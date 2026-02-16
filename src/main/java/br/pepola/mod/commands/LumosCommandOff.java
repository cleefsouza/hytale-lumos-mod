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

public class LumosCommandOff extends CommandBase {

    public static final String LUMOS_OFF = "off";

    private final String LOGGER = LumosCommandOff.class.getName();
    private final Logger logger = Logger.getLogger(LOGGER);
    private final Manager manager;

    public LumosCommandOff(Manager manager) {
        super(LUMOS_OFF, "Comando para desativar o Lumos (OFF)");

        this.manager = manager;
    }

    @SuppressWarnings("removal")
    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.info("Lumos OFF");

        if (!commandContext.isPlayer()) {
            commandContext.sendMessage(Message.raw("Apenas jogadoes podem usar esse comando"));
            this.logger.warning("Jogador não encontrado");

            return;
        }

        Player player = commandContext.senderAs(Player.class);
        UUID playerUUID = player.getUuid();

        Ref<EntityStore> playerRef = commandContext.senderAsPlayerRef();

        if (Objects.isNull(playerRef) || !playerRef.isValid()) {
            this.logger.warning("Referência do jogador não encontrada");

            return;
        }

        World world = playerRef.getStore().getExternalData().getWorld();

        manager.disable(playerUUID, world);

        commandContext.sendMessage(Message.raw("Lumos OFF"));
        this.logger.info(String.format("Lumos OFF para %s (UUID=%s)", player.getDisplayName(), playerUUID));
    }

    @Override
    protected boolean canGeneratePermission() {
        return false;
    }
}
