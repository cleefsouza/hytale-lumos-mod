package br.pepola.mod.commands;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.util.logging.Level;

public class OrbCommand extends CommandBase {

    private final String SUB_LOGGER = "[OrbCommand]";
    private HytaleLogger logger;

    public OrbCommand(HytaleLogger logger) {
        super("orb-command", "Primeiro comando! Primeiro mod! WOW!");

        this.logger = logger;
        this.logger.getSubLogger(SUB_LOGGER);
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        this.logger.at(Level.INFO).log("Executando comando");
        commandContext.sendMessage(Message.raw("HELLO WORLD!"));
    }
}
