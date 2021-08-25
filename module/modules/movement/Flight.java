package Notorious.module.modules.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.event.Event;
import Notorious.event.events.EventMotion;
import Notorious.event.events.EventPacket;
import Notorious.module.Category;
import Notorious.module.Module;
import Notorious.utils.MovementUtil;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Flight extends Module {

	public int posY;

	public Flight() {
		super("Flight", Keyboard.KEY_G, true, Category.MOVEMENT, "Allows you to fly!");
		Client.instance.settingsManager.rSetting(new Setting("FlySpeed", "Speed", this, 0.5, 0.1, 4, false));
		ArrayList<String> options = new ArrayList<>();
		options.add("Vanilla");
		options.add("Smooth");
		Client.instance.settingsManager.rSetting(new Setting("FlyMode", "Mode", this, "Vanilla", options));
	}

	public float getMaxFallDist() {
		PotionEffect potioneffect = mc.thePlayer.getActivePotionEffect(Potion.jump);
		int f = potioneffect != null ? (potioneffect.getAmplifier() + 1) : 0;
		return mc.thePlayer.getMaxFallHeight() + f;
	}

	@Override
	public void onUpdate() {
		if (Client.instance.settingsManager.getSettingByName("FlyMode").getValString().equalsIgnoreCase("Vanilla")) {

			mc.thePlayer.onGround = true;
			MovementUtil.setSpeed(Client.instance.settingsManager.getSettingByName("FlySpeed").getValDouble());
			mc.thePlayer.capabilities.isFlying = false;
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.thePlayer.motionY = +Client.instance.settingsManager.getSettingByName("FlySpeed").getValDouble();
			} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.thePlayer.motionY = -Client.instance.settingsManager.getSettingByName("FlySpeed").getValDouble();
			} else if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()
					|| mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
				mc.thePlayer.motionY = 0;
			} else {
				mc.thePlayer.motionY = 0;
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
			}
		}

		if (Client.instance.settingsManager.getSettingByName("FlyMode").getValString().equalsIgnoreCase("Smooth")) {
			mc.thePlayer.capabilities.isFlying = true;
			mc.thePlayer.capabilities.setFlySpeed(
					(float) (Client.instance.settingsManager.getSettingByName("FlySpeed").getValDouble() / 20));
		}
	}

	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1;
		mc.thePlayer.capabilities.isFlying = false;
	}

}
