package Notorious.module.modules.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.event.Event;
import Notorious.event.events.EventPacket;
import Notorious.event.events.EventUpdate;
import Notorious.module.Category;
import Notorious.module.Module;
import Notorious.module.modules.player.Scaffold;
import Notorious.utils.MoveUtils;
import Notorious.utils.PlayerUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Speed extends Module {

	private double mineplex = 0, stage;

	public Speed() {
		super("Speed", Keyboard.KEY_V, true, Category.MOVEMENT, "Makes you go zoom!");
		Client.instance.settingsManager.rSetting(new Setting("SpeedVal", "Speed", this, 0.5, 0.1, 1, false));
		ArrayList<String> options = new ArrayList<>();
		options.add("Mineplex");
		Client.instance.settingsManager.rSetting(new Setting("SpeedMode", "Mode", this, "Mineplex", options));
	}

//	@Override
//	public void onUpdate() {
//		if(Client.instance.settingsManager.getSettingByName("SpeedMode").getValString().equalsIgnoreCase("Mineplex")) {
//			if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()
//					|| mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
//				if (mc.thePlayer.onGround) {
//					mc.thePlayer.jump();
//				}
//			}
//
//			MovementUtil.setSpeed(Client.instance.settingsManager.getSettingByName("SpeedVal").getValDouble());
//		}
//	}

	@Override
	public void onEnable() {
//		Scaffold.keepY = (int) mc.thePlayer.posY;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate) {
			EventUpdate em = (EventUpdate) event;
			double speed = 0.15;
			if (mc.thePlayer.isCollidedHorizontally || !PlayerUtil.isMoving2()) {
				mineplex = -2;
			}
			if (MoveUtils.isOnGround(0.001) && PlayerUtil.isMoving2()) {
				stage = 0;
				mc.thePlayer.motionY = 0.42;
				if (mineplex < 0)
					mineplex++;
//            		if(mc.thePlayer.posY != (int)mc.thePlayer.posY){
//            			mineplex = -1;
//            		}
				mc.timer.timerSpeed = 2.001f;
			} else {
				if (mc.timer.timerSpeed == 2.001f)
					mc.timer.timerSpeed = 1;
				speed = 0.45 - stage / 300 + mineplex / 5;
				stage++;

			}
			MoveUtils.setMotion(speed);
		} else if (event instanceof EventPacket) {
			EventPacket ep = (EventPacket) event;
			Packet p = ep.getPacket();
			if (p instanceof S08PacketPlayerPosLook) {
				mineplex = -2;
			}
		}
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1;
	}

}
