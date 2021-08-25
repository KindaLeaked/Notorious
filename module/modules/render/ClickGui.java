package Notorious.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.module.Category;
import Notorious.module.Module;

public class ClickGui extends Module {

	public Notorious.ClickGui.clickgui.ClickGui clickgui;

	public ClickGui() {
		super("ClickGui", Keyboard.KEY_RSHIFT, false, Category.RENDER, "Manage all of your modules");
		java.util.ArrayList<String> options = new ArrayList<>();
		options.add("Normal");
//		options.add("Discord");
		Client.instance.settingsManager.rSetting(new Setting("NormalR", "Red", this, 84, 0, 255, true));
		Client.instance.settingsManager.rSetting(new Setting("NormalG", "Green", this, 98, 0, 255, true));
		Client.instance.settingsManager.rSetting(new Setting("NormalB", "Blue", this, 205, 0, 255, true));
		Client.instance.settingsManager.rSetting(new Setting("NormalA", "Alpha", this, 255, 0, 255, true));

		Client.instance.settingsManager.rSetting(new Setting("CGuiBG", "Background", this, true));

		Client.instance.settingsManager.rSetting(new Setting("CGuiMode", "Style", this, "Normal", options));
	}

	@Override
	public void onEnable() {
		if (this.clickgui == null)
			this.clickgui = new Notorious.ClickGui.clickgui.ClickGui();

		mc.displayGuiScreen(this.clickgui);
		toggle();
		super.onEnable();
	}

	public static Color colllor() {
		return new Color((int) Client.instance.settingsManager.getSettingByName("NormalR").getValDouble(),
				(int) Client.instance.settingsManager.getSettingByName("NormalG").getValDouble(),
				(int) Client.instance.settingsManager.getSettingByName("NormalB").getValDouble(), 
				(int) Client.instance.settingsManager.getSettingByName("NormalA").getValDouble());
	}

	public static int ClickGuiColor() {
		Color temp = colllor();
		int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), temp.getAlpha()).getRGB();
		return color;
	}

}
