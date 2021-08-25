package Notorious.module.modules.render;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.module.Category;
import Notorious.module.Module;

public class ArrayList extends Module {

    public ArrayList() {
        super("ArrayList", 0, false, Category.RENDER, "The module list on the HUD");
    }

    @Override
    public void setup() {
//        Client.instance.settingsManager.rSetting(new Setting("ArrayScale", "Scale", this, 2, 1, 10, false));
        Client.instance.settingsManager.rSetting(new Setting("ArrayBG", "Background", this, true));
        Client.instance.settingsManager.rSetting(new Setting("ArrayA", "Saturation", this, 162, 0, 255, true));
        Client.instance.settingsManager.rSetting(new Setting("ArrayOutline", "Outline", this, true));
//        Client.instance.settingsManager.rSetting(new Setting("ArrayBar", "Bar", this, true));
        java.util.ArrayList<String> options = new java.util.ArrayList<>();
        options.add("Up");
        options.add("Down");
        Client.instance.settingsManager.rSetting(new Setting("ArrayDirMode", "Direction", this, "Down", options));
        Client.instance.settingsManager.rSetting(new Setting("ArrayRainbow", "Rainbow", this, false));
        Client.instance.settingsManager.rSetting(new Setting("ArrayRed", "Red", this, 78, 0, 255, true));
        Client.instance.settingsManager.rSetting(new Setting("ArrayGreen", "Green", this, 202, 0, 255, true));
        Client.instance.settingsManager.rSetting(new Setting("ArrayBlue", "Blue", this, 255, 0, 255, true));
    }

    @Override
    public void onEnable() {
        this.enabled = false;

    }
}
