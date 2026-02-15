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

public class OrbCommandOn extends CommandBase {

    private final String SUB_LOGGER = "ON";
    private final HytaleLogger logger;
    private final OrbManager manager;

    public OrbCommandOn(HytaleLogger logger, OrbManager manager) {
        super("on", "Power On Command (ON)");

        this.logger = logger.getSubLogger(SUB_LOGGER);
        this.manager = manager;
    }

    @SuppressWarnings("removal")
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.at(Level.INFO).log("Turn ON orb");

        if (!commandContext.isPlayer()) {
            commandContext.sendMessage(Message.raw("Only players can use this command"));
            this.logger.at(Level.WARNING).log("Player not found");

            return;
        }

        Player player = this.getPlayer(commandContext);
        UUID playerUUID = player.getUuid();

        this.logger.at(Level.INFO).log("Player %s identified | UUID: %s", player.getDisplayName(), player.getUuid());

        Ref<EntityStore> playerRef = commandContext.senderAsPlayerRef();

        if (Objects.isNull(playerRef) || !playerRef.isValid()) {
            this.logger.at(Level.WARNING).log("PlayerRef not found");

            return;
        }

        World world = playerRef.getStore().getExternalData().getWorld();

        manager.enable(playerUUID, world);

        commandContext.sendMessage(Message.raw("Orb ON"));
        logger.at(Level.INFO).log("Orb ON for %s (%s)", player.getDisplayName(), playerUUID);
    }

    private Player getPlayer(CommandContext commandContext) {
        return commandContext.senderAs(Player.class);
    }
}
