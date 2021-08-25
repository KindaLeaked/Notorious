package Notorious.module.modules.render;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.module.Category;
import Notorious.module.Module;

public class Animations extends Module {

	public Animations() {
		super("Animations", 0, true, Category.RENDER, "Choose different sword blocking animations!");
		java.util.ArrayList<String> options = new java.util.ArrayList<>();
		options.add("Exhibition");
		options.add("Slide");
		options.add("Astro");
		options.add("Spin");
		options.add("Sigma");
		options.add("Tap");
		options.add("Remix");
		options.add("Ambitious");
		Client.instance.settingsManager.rSetting(new Setting("AnimationMode", "Style", this, "Exhibition", options));
		Client.instance.settingsManager.rSetting(new Setting("AnimationScale", "Sword Scale", this, 1, 0.1, 2, false));
	}

}
