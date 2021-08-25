package Notorious.module.modules.player;

import Notorious.module.Category;
import Notorious.module.Module;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;

public class Parkour extends Module {

	public Parkour() {
		super("Parkour", 0, true, Category.PLAYER, "Jumps at the edge of a block.");
	}
	
	@Override
	public void onUpdate() {
		if(mc.thePlayer == null)
			return;
		BlockPos at = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 2, mc.thePlayer.posZ);
		if(mc.theWorld.getBlockState(at.add(0, 1, 0)).getBlock() instanceof BlockAir && mc.thePlayer.onGround) {
			mc.thePlayer.jump();
		}
	}

}
