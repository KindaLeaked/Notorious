package Notorious.module.modules.render;

import Notorious.module.Category;
import Notorious.module.Module;

public class FullBright extends Module {

	public FullBright() {
		super("FullBright", 0, true, Category.RENDER, "Makes the world bright");
	}
	
	@Override
	public void onEnable() {
		mc.gameSettings.gammaSetting = 100;
	}
	
	@Override
	public void onDisable() {
		mc.gameSettings.gammaSetting = 1;
	}

}
