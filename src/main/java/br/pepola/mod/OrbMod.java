package br.pepola.mod;

import br.pepola.mod.commands.OrbCommandOff;
import br.pepola.mod.commands.OrbCommandOn;
import br.pepola.mod.commands.OrbManager;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.command.system.CommandRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.logging.Level;

public class OrbMod extends JavaPlugin {

    private final String SUB_LOGGER = "Plugin";

    public OrbMod(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        HytaleLogger logger = getLogger().getSubLogger(SUB_LOGGER);
        logger.at(Level.INFO).log("Starting mod");

        OrbManager manager = new OrbManager(logger);

        CommandRegistry commandRegistry = this.getCommandRegistry();
        commandRegistry.registerCommand(new OrbCommandOn(logger, manager));
        commandRegistry.registerCommand(new OrbCommandOff(logger, manager));

        logger.at(Level.INFO).log("Registered commands");
    }
}
