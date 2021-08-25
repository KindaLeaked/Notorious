package Notorious.module.modules.render;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.module.Category;
import Notorious.module.Module;
import java.util.ArrayList;

public class Cape extends Module {

	public Cape() {
		super("Cape", 0, true, Category.RENDER, "Custom cape design");
		ArrayList<String> options = new ArrayList<>();
		options.add("Notorious");
		Client.instance.settingsManager.rSetting(new Setting("CapeImg", "Design", this, "Notorious", options));
	}

}
