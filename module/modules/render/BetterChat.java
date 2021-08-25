package Notorious.module.modules.render;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.module.Category;
import Notorious.module.Module;

public class BetterChat extends Module {
	
	public static boolean shouldAniDown = false;

	public BetterChat() {
		super("BetterChat", 0, true, Category.RENDER, "Makes the chat gui better and more customizable!");
		Client.instance.settingsManager.rSetting(new Setting("ChatAni", "Open Animation", this, false));
		Client.instance.settingsManager.rSetting(new Setting("ChatAniTime", "Animation Seconds", this, 1, 1, 5, true));
	}

}
