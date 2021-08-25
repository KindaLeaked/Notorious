package Notorious.module.modules.player;

import Notorious.event.Event;
import Notorious.event.events.EventUpdate;
import Notorious.module.Category;
import Notorious.module.Module;
import net.minecraft.block.BlockStairs;
import net.minecraft.util.BlockPos;

public class FastStair extends Module {

	public FastStair() {
		super("FastStair", 0, true, Category.PLAYER, "Go up stairs fast.");
	}
	
	@Override
	public void onDisable() {
		mc.gameSettings.keyBindSneak.pressed = false;
	}
	
	@Override
	public void onEvent(Event e) {
		if(e instanceof EventUpdate) {
	        BlockPos at = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ);
			if(mc.theWorld.getBlockState(at.add(0, 1, 0)).getBlock() instanceof BlockStairs) {
				if(mc.thePlayer.onGround && mc.thePlayer.isMoving()) {
					mc.thePlayer.jump();
				}
			}
		}
	}

}
