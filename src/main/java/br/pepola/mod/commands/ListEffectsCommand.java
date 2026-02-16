package br.pepola.mod.commands;

import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Objects;
import java.util.logging.Logger;

public class ListEffectsCommand extends CommandBase {
    private final String LOGGER = ListEffectsCommand.class.getName();
    private final Logger logger = Logger.getLogger(LOGGER);

    public ListEffectsCommand() {
        super("eff", "Listagem das EntityEffects disponiveis");
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.info("Listando EntityEffects");

        var map = EntityEffect.getAssetMap();
        int sizeMap = map.getAssetMap().size();

        this.logger.info(String.format("EntityEffects carregadas: %d", sizeMap));

        int shown = 0;
        int max = 80;

        for (int i = 0; i < sizeMap && shown < max; i++) {
            EntityEffect eff = map.getAsset(i);

            if (Objects.isNull(eff) || Objects.isNull(eff.getId())) {
                continue;
            }

            this.logger.info(String.format("- %s", eff.getId()));

            shown++;
        }

        if (sizeMap > max) {
            this.logger.info(String.format("Exibindo primeiras %d (Sem filtros)", max));
        }
    }
}
