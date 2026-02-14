package br.pepola.mod;

import br.pepola.mod.commands.OrbCommand;
import com.hypixel.hytale.server.core.command.system.CommandRegistry;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.logging.Level;

public class OrbMod extends JavaPlugin {

    private final String SUB_LOGGER = "[OrbMod]";

    public OrbMod(@NonNullDecl JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        getLogger().getSubLogger(SUB_LOGGER);
        getLogger().at(Level.INFO).log("Configurando mod");

        CommandRegistry commandRegistry = this.getCommandRegistry();
        commandRegistry.registerCommand(new OrbCommand(getLogger()));
    }
}
