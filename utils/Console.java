package Notorious.utils;

import Notorious.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class Console {

	//A custom util used to print.
	
	public static Minecraft mc = Minecraft.getMinecraft();
	
	public static void print(String message) {
		System.out.println(message);
	}
	
	public static void printWithPrefix(String message) {
		System.out.println(Client.instance.prefix + " " + message); //You can change 'Client.instance.prefix' to your client name.
	}
	
	public static void sendChatToPlayer(String message) {
		mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(message));
	}
	
	public static void sendChatToPlayerWithPrefix(String message) {
		mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + "[" + EnumChatFormatting.BLUE + Client.instance.name + EnumChatFormatting.AQUA + "] " + message));
	}
	
	public static void sendChat(String message) {
		mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
	}
	
}
