package br.pepola.mod.commands;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.asset.type.modelvfx.config.ModelVFX;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.Objects;
import java.util.logging.Level;

public class ListVFXCommand extends CommandBase {
    private final String SUB_LOGGER = "VFX";
    private final HytaleLogger logger;

    public ListVFXCommand(HytaleLogger logger) {
        super("vfx", "List model vfx");

        this.logger = logger.getSubLogger(SUB_LOGGER);
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.at(Level.INFO).log("List vfx");

        var map = ModelVFX.getAssetMap();
        int sizeMap = map.getAssetMap().size();

        this.logger.at(Level.INFO).log("ModelVFX loaded: %d", sizeMap);

        int shown = 0;
        int max = 80;

        for (int i = 0; i < sizeMap && shown < max; i++) {
            ModelVFX vfx = map.getAsset(i);

            if (Objects.isNull(vfx) || Objects.isNull(vfx.getId())) {
                continue;
            }

            this.logger.at(Level.INFO).log("- %s", vfx.getId());

            shown++;
        }

        if (sizeMap > max) {
            this.logger.at(Level.INFO).log("Showing first %d (No filter yet)", max);
        }
    }
}
