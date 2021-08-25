package Notorious.module.modules.player;

import org.lwjgl.input.Keyboard;

import Notorious.event.Event;
import Notorious.event.events.EventPacket;
import Notorious.module.Category;
import Notorious.module.Module;
import Notorious.utils.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {

	public NoFall() {
		super("NoFall", Keyboard.KEY_N, true, Category.PLAYER, "Prevents fall damage");
	}
	
	public Timer timer = new Timer();

	@Override
	public void onEvent(Event e) {
		if (mc.thePlayer == null)
			return;
		
		if (e instanceof EventPacket && ((EventPacket) e).isSending() && mc.thePlayer.fallDistance > 3) {
            EventPacket event = (EventPacket) e;
            Packet<?> packet = event.getPacket();
            if (packet instanceof C03PacketPlayer) {

                C03PacketPlayer C03 = (C03PacketPlayer) packet;

                if (mc.thePlayer.fallDistance > 2.5f) {
                    C03.setOnGround(true);
                    mc.thePlayer.fallDistance = 0.5f;
                }
            }
        }
		
	}

}
