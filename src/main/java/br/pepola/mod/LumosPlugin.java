package br.pepola.mod;

import br.pepola.mod.manager.CommandCenter;
import br.pepola.mod.manager.Manager;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.command.system.CommandRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.logging.Level;

public class LumosPlugin extends JavaPlugin {

    private final String SUB_LOGGER = "Plugin";

    public LumosPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        HytaleLogger logger = getLogger().getSubLogger(SUB_LOGGER);
        logger.at(Level.INFO).log("Iniciando mod");

        Manager manager = new Manager(logger);

        CommandRegistry commandRegistry = this.getCommandRegistry();
        commandRegistry.registerCommand(new CommandCenter(logger, manager));

        logger.at(Level.INFO).log("Comandos registrados");
    }
}
