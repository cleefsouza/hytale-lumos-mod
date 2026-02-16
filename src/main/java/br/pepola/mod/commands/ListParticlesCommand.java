package br.pepola.mod.commands;

import com.hypixel.hytale.server.core.asset.type.particle.config.ParticleSystem;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Objects;
import java.util.logging.Logger;

public class ListParticlesCommand extends CommandBase {
    private final String LOGGER = ListParticlesCommand.class.getName();
    private final Logger logger = Logger.getLogger(LOGGER);

    public ListParticlesCommand() {
        super("par", "Listagem das Particles disponiveis");
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.info("Listando Particles");

        var map = ParticleSystem.getAssetMap();
        int sizeMap = map.getAssetMap().size();

        this.logger.info(String.format("Particles carregadas: %d", sizeMap));

        int shown = 0;
        int max = 100;

        for (var entry: map.getAssetMap().entrySet()) {
            if (shown >= max) {
                break;
            }

            if (Objects.isNull(entry.getKey()) || Objects.isNull(entry.getValue())) {
                continue;
            }

            this.logger.info(String.format("- %s", entry.getValue().getId()));

            shown++;
        }

        if (sizeMap > max) {
            this.logger.info(String.format("Exibindo primeiras %d (Sem filtros)", max));
        }
    }
}
