package br.pepola.mod.commands;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Objects;
import java.util.logging.Level;

public class ListEffectsCommand extends CommandBase {
    private final String SUB_LOGGER = "Effects";
    private final HytaleLogger logger;

    public ListEffectsCommand(HytaleLogger logger) {
        super("eff", "List entity effects");

        this.logger = logger.getSubLogger(SUB_LOGGER);
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.at(Level.INFO).log("List effects");

        var map = EntityEffect.getAssetMap();
        int sizeMap = map.getAssetMap().size();

        this.logger.at(Level.INFO).log("EntityEffects loaded: %d", sizeMap);

        int shown = 0;
        int max = 80;

        for (int i = 0; i < sizeMap && shown < max; i++) {
            EntityEffect eff = map.getAsset(i);

            if (Objects.isNull(eff) || Objects.isNull(eff.getId())) {
                continue;
            }

            this.logger.at(Level.INFO).log("- %s", eff.getId());

            shown++;
        }

        if (sizeMap > max) {
            this.logger.at(Level.INFO).log("Showing first %d (No filter yet)", max);
        }
    }
}
