package Notorious.command.impl;

import java.util.List;

import Notorious.Client;
import Notorious.command.CommandExecutor;
import Notorious.utils.Console;
import net.minecraft.client.entity.EntityPlayerSP;

public class Config implements CommandExecutor {

	@Override
	public void execute(EntityPlayerSP sender, List<String> args) {
		if (args.size() >= 1) {
            String upperCaseFunction = args.get(0).toUpperCase();
            
            if (args.size() == 2) {
                switch (upperCaseFunction) {
                    case "LOAD":
                        if (Client.instance.configManager.loadConfig(args.get(1)))
                        	Console.sendChatToPlayerWithPrefix("Successfully loaded config: '" + args.get(1) + "'");
                        else
                        	Console.sendChatToPlayerWithPrefix("Failed to load config: '" + args.get(1) + "'");
                        break;
                    case "SAVE":
                        if (Client.instance.configManager.saveConfig(args.get(1)))
                        	Console.sendChatToPlayerWithPrefix("Successfully saved config: '" + args.get(1) + "'");
                        else
                        	Console.sendChatToPlayerWithPrefix("Failed to save config: '" + args.get(1) + "'");
                        break;
                    case "DELETE":
                        if (Client.instance.configManager.deleteConfig(args.get(1)))
                        	Console.sendChatToPlayerWithPrefix("Successfully deleted config: '" + args.get(1) + "'");
                        else
                        	Console.sendChatToPlayerWithPrefix("Failed to delete config: '" + args.get(1) + "'");
                        break;
                }
            } else if (args.size() == 1 && upperCaseFunction.equalsIgnoreCase("LIST")) {
                if(Client.instance.configManager.getContents().size() == 0) {
                	Console.sendChatToPlayerWithPrefix("No configs available!");
                }else {
                	Console.sendChatToPlayerWithPrefix("--Available Configs--");
                    for (Notorious.config.Config config : Client.instance.configManager.getContents())
                    	Console.sendChatToPlayerWithPrefix(config.getName());
                }
            }
        }
	}

	
	
}
