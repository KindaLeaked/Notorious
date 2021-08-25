package Notorious.command.impl;

import java.util.List;

import org.lwjgl.input.Keyboard;

import Notorious.Client;
import Notorious.command.CommandExecutor;
import Notorious.module.Module;
import Notorious.utils.Console;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumChatFormatting;

public class Bind implements CommandExecutor {

	@Override
	public void execute(EntityPlayerSP sender, List<String> args) {
		if (args.size() == 3) {
            Module m = Client.instance.moduleManager.getModuleByName(args.get(1));
            if (args.get(0).equalsIgnoreCase("add") && m != null) {
                m.setKey(Keyboard.getKeyIndex(args.get(2).toUpperCase()));
                Console.sendChatToPlayerWithPrefix(m.getName() + " has been bound to " + Keyboard.getKeyName(m.getKey()) + ".");
            }else
            	sendSyntax();
        } else if (args.size() == 2) {
            Module m = Client.instance.moduleManager.getModuleByName(args.get(1));
            if (args.get(0).equalsIgnoreCase("del") && m != null) {
                m.setKey(Keyboard.KEY_NONE);
                Console.sendChatToPlayerWithPrefix("Unbound " + m.getName() + ".");
            }else
            	sendSyntax();
        } else if (args.size() == 1) {
            if (args.get(0).equalsIgnoreCase("clear")) {
                Client.instance.moduleManager.modules.forEach(module -> module.setKey(Keyboard.KEY_NONE));
                Console.sendChatToPlayerWithPrefix("All binds have been cleared.");
            }else
            	sendSyntax();
        }else
        	sendSyntax();
	}
	
	public void sendSyntax() {
		Console.sendChatToPlayerWithPrefix(EnumChatFormatting.RED + "Invalid Syntax! " + EnumChatFormatting.WHITE + "- " + EnumChatFormatting.BLUE + ".bind add/del/clear (module) (key)");
		Console.sendChatToPlayerWithPrefix(EnumChatFormatting.YELLOW + "Warning: .bind clear will clear all binds!");
	}

}
