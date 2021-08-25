package Notorious.module.modules.movement;

import java.util.ArrayList;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.module.Category;
import Notorious.module.Module;
import Notorious.utils.MovementUtil;

public class LongJump extends Module {

	private float air, groundTicks;
	boolean collided, half;
	double motionY;
	private boolean candisable = false;

	public LongJump() {
		super("LongJump", 0, true, Category.MOVEMENT, "Allows you to jump far distances.");
		ArrayList<String> options = new ArrayList<>();
		options.add("Mineplex");
		Client.instance.settingsManager.rSetting(new Setting("LongJumpMode", "Mode", this, "Mineplex", options));
	}

	@Override
	public void onUpdate() {
		if (Client.instance.settingsManager.getSettingByName("LongJumpMode").getValString()
				.equalsIgnoreCase("Mineplex")) {
			if (mc.thePlayer.onGround && candisable) {
				toggle();
				return;
			}

			if (!mc.thePlayer.onGround && !MovementUtil.isOnGround(0.01) && air > 0) {
				air++;
				if (mc.thePlayer.isCollidedVertically) {
					air = 0;
				}
				if (mc.thePlayer.isCollidedHorizontally && !collided) {
					collided = !collided;
				}
				double speed = half ? 0.5 - air / 100 : 0.658 - air / 100;
				mc.thePlayer.motionX = 0;
				mc.thePlayer.motionZ = 0;
				motionY -= 0.04000000000001;
				if (air > 24) {
					motionY -= 0.02;
				}
				if (air == 12) {
					motionY = -0.005;
				}
				if (speed < 0.3)
					speed = 0.3;
				if (collided)
					speed = 0.2873;
				mc.thePlayer.motionY = motionY;
				MovementUtil.setSpeed(speed);
				candisable = true;
			} else {
				candisable = true;
				if (air > 0) {
					air = 0;
				}
			}

			if (mc.thePlayer.onGround && MovementUtil.isOnGround(0.01) && mc.thePlayer.isCollidedVertically
					&& (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0)) {
				double groundspeed = 0;
				collided = mc.thePlayer.isCollidedHorizontally;
				mc.thePlayer.motionX *= groundspeed;
				mc.thePlayer.motionZ *= groundspeed;

				half = mc.thePlayer.posY != (int) mc.thePlayer.posY;
				mc.thePlayer.motionY = 0.4299999;
				air = 1;
				motionY = mc.thePlayer.motionY;
			}
		}
	}

	@Override
	public void onEnable() {
		candisable = false;
	}

}
