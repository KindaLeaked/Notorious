package Notorious.module.modules.player;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.module.Category;
import Notorious.module.Module;
import Notorious.utils.Wrapper;
import net.minecraft.network.play.client.C03PacketPlayer;

public class FastEat extends Module {

	public FastEat() {
		super("FastEat", 0, true, Category.PLAYER, "Allows you to eat food faster.");
		Client.instance.settingsManager.rSetting(new Setting("FEPackets", "Packets", this, 10, 1, 100, true));
	}

	@Override
	public void onUpdate() {
		for (int i = 0; i > Client.instance.settingsManager.getSettingByName("FEPackets").getValDouble(); i++) {
			Wrapper.sendPacket(new C03PacketPlayer());
		}
	}

}
