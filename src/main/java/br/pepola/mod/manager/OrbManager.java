package br.pepola.mod.manager;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.util.MathUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.section.BlockSection;
import com.hypixel.hytale.server.core.universe.world.lighting.ChunkLightingManager;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

public class OrbManager {

    private final String BLOCK_ID = "WinterHoliday_Single_Light";
    private final long PERIOD_MS = 50;

    private final String SUB_LOGGER = "Manager";
    private final HytaleLogger logger;

    private final Set<UUID> active = ConcurrentHashMap.newKeySet();
    private final ConcurrentHashMap<UUID, Placement> placements = new ConcurrentHashMap<>(); // by player: where is the light and what block was there before?

    private volatile boolean onLoop = false;
    private final AtomicLong loopToken = new AtomicLong(0);

    private volatile int lightBlockId = -1;

    public OrbManager(HytaleLogger logger) {
        this.logger = logger.getSubLogger(SUB_LOGGER);
    }

    public void enable(UUID playerUUID, World world) {

        if (Objects.isNull(playerUUID) || Objects.isNull(world)) {
            return;
        }

        if (active.add(playerUUID)) {
            this.logger.at(Level.INFO).log("Orb enabled for %s", playerUUID);
        }

        this.startLoopIfNeeded(world);
    }

    public void disable(UUID playerUUID, World world) {
        if (Objects.isNull(playerUUID)) {
            return;
        }

        active.remove(playerUUID);

        long newToken = loopToken.incrementAndGet(); // if it becomes empty, "kill" the current loop
        this.onLoop = false;

        this.logger.at(Level.INFO).log("Orb disabled for %s (token now=%d)", playerUUID, newToken);

        if (!Objects.isNull(world)) {
            world.execute(() -> this.removePlacement(world, playerUUID)); // remove the light placed by this player (restore the previous block)
        }
    }

    private void startLoopIfNeeded(World world) {
        if (this.onLoop) {
            return;
        }

        synchronized (this) {
            if (this.onLoop) return;
            this.onLoop = true;
        }

        long token = this.loopToken.incrementAndGet();
        this.logger.at(Level.INFO).log("Starting orb loop (token=%d)", token);

        this.scheduleNextTick(world, token);
    }

    private void scheduleNextTick(World world, long token) {
        CompletableFuture
                .delayedExecutor(PERIOD_MS, TimeUnit.MILLISECONDS)
                .execute(() -> world.execute(() -> tick(world, token)));
    }

    private void tick(World world, long token) {

        int id = resolveLightBlockIdOnce();

        if (id == 0) {
            this.onLoop = false;
            this.logger.at(Level.SEVERE).log("Stopping orb loop because light block id is invalid (0).");

            return;
        }

        if (token != this.loopToken.get()) { // if someone called /off and generated a new token, this old loop ends here
            this.logger.at(Level.INFO).log("Loop canceled (token=%d)", token);

            return;
        }

        if (active.isEmpty()) {
            this.onLoop = false;
            this.logger.at(Level.INFO).log("Stopping orb loop (no active players)");

            return;
        }

        EntityStore entityStore = world.getEntityStore();
        Store<EntityStore> store = entityStore.getStore();

        for (UUID playerUUID : active) {
            Ref<EntityStore> ref = entityStore.getRefFromUUID(playerUUID);

            if (!isValid(ref)) {
                this.logger.at(Level.WARNING).log("Ref invalid for %s", playerUUID);
                continue;
            }

            TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());

            if (Objects.isNull(transform)) {
                this.logger.at(Level.WARNING).log("Transform not found for %s", playerUUID);
                continue;
            }

            Vector3d p = transform.getPosition();

            /**
             * target block selection:
             * - floor in X/Z
             * - Y: slightly below/around the player (adjust if desired)
             */
            int bx = MathUtil.floor(p.getX());
            int by = MathUtil.floor(p.getY()); // you can test by -1 or by +1 depending on the "foot"
            int bz = MathUtil.floor(p.getZ());

            this.moveLightTo(world, playerUUID, bx, by, bz, id);
        }

