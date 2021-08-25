package Notorious.module.modules.misc;

import java.util.ArrayList;

import Notorious.event.Event;
import Notorious.event.events.EventPacket;
import Notorious.module.Category;
import Notorious.module.Module;
import Notorious.utils.Console;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class ChatBypass extends Module {

	public ChatBypass() {
		super("ChatBypass", 0, true, Category.MISC, "Bypass servers chat moderation.");
	}

	@Override
	public void onEvent(Event e) {
		if (mc.thePlayer == null)
			return;
		if (e instanceof EventPacket && ((EventPacket) e).isSending()) {
			EventPacket event = (EventPacket) e;
			Packet<?> packet = event.getPacket();
			if (packet instanceof C01PacketChatMessage) {

				C01PacketChatMessage p = (C01PacketChatMessage) event.getPacket();

				if (p.getMessage().startsWith("/")) {
					return;
				}

				String finalmsg = "";
				ArrayList<String> splitshit = new ArrayList<>();
				String[] msg = p.getMessage().split(" ");

				for (int i = 0; i < msg.length; i++) {
					char[] characters = msg[i].toCharArray();
					for (int i2 = 0; i2 < characters.length; i2++) {
						splitshit.add(characters[i2] + "Ö�");
					}
					splitshit.add(" ");
				}
				for (int i = 0; i < splitshit.size(); i++) {
					finalmsg += splitshit.get(i);
				}
				p.setMessage(finalmsg.replaceFirst("%", ""));
				splitshit.clear();
			}
		}
	}

	@Override
	public void onEnable() {
		Console.sendChatToPlayerWithPrefix("Warning! Commands and captchas will not work with ChatBypass enabled!");
	}

}
