package br.pepola.mod.manager;

import br.pepola.mod.commands.ListEffectsCommand;
import br.pepola.mod.commands.ListVFXCommand;
import br.pepola.mod.commands.OrbCommandOff;
import br.pepola.mod.commands.OrbCommandOn;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class CommandCenter extends AbstractCommandCollection {

    private final String SUB_LOGGER = "Command Center";
    private final HytaleLogger logger;
    private final OrbManager manager;

    public CommandCenter(HytaleLogger logger, OrbManager manager) {
        super("orb", "Command center of plugin");

        this.manager = manager;
        this.logger = logger.getSubLogger(SUB_LOGGER);

        this.addSubCommand(new OrbCommandOn(logger, manager));
        this.addSubCommand(new OrbCommandOff(logger, manager));
        this.addSubCommand(new ListEffectsCommand(logger));
        this.addSubCommand(new ListVFXCommand(logger));
    }
}