        this.scheduleNextTick(world, token);
    }

    private void moveLightTo(World world, UUID playerUUID, int x, int y, int z, int id) {

        if (!active.contains(playerUUID)) { // lock "replace after power off"
            Placement prev = placements.remove(playerUUID);

            if (!Objects.isNull(prev)) {
                restoreBlock(world, prev);
            }

            return;
        }

        Placement prev = placements.get(playerUUID);

        if (!Objects.isNull(prev) && prev.x == x && prev.y == y && prev.z == z) { // if it hasn't changed blocks, it doesn't do anything
            return;
        }

        if (!Objects.isNull(prev)) { // restores previous block (if it existed)
            this.restoreBlock(world, prev);
        }

        Placement placement = placeLight(world, x, y, z, id); // place the light in the new location and save the "before".

        if (!Objects.isNull(placement)) {
            placements.put(playerUUID, placement);
        }
    }

    private void removePlacement(World world, UUID playerUUID) {
        Placement placement = placements.remove(playerUUID);

        if (!Objects.isNull(placement)) {
            this.restoreBlock(world, placement);
        }
    }

    @Nullable
    private Placement placeLight(World world, int worldX, int worldY, int worldZ, int id) {

        ChunkStore chunkStore = world.getChunkStore();
        long chunkIndex = ChunkUtil.indexChunkFromBlock(worldX, worldZ);

        Ref<ChunkStore> chunkRef = chunkStore.getChunkReference(chunkIndex);

        if (Objects.isNull(chunkRef) || !chunkRef.isValid()) {
            this.logger.at(Level.WARNING).log("Chunk not found");

            return null; // chunk not charged, don't move
        }

        Store<ChunkStore> cs = chunkStore.getStore();
        BlockChunk blockChunk = cs.getComponent(chunkRef, BlockChunk.getComponentType());

        if (Objects.isNull(blockChunk)) {
            this.logger.at(Level.WARNING).log("Block chunk not found");

            return null;
        }

        int sectionY = (MathUtil.floor(worldY) >> 5); // 32 blocks per section

        BlockSection section = blockChunk.getSectionAtBlockY(worldY);

        if (Objects.isNull(section)) {
            this.logger.at(Level.WARNING).log("Block section not found");

            return null;
        }

        int lx = worldX & 31;
        int ly = worldY & 31;
        int lz = worldZ & 31;

        int beforeId = section.get(lx, ly, lz);
        int beforeRot = section.getRotationIndex(lx, ly, lz);
        int beforeFill = section.getFiller(lx, ly, lz);

        boolean changed = section.set(lx, ly, lz, id, 0, 0); // set the emissive block; rotation/filler 0 for now (simple logic)

        if (changed) {
            section.invalidateLocalLight(); // same logic as official lighting commands
            blockChunk.invalidateChunkSection(sectionY);

            ChunkLightingManager lighting = world.getChunkLighting();
            lighting.addToQueue(new Vector3i(blockChunk.getX(), sectionY, blockChunk.getZ()));
        }

        return new Placement(worldX, worldY, worldZ, beforeId, beforeRot, beforeFill);
    }

    private void restoreBlock(World world, Placement placement) {

        ChunkStore chunkStore = world.getChunkStore();
        long chunkIndex = ChunkUtil.indexChunkFromBlock(placement.x, placement.z);

        Ref<ChunkStore> chunkRef = chunkStore.getChunkReference(chunkIndex);

        if (Objects.isNull(chunkRef) || !chunkRef.isValid()) {
            this.logger.at(Level.WARNING).log("Chunk ref invalid");

            return;
        }

        Store<ChunkStore> cs = chunkStore.getStore();
        BlockChunk blockChunk = cs.getComponent(chunkRef, BlockChunk.getComponentType());

        if (Objects.isNull(blockChunk)) {
            this.logger.at(Level.WARNING).log("Block chunk not found");

            return;
        }

        int sectionY = (MathUtil.floor(placement.y) >> 5);

        BlockSection section = blockChunk.getSectionAtBlockY(placement.y);

        if (Objects.isNull(section)) {
            this.logger.at(Level.WARNING).log("Block section not found");

            return;
        }

        int lx = placement.x & 31;
        int ly = placement.y & 31;
        int lz = placement.z & 31;

        int currentId = section.get(lx, ly, lz); // get the current block

        boolean changed = section.set(lx, ly, lz, placement.beforeId, placement.beforeRot, placement.beforeFiller);

        if (changed || currentId == lightBlockId) {
            section.invalidateLocalLight();
            blockChunk.invalidateChunkSection(sectionY);
            world.getChunkLighting().addToQueue(new Vector3i(blockChunk.getX(), sectionY, blockChunk.getZ()));
        }
    }

    private int resolveLightBlockIdOnce() {
        int id = this.lightBlockId;

        if (id != -1) return id;

        synchronized (this) {
            if (this.lightBlockId != -1) {
                return this.lightBlockId;
            }

            int resolved = BlockType.getBlockIdOrUnknown(
                    BLOCK_ID,
                    "Unknown BlockType: %s",
                    new Object[]{BLOCK_ID}
            );

            this.lightBlockId = resolved;

            if (resolved == 0) {
                logger.at(Level.SEVERE).log("BLOCK_ID '%s' resolved to 0. Orb light DISABLED.", BLOCK_ID);
            } else {
                logger.at(Level.INFO).log("Light block resolved: id=%d (%s)", resolved, BLOCK_ID);
            }

            return resolved;
        }
    }

    private static boolean isValid(@Nullable Ref<EntityStore> ref) {
        return !Objects.isNull(ref) && ref.isValid();
    }
}
