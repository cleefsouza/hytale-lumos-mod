package br.pepola.mod.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

public final class OrbManager {

    private final String SUB_LOGGER = "Manager";
    private final HytaleLogger logger;

    private final Set<UUID> activeOrbs = ConcurrentHashMap.newKeySet();
    private volatile boolean onLoop = false; // avoids multiple loops
    private final AtomicLong loopToken = new AtomicLong(0); // token to invalidate already queued ticks

    public OrbManager(HytaleLogger logger) {
        this.logger = logger.getSubLogger(SUB_LOGGER);
    }

    /**
     * Activate the orb for the player and ensure the loop is running
     */
    public void enable(UUID playerId, World world) {

        if (Objects.isNull(playerId) || Objects.isNull(world)) {
            return;
        }

        boolean added = activeOrbs.add(playerId);

        if (added) {
            logger.at(Level.INFO).log("Orb enabled for %s", playerId);
        }

        this.startLoopIfNeeded(world); // start loop 1x
    }

    /**
     * Deactivates the orb for the player
     * There's no need to stop the loop immediately
     * It stops automatically when activeOrbs becomes empty (see tick)
     */
    public void disable(UUID playerId) {
        if (Objects.isNull(playerId)) {
            return;
        }

        this.activeOrbs.remove(playerId);

        if (this.activeOrbs.isEmpty()) {
            this.onLoop = false;
            this.loopToken.incrementAndGet(); // invalidates pending ticks.
            logger.at(Level.INFO).log("Orb disabled for %s", playerId);
        }
    }

    public boolean isEnabled(UUID playerId) {
        return playerId != null && this.activeOrbs.contains(playerId);
    }

    public int activeCount() {
        return this.activeOrbs.size();
    }

    private void startLoopIfNeeded(World world) {

        if (this.onLoop) {
            return;
        }

        synchronized (this) {
            if (this.onLoop) {
                return;
            }

            this.onLoop = true;
        }

        logger.at(Level.INFO).log("Starting orb loop");

        long token = loopToken.incrementAndGet(); // new token
        scheduleNextTick(world, token);
    }

    private void scheduleNextTick(World world, long token) {
        world.execute(() -> tick(world, token));
    }

    private void tick(World world, long token) {

        logger.at(Level.INFO).log("Starting tick");

        if (token != this.loopToken.get()) { // if this token is old, do nothing and don't reschedule
            return;
        }

        if (this.activeOrbs.isEmpty()) {
            this.onLoop = false;
            this.logger.at(Level.WARNING).log("Stopping orb loop (No active orbs)");

            return;
        }

        EntityStore entityStore = world.getEntityStore();
        Store<EntityStore> store = entityStore.getStore();

        for (UUID uuid : activeOrbs) {
            Ref<EntityStore> ref = entityStore.getRefFromUUID(uuid);

            if (!isValid(ref)) {
                this.logger.at(Level.WARNING).log("Ref invalid for %s (Orb tick)", uuid);
                continue;
            }

            TransformComponent transform = store.getComponent(ref, TransformComponent.getComponentType());

            if (Objects.isNull(transform)) {
                this.logger.at(Level.WARNING).log("Transform not found for %s (Orb tick)", uuid);
                continue;
            }

            var pos = transform.getPosition();

            world.getLogger().at(Level.INFO).log(
                    String.format(
                            "Orb tick for %s (X: %.2f | Y: %.2f | Z: %.2f)",
                            uuid,
                            pos.getX(),
                            pos.getY(),
                            pos.getZ()
                    )
            );
        }

        this.logger.at(Level.INFO).log("Rescheduling loop");

        this.scheduleNextTick(world, token);
    }

    private static boolean isValid(@Nullable Ref<EntityStore> ref) {
        return !Objects.isNull(ref) && ref.isValid();
    }
}
