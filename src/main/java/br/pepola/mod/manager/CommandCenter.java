package br.pepola.mod.manager;

import br.pepola.mod.commands.*;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class CommandCenter extends AbstractCommandCollection {

    public static final String LUMOS = "lumos";

    public CommandCenter(Manager manager) {
        super(LUMOS, "Central de comandos do plugin");

        this.addSubCommand(new LumosCommandOn(manager));
        this.addSubCommand(new LumosCommandOff(manager));

        // apenas para desenvolvimento
        // this.addSubCommand(new ListEffectsCommand());
        // this.addSubCommand(new ListVFXCommand());
        // this.addSubCommand(new ListParticlesCommand());
    }

    @Override
    protected boolean canGeneratePermission() {
        return false;
    }
}
