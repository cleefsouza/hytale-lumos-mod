package br.pepola.mod.manager;

import br.pepola.mod.utils.Utils;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.ColorLight;
import com.hypixel.hytale.server.core.modules.entity.component.DynamicLight;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class Manager {

    private final String SUB_LOGGER = "Manager";
    private final HytaleLogger logger;

    private boolean lumosActive = false;

    private final byte RADIUS = Utils.clampByte(5);
    private final byte RED = Utils.clampByte(14);
    private final byte GREEN = Utils.clampByte(12);
    private final byte BLUE = Utils.clampByte(9);

    private final ColorLight LIGHT = new ColorLight(RADIUS, RED, GREEN, BLUE);

    public Manager(HytaleLogger logger) {
        this.logger = logger.getSubLogger(SUB_LOGGER);
    }

    public void enable(UUID playerUUID, World world) {

        if (Objects.isNull(playerUUID) || Objects.isNull(world)) {
            this.logger.at(Level.WARNING).log("Jogador/Mundo não encontrado");

            return;
        }

        world.execute(() -> {
            EntityStore es = world.getEntityStore();
            Ref<EntityStore> ref = es.getRefFromUUID(playerUUID);

            if (!Utils.isValid(ref)) {
                this.logger.at(Level.WARNING).log("Referência inválida para jogador (UUID=%s)", playerUUID);

                return;
            }

            Store<EntityStore> store = es.getStore();

            DynamicLight dynamicLight = store.getComponent(ref, DynamicLight.getComponentType());

            if (!Objects.isNull(dynamicLight)) {
                dynamicLight.setColorLight(new ColorLight(LIGHT));
                store.putComponent(ref, DynamicLight.getComponentType(), dynamicLight);

                return;
            }

            store.putComponent(ref, DynamicLight.getComponentType(), new DynamicLight(new ColorLight(LIGHT)));

            this.lumosActive = true;
        });
    }

    public void disable(UUID playerUUID, World world) {

        if (!this.lumosActive) {
            return;
        }

        if (Objects.isNull(playerUUID) || Objects.isNull(world)) {
            this.logger.at(Level.WARNING).log("Jogador/Mundo não encontrado");

            return;
        }

        world.execute(() -> {
            EntityStore es = world.getEntityStore();

            Ref<EntityStore> ref = es.getRefFromUUID(playerUUID);

            if (!Utils.isValid(ref)) {
                this.logger.at(Level.WARNING).log("Referência inválida para jogador (UUID=%s)", playerUUID);

                return;
            }

            es.getStore().removeComponent(ref, DynamicLight.getComponentType());

            this.lumosActive = false;
        });
    }
}
