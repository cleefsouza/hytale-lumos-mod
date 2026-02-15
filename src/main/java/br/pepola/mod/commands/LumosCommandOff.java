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

public class LumosCommandOff extends CommandBase {

    private final String SUB_LOGGER = "OFF";
    private final HytaleLogger logger;
    private final Manager manager;

    public LumosCommandOff(HytaleLogger logger, Manager manager) {
        super("off", "Comando para desativar o Lumos (OFF)");

        this.logger = logger.getSubLogger(SUB_LOGGER);
        this.manager = manager;
    }

    @SuppressWarnings("removal")
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.at(Level.INFO).log("Lumos OFF");

        if (!commandContext.isPlayer()) {
            commandContext.sendMessage(Message.raw("Apenas jogadoes podem usar esse comando"));
            this.logger.at(Level.WARNING).log("Jogador não encontrado");

            return;
        }

        Player player = commandContext.senderAs(Player.class);
        UUID playerUUID = player.getUuid();

        Ref<EntityStore> playerRef = commandContext.senderAsPlayerRef();

        if (Objects.isNull(playerRef) || !playerRef.isValid()) {
            this.logger.at(Level.WARNING).log("Referência do jogador não encontrada");

            return;
        }

        World world = playerRef.getStore().getExternalData().getWorld();

        manager.disable(playerUUID, world);

        commandContext.sendMessage(Message.raw("Lumos OFF"));
        logger.at(Level.INFO).log("Lumos OFF para %s (UUID=%s)", player.getDisplayName(), playerUUID);
    }
}
