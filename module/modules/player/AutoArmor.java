package Notorious.module.modules.player;

import Notorious.event.Event;
import Notorious.event.events.EventUpdate;
import Notorious.module.Category;
import Notorious.module.Module;
import Notorious.utils.InventoryUtils;
import Notorious.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;

public class AutoArmor extends Module {

	public static boolean isEquiping = false;

	public AutoArmor() {
		super("AutoArmor", 0, true, Category.PLAYER, "Automatically equips the best armor");
	}

	public transient Timer timer = new Timer();

	@Override
	public void onEvent(Event e) {

		if (e instanceof EventUpdate && e.isPre()) {

			if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory
					|| mc.currentScreen instanceof GuiChat) {
				// if(timer.hasTimeElapsed(500, false)){
				// getBestArmor();
				// }
				getBestArmor();
			}
		}

	}
	
	public static double getProtectionValue(ItemStack stack) {
        return !(stack.getItem() instanceof ItemArmor) ? 0.0D : (double) ((ItemArmor) stack.getItem()).damageReduceAmount + (double) ((100 - ((ItemArmor) stack.getItem()).damageReduceAmount * 4) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 4) * 0.0075D;
    }

	public void getBestArmor() {
		for (int type = 1; type < 5; type++) {
			if (mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
				ItemStack item = mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
				if (isBestArmor(item, type)) {
					continue;
				} else {
					if (!(mc.currentScreen instanceof GuiInventory)) {
						timer.reset();
						C16PacketClientStatus p = new C16PacketClientStatus(EnumState.OPEN_INVENTORY_ACHIEVEMENT);
						mc.thePlayer.sendQueue.addToSendQueue(p);
					}
					InventoryUtils.drop(4 + type);
				}
			}
			for (int i = 9; i < 45; i++) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
					ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
					if (isBestArmor(is, type) && getProtection(is) > 0) {
						InventoryUtils.shiftClick(i);
					}
				}
			}
		}
	}

	public static boolean isBestArmor(ItemStack stack, int type) {
		float prot = getProtection(stack);
		String strType = "";
		if (type == 1) {
			strType = "helmet";
		} else if (type == 2) {
			strType = "chestplate";
		} else if (type == 3) {
			strType = "leggings";
		} else if (type == 4) {
			strType = "boots";
		}
		if (!stack.getUnlocalizedName().contains(strType)) {
			return false;
		}
		for (int i = 5; i < 45; i++) {
			if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getProtection(is) > prot && is.getUnlocalizedName().contains(strType))
					return false;
			}
		}
		return true;
	}

	public static float getProtection(ItemStack stack) {
		float prot = 0;
		if ((stack.getItem() instanceof ItemArmor)) {
			ItemArmor armor = (ItemArmor) stack.getItem();
			prot += armor.damageReduceAmount + (100 - armor.damageReduceAmount)
					* EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.0075D;
			prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100d;
			prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100d;
			prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100d;
			prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50d;
			prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) / 100d;
		}
		return prot;
	}

}