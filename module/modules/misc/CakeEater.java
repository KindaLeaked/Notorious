package Notorious.module.modules.misc;

import Notorious.event.Event;
import Notorious.event.events.EventMotion;
import Notorious.module.Category;
import Notorious.module.Module;
import net.minecraft.block.BlockCake;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public class CakeEater extends Module {

	public CakeEater() {
		super("CakeEater", 0, true, Category.MISC, "Eats cake in range of you.");
	}
	
	@Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion) {
            if (e.isPre()) {
                for (int x = -3; x < 3; x++) {
                    for (int y = -3; y < 3; y++) {
                        for (int z = -3; z < 3; z++) {
                            BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y,
                                    mc.thePlayer.posZ + z);

                            if (pos.getBlock() instanceof BlockCake) {

                                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(pos, 1, mc.thePlayer.inventory.getCurrentItem(), 1f, 1f,1f));
                            }
                        }
                    }
                }
            }
        }
    }

}
