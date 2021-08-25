package Notorious.module.modules.render;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.module.Category;
import Notorious.module.Module;

public class TargetHUD extends Module {

    public TargetHUD() {
        super("TargetHUD", 0, true, Category.RENDER, "Displays information about the aura target");
        Client.instance.settingsManager.rSetting(new Setting("TargetHUDX", "X", this, mc.displayWidth / 2, 1, mc.displayWidth, true));
        Client.instance.settingsManager.rSetting(new Setting("TargetHUDY", "Y", this, mc.displayHeight / 2 - 40, 1, mc.displayHeight, true));
    }

    //Code in aura class

}
