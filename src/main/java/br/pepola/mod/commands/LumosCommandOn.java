package br.pepola.mod.commands;

import br.pepola.mod.manager.Manager;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public class LumosCommandOn extends CommandBase {

    public static final String LUMOS_ON = "on";

    private final String LOGGER = LumosCommandOn.class.getName();
    private final Logger logger = Logger.getLogger(LOGGER);
    private final Manager manager;

    public LumosCommandOn(Manager manager) {
        super(LUMOS_ON, "Comando para ativar o Lumos (ON)");

        this.manager = manager;
    }

    @SuppressWarnings("removal")
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.info("Lumos ON");

        if (!commandContext.isPlayer()) {
            commandContext.sendMessage(Message.raw("Apenas jogadoes podem usar esse comando"));
            this.logger.warning("Jogador não encontrado");

            return;
        }

        Player player = this.getPlayer(commandContext);
        UUID playerUUID = player.getUuid();

        this.logger.info(String.format("Jogador %s identificado | UUID: %s", player.getDisplayName(), player.getUuid()));

        Ref<EntityStore> playerRef = commandContext.senderAsPlayerRef();

        if (Objects.isNull(playerRef) || !playerRef.isValid()) {
            this.logger.warning("Referência do jogador não encontrada");

            return;
        }

        World world = playerRef.getStore().getExternalData().getWorld();

        manager.enable(playerUUID, world);

        commandContext.sendMessage(Message.raw("Lumos ON"));
        this.logger.info(String.format("Lumos ON para %s (UUID=%s)", player.getDisplayName(), playerUUID));
    }

    private Player getPlayer(CommandContext commandContext) {
        return commandContext.senderAs(Player.class);
    }

    @Override
    protected boolean canGeneratePermission() {
        return false;
    }
}
