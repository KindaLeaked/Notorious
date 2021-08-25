package Notorious.command.impl;

import java.util.List;

import Notorious.command.CommandExecutor;
import Notorious.command.CommandManager;
import Notorious.utils.Console;
import net.minecraft.client.entity.EntityPlayerSP;


public class Help implements CommandExecutor {

    @Override
    public void execute(EntityPlayerSP sender, List<String> args) {
        CommandManager.getCommands().forEach(command -> Console.sendChatToPlayerWithPrefix(command.getName() + " - " + command.getDesc()));
    }
}
