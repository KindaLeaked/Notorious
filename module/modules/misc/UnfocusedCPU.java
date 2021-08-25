package Notorious.module.modules.misc;

import org.lwjgl.opengl.Display;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.module.Category;
import Notorious.module.Module;

public class UnfocusedCPU extends Module {

	public UnfocusedCPU() {
		super("UnfocusedCPU", 0, true, Category.MISC, "Limits your fps when the window is unfocused");
		Client.instance.settingsManager.rSetting(new Setting("UnCPULimitFPS", "FPS limit", this, 4, 1, 60, true));
	}
	
	public int lastFPS;
	
	@Override
	public void onUpdate() {
		if(!Display.isActive()) {
			mc.setLimitFramerate((int) Client.instance.settingsManager.getSettingByName("UnCPULimitFPS").getValDouble());
		}else {
			mc.setLimitFramerate(lastFPS);
		}
	}
	
	@Override
	public void onEnable() {
		lastFPS = mc.getLimitFramerate();
	}
	
	@Override
	public void onDisable() {
		mc.setLimitFramerate(lastFPS);
	}

}
