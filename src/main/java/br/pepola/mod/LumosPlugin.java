package br.pepola.mod;

import br.pepola.mod.manager.CommandCenter;
import br.pepola.mod.manager.Manager;
import com.hypixel.hytale.server.core.command.system.CommandRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.logging.Logger;

public class LumosPlugin extends JavaPlugin {

    private final String LOGGER = LumosPlugin.class.getName();

    public LumosPlugin(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        Logger logger = Logger.getLogger(LOGGER);
        logger.info("Iniciando mod");

//        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, AutoPerms::onPlayerReady);

        Manager manager = new Manager();

        CommandRegistry commandRegistry = this.getCommandRegistry();
        commandRegistry.registerCommand(new CommandCenter(manager));

        logger.info("Comandos registrados");
    }
}
