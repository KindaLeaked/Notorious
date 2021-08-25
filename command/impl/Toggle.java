package Notorious.command.impl;

import java.util.List;

import Notorious.Client;
import Notorious.command.CommandExecutor;
import Notorious.module.Module;
import Notorious.utils.Console;
import net.minecraft.client.entity.EntityPlayerSP;

public class Toggle implements CommandExecutor {

	@Override
	public void execute(EntityPlayerSP sender, List<String> args) {
		if (args.size() == 1) {
			try {
				System.out.println(args.get(0));
				Module m = Client.instance.moduleManager.getModuleByName(args.get(0));
				if (args.get(0).equalsIgnoreCase(m.getName()))
					m.toggle();
				Console.sendChatToPlayerWithPrefix(
						m.getName() + (m.isEnabled() ? " has been \u00A72enabled" : " has been \u00A74disabled"));
			} catch (Exception e) {
				Console.sendChatToPlayerWithPrefix("Module not found.");
			}
		}else
			Console.sendChatToPlayerWithPrefix(".toggle (module)");
	}

}
