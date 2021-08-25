package Notorious.command.impl;

import java.util.List;

import Notorious.command.CommandExecutor;
import Notorious.module.modules.render.Xray;
import Notorious.utils.Console;
import Notorious.utils.XRayUtils;
import net.minecraft.client.entity.EntityPlayerSP;

public class rx implements CommandExecutor {

	@Override
	public void execute(EntityPlayerSP sender, List<String> args) {
		Xray.xrayBlocks.clear();
		XRayUtils.initXRayBlocks();
		mc.renderGlobal.loadRenderers();
		Console.sendChatToPlayerWithPrefix("Reloaded Xray blocks!");
	}

}
