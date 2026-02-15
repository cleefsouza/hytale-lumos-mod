package br.pepola.mod.commands;

import br.pepola.mod.manager.Manager;
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

public class LumosCommandOn extends CommandBase {

    private final String SUB_LOGGER = "ON";
    private final HytaleLogger logger;
    private final Manager manager;

    public LumosCommandOn(HytaleLogger logger, Manager manager) {
        super("on", "Comando para ativar o Lumos (ON)");

        this.logger = logger.getSubLogger(SUB_LOGGER);
        this.manager = manager;
    }

    @SuppressWarnings("removal")
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.at(Level.INFO).log("Lumos ON");

        if (!commandContext.isPlayer()) {
            commandContext.sendMessage(Message.raw("Apenas jogadoes podem usar esse comando"));
            this.logger.at(Level.WARNING).log("Jogador não encontrado");

            return;
        }

        Player player = this.getPlayer(commandContext);
        UUID playerUUID = player.getUuid();

        this.logger.at(Level.INFO).log("Jogador %s identificado | UUID: %s", player.getDisplayName(), player.getUuid());

        Ref<EntityStore> playerRef = commandContext.senderAsPlayerRef();

        if (Objects.isNull(playerRef) || !playerRef.isValid()) {
            this.logger.at(Level.WARNING).log("Referência do jogador não encontrada");

            return;
        }

        World world = playerRef.getStore().getExternalData().getWorld();

        manager.enable(playerUUID, world);

        commandContext.sendMessage(Message.raw("Lumos ON"));
        logger.at(Level.INFO).log("Lumos ON para %s (UUID=%s)", player.getDisplayName(), playerUUID);
    }

    private Player getPlayer(CommandContext commandContext) {
        return commandContext.senderAs(Player.class);
    }
}
