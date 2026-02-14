package br.pepola.mod.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class OrbCommandOff extends CommandBase {

    private final String SUB_LOGGER = "Command OFF";
    private final HytaleLogger logger;
    private final OrbManager manager;

    public OrbCommandOff(HytaleLogger logger, OrbManager manager) {
        super("orboff", "Power OFF Command (OFF)");

        this.logger = logger.getSubLogger(SUB_LOGGER);
        this.manager = manager;
    }

    @SuppressWarnings("removal")
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.at(Level.INFO).log("Turn off orb");

        if (!commandContext.isPlayer()) {
            commandContext.sendMessage(Message.raw("Only players can use this command"));
            this.logger.at(Level.WARNING).log("Player not found");

            return;
        }

        Player player = this.getPlayer(commandContext);
        UUID playerId = player.getUuid();

        commandContext.sendMessage(Message.raw("Hi " + player.getDisplayName() + " <3"));

        this.logger.at(Level.INFO).log("Player %s identified | UUID: %s", player.getDisplayName(), player.getUuid());

        Ref<EntityStore> ref = commandContext.senderAsPlayerRef();

        if (Objects.isNull(ref)) {
            this.logger.at(Level.WARNING).log("PlayerRef not found");

            return;
        }

        this.manager.disable(playerId);

        commandContext.sendMessage(Message.raw("Orb OFF"));
        logger.at(Level.INFO).log("Orb OFF for %s (%s)", player.getDisplayName(), playerId);
    }

    private Player getPlayer(CommandContext commandContext) {
        return commandContext.senderAs(Player.class);
    }
}
