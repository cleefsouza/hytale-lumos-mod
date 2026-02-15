package br.pepola.mod.commands;

import br.pepola.mod.manager.OrbManager;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class OrbCommandOff extends CommandBase {

    private final String SUB_LOGGER = "OFF";
    private final HytaleLogger logger;
    private final OrbManager manager;

    public OrbCommandOff(HytaleLogger logger, OrbManager manager) {
        super("off", "Power OFF Command (OFF)");

        this.logger = logger.getSubLogger(SUB_LOGGER);
        this.manager = manager;
    }

    @SuppressWarnings("removal")
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.at(Level.INFO).log("Turn OFF orb");

        if (!commandContext.isPlayer()) {
            commandContext.sendMessage(Message.raw("Only players can use this command"));
            this.logger.at(Level.WARNING).log("Player not found");

            return;
        }

        Player player = commandContext.senderAs(Player.class);
        UUID playerUUID = player.getUuid();

        Ref<EntityStore> playerRef = commandContext.senderAsPlayerRef();

        if (Objects.isNull(playerRef) || !playerRef.isValid()) {
            this.logger.at(Level.WARNING).log("PlayerRef not found");

            return;
        }

        World world = playerRef.getStore().getExternalData().getWorld();

        manager.disable(playerUUID, world);

        commandContext.sendMessage(Message.raw("Orb OFF"));
        logger.at(Level.INFO).log("Orb OFF for %s (%s)", player.getDisplayName(), playerUUID);
    }
}
