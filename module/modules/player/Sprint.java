package Notorious.module.modules.player;

import Notorious.module.Category;
import Notorious.module.Module;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", 0, true, Category.PLAYER, "Automatically sprints for you.");
	}

	@Override
	public void onUpdate() {
		if ((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown()
				|| mc.gameSettings.keyBindRight.isKeyDown()) && !mc.thePlayer.isCollidedHorizontally) {
			mc.thePlayer.setSprinting(true);
		}
	}

	@Override
	public void onDisable() {
		mc.thePlayer.setSprinting(mc.thePlayer.isSprinting());
	}

}
