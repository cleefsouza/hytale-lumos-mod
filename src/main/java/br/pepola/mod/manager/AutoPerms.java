package br.pepola.mod.manager;

import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import static br.pepola.mod.commands.LumosCommandOff.*;
import static br.pepola.mod.commands.LumosCommandOn.*;
import static br.pepola.mod.manager.CommandCenter.LUMOS;

public final class AutoPerms {

    public static final String LUMOS_PERM = "hytale.command.lumos";

    private AutoPerms() {
    }

    @SuppressWarnings("removal")
    public static void onPlayerReady(PlayerReadyEvent event) {

        Logger logger = Logger.getLogger(AutoPerms.class.getName());
        logger.info("Adicionando permissões");

        Player player = event.getPlayer();
        UUID playerUUID = player.getUuid();

        if (Objects.isNull(playerUUID)) {
            logger.warning("Jogador não encontrado");

            return;
        }

        PermissionsModule perms = PermissionsModule.get();

        if (Objects.isNull(perms)) {
            logger.warning("PermissionsModule não encontrado");

            return;
        }

        perms.addUserPermission(playerUUID, getCommandsPerms());

        for (var provider : perms.getProviders()) {
            logger.info("Provider: " + provider.getName());
            logger.info("User perms: " + provider.getUserPermissions(playerUUID));
        }
    }

    public static Set<String> getCommandsPerms() {
        String base = HytalePermissions.fromCommand(LUMOS);
        String on   = HytalePermissions.fromCommand(LUMOS, LUMOS_ON);
        String off  = HytalePermissions.fromCommand(LUMOS, LUMOS_OFF);

        return Set.of(base, on, off);
    }
}
