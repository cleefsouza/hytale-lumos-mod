package br.pepola.mod.commands;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.particle.config.ParticleSystem;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Objects;
import java.util.logging.Level;

public class ListParticlesCommand extends CommandBase {
    private final String SUB_LOGGER = "Particles";
    private final HytaleLogger logger;

    public ListParticlesCommand(HytaleLogger logger) {
        super("par", "Listagem das Particles disponiveis");

        this.logger = logger.getSubLogger(SUB_LOGGER);
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.at(Level.INFO).log("Listando Particles");

        var map = ParticleSystem.getAssetMap();
        int sizeMap = map.getAssetMap().size();

        this.logger.at(Level.INFO).log("Particles carregadas: %d", sizeMap);

        int shown = 0;
        int max = 100;

        for (var entry: map.getAssetMap().entrySet()) {
            if (shown >= max) {
                break;
            }

            if (Objects.isNull(entry.getKey()) || Objects.isNull(entry.getValue())) {
                continue;
            }

            this.logger.at(Level.INFO).log("- %s", entry.getValue().getId());

            shown++;
        }

        if (sizeMap > max) {
            this.logger.at(Level.INFO).log("Exibindo primeiras %d (Sem filtros)", max);
        }
    }
}
