package br.pepola.mod.manager;

import br.pepola.mod.commands.*;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class CommandCenter extends AbstractCommandCollection {

    private final String SUB_LOGGER = "Central";
    private final HytaleLogger logger;

    public CommandCenter(HytaleLogger logger, Manager manager) {
        super("lumos", "Central de comandos do plugin");

        this.logger = logger.getSubLogger(SUB_LOGGER);

        this.addSubCommand(new LumosCommandOn(logger, manager));
        this.addSubCommand(new LumosCommandOff(logger, manager));

        // apenas para desenvolvimento
        // this.addSubCommand(new ListEffectsCommand(logger));
        // this.addSubCommand(new ListVFXCommand(logger));
        // this.addSubCommand(new ListParticlesCommand(logger));
    }
}
