package br.pepola.mod.commands;

import com.hypixel.hytale.server.core.asset.type.modelvfx.config.ModelVFX;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Objects;
import java.util.logging.Logger;

public class ListVFXCommand extends CommandBase {
    private final String LOGGER = ListVFXCommand.class.getName();
    private final Logger logger = Logger.getLogger(LOGGER);

    public ListVFXCommand() {
        super("vfx", "Listagem dos ModelVFX disponiveis");
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.info("Listando ModelVFX");

        var map = ModelVFX.getAssetMap();
        int sizeMap = map.getAssetMap().size();

        this.logger.info(String.format("ModelVFX carregados: %d", sizeMap));

        int shown = 0;
        int max = 80;

        for (int i = 0; i < sizeMap && shown < max; i++) {
            ModelVFX vfx = map.getAsset(i);

            if (Objects.isNull(vfx) || Objects.isNull(vfx.getId())) {
                continue;
            }

            this.logger.info(String.format("- %s", vfx.getId()));

            shown++;
        }

        if (sizeMap > max) {
            this.logger.info(String.format("Exibindo primeiros %d (Sem filtros)", max));
        }
    }
}
