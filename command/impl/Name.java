package Notorious.command.impl;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

import Notorious.command.CommandExecutor;
import Notorious.utils.Console;
import net.minecraft.client.entity.EntityPlayerSP;

public class Name implements CommandExecutor{

	@Override
	public void execute(EntityPlayerSP sender, List<String> args) {
		String username = mc.thePlayer.getName();
		StringSelection stringSelection = new StringSelection(username);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
		
		Console.sendChatToPlayerWithPrefix("Copied username to clipboard.");
	}
	
}
