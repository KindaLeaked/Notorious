package Notorious.module.modules.player;

import Notorious.module.Category;
import Notorious.module.Module;

public class Phase extends Module {

	public Phase() {
		super("Phase", 0, true, Category.PLAYER, "Allows you to go through blocks!");
	}
	
	@Override
	public void onUpdate() {
		if(mc.gameSettings.keyBindSneak.isKeyDown()) {
			mc.thePlayer.motionY = 0;
		}
	}

}
