package Notorious.module.modules.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.event.Event;
import Notorious.event.events.EventMotion;
import Notorious.event.events.EventRenderGUI;
import Notorious.module.Category;
import Notorious.module.Module;
import Notorious.utils.MathUtils;
import Notorious.utils.Timer;
import Notorious.utils.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class KillAura extends Module {

	public Timer timer = new Timer();
	public static EntityLivingBase target;
	private float[] serverAngles = new float[2];

	ArrayList<Entity> InvalidEntitiesToRemove = new ArrayList<>();

	public KillAura() {
		super("KillAura", Keyboard.KEY_R, true, Category.COMBAT, "Attacks players in range.");
		Client.instance.settingsManager.rSetting(new Setting("KillAuraRange", "Range", this, 3.5, 1, 10, false));
		Client.instance.settingsManager.rSetting(new Setting("KillAuraNoSwing", "NoSwing", this, false));
		Client.instance.settingsManager.rSetting(new Setting("KillAuraAutoBlock", "Auto Block", this, false));
	}

	@Override
	public void onEnable() {
		timer.reset();
	}

	private float[] smoothAngle(float[] dst, float[] src) {
		float[] smoothedAngle = new float[2];
		smoothedAngle[0] = (src[0] - dst[0]);
		smoothedAngle[1] = (src[1] - dst[1]);
		smoothedAngle = MathUtils.constrainAngle(smoothedAngle);
		smoothedAngle[0] = (src[0] - smoothedAngle[0] / 40 * MathUtils.getRandomInRange(1, 24));
		smoothedAngle[1] = (src[1] - smoothedAngle[1] / 40 * MathUtils.getRandomInRange(3, 8));
		return smoothedAngle;
	}

	private float[] getRotationsToEnt(Entity ent, EntityPlayerSP playerSP) {
		final double differenceX = ent.posX - playerSP.posX;
		final double differenceY = (ent.posY + ent.height) - (playerSP.posY + playerSP.height);
		final double differenceZ = ent.posZ - playerSP.posZ;
		final float rotationYaw = (float) (Math.atan2(differenceZ, differenceX) * 180.0D / Math.PI) - 90.0f;
		final float rotationPitch = (float) (Math.atan2(differenceY, playerSP.getDistanceToEntity(ent)) * 180.0D
				/ Math.PI);
		final float finishedYaw = playerSP.rotationYaw
				+ MathHelper.wrapAngleTo180_float(rotationYaw - playerSP.rotationYaw);
		final float finishedPitch = playerSP.rotationPitch
				+ MathHelper.wrapAngleTo180_float(rotationPitch - playerSP.rotationPitch);
		return new float[] { finishedYaw, -finishedPitch };
	}

	@Override
	public void onEvent(Event e) {
		if (mc.currentScreen instanceof GuiChest)
			return;

		if (e instanceof EventMotion && e.isPre()) {

			List<Entity> targets = (List<Entity>) mc.theWorld.loadedEntityList.stream().filter(Entity.class::isInstance)
					.collect(Collectors.toList());

			targets = targets.stream()
					.filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < Client.instance.settingsManager
							.getSettingByName("KillAuraRange").getValDouble() && entity != mc.thePlayer)
					.collect(Collectors.toList());

			targets.sort(Comparator.comparingDouble(entity -> ((Entity) entity).getDistanceToEntity(mc.thePlayer)));

			targets = targets.stream().filter(Entity.class::isInstance).collect(Collectors.toList());

			Iterator<Entity> iter = targets.iterator();
			while (iter.hasNext()) {
				if (!isValid(iter.next())) {
					iter.remove();
				}
			}

			if (!targets.isEmpty()) {
				Entity target = targets.get(0);

				if (!Client.instance.friendManager.isFriend(target.getName())) {

					if (!target.isValid())
						return;

					final float[] dstAngle = getRotationsToEnt(target, mc.thePlayer);
					final float[] srcAngle = new float[] { serverAngles[0], serverAngles[1] };
					serverAngles = smoothAngle(dstAngle, srcAngle);
					mc.thePlayer.renderYawOffset = serverAngles[0];
					mc.thePlayer.rotationYawHead = serverAngles[0];
					mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw + 0.001f;
					mc.thePlayer.rotationPitch = mc.thePlayer.rotationPitch + 0.001f;
					((EventMotion) e).setYaw(serverAngles[0]);
					((EventMotion) e).setPitch(serverAngles[1]);
					block();
					if (timer.hasTimeElapsed((long) (1000 / 200), true)) {

						if (Client.instance.settingsManager.getSettingByName("KillAuraNoSwing").getValBoolean()) {
							Wrapper.sendPacket(new C0APacketAnimation());
						} else {
							mc.thePlayer.swingItem();
						}

						mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, Action.ATTACK));
					}

					if (!(mc.thePlayer.inventory.getCurrentItem() == null) && true) {
						if (mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
							mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), 20);
						}
					}
				}
			} else {
				unBlock();
			}

		}

		if (e instanceof EventRenderGUI) {
			if (Client.instance.moduleManager.getModuleByName("TargetHUD").isEnabled()) {
				List<Entity> targets = (List<Entity>) mc.theWorld.loadedEntityList.stream().filter(Objects::nonNull)
						.collect(Collectors.toList());
				targets = targets.stream()
						.filter(entity -> entity.getDistanceToEntity(mc.thePlayer) < Client.instance.settingsManager
								.getSettingByName("KillAuraRange").getValDouble() && entity != mc.thePlayer)
						.collect(Collectors.toList());
				targets.sort(Comparator.comparingDouble(entity -> ((Entity) entity).getDistanceToEntity(mc.thePlayer)));

				targets = targets.stream().filter(Entity.class::isInstance).collect(Collectors.toList());

				// Dont do anything if there is no targets
				if (targets.isEmpty())
					return;

				// the target that is displayed
				target = (EntityLivingBase) targets.get(0);

				// Max Health
				double maxHealth = 130.0f / this.target.getMaxHealth();

				// Render code
				if (!target.isInvisibleToPlayer(mc.thePlayer) && !target.getName().contains("?")) {
					Gui.drawRect(Client.instance.settingsManager.getSettingByName("TargetHUDX").getValDouble(),
							Client.instance.settingsManager.getSettingByName("TargetHUDY").getValDouble(),
							Client.instance.settingsManager.getSettingByName("TargetHUDX").getValDouble()
									+ (int) (target.getHealth() * maxHealth) + 3,
							Client.instance.settingsManager.getSettingByName("TargetHUDY").getValDouble() + 2,
							getColorByHealth(target.getHealth()));
					Gui.drawRect(Client.instance.settingsManager.getSettingByName("TargetHUDX").getValDouble(),
							Client.instance.settingsManager.getSettingByName("TargetHUDY").getValDouble(),
							Client.instance.settingsManager.getSettingByName("TargetHUDX").getValDouble() + 133,
							Client.instance.settingsManager.getSettingByName("TargetHUDY").getValDouble() - 40,
							-1879048192);
					Client.instance.font26.drawString(target.getName(),
							(float) (Client.instance.settingsManager.getSettingByName("TargetHUDX").getValDouble() + 4),
							(float) (Client.instance.settingsManager.getSettingByName("TargetHUDY").getValDouble()
									- 38),
							-1, false);
					Client.instance.font30.drawString(
							String.valueOf((int) target.getHealth()) + EnumChatFormatting.WHITE + " HP",
							((int) Client.instance.settingsManager.getSettingByName("TargetHUDX").getValDouble() + 2),
							((int) Client.instance.settingsManager.getSettingByName("TargetHUDY").getValDouble() - 15),
							getColorByHealth(target.getHealth()), false);
					// FontUtil.arrayList.drawString(getItemName(target.getHeldItem()),
					// Client.settingsManager.getSettingByName("TargetHUDX").getValDouble() + 35,
					// (float) (Client.settingsManager.getSettingByName("TargetHUDY").getValDouble()
					// - 25), -1);
				}
			}
		}

	}

	public int getColorByHealth(double health) {
		if (health > 15) {
			return 0xff33ff00; // Green
		} else if (health >= 10) {
			return 0xffccff00; // Yellow
		} else {
			return 0xffff2600; // Red
		}
	}

	private void unBlock() {
		if (!Client.instance.settingsManager.getSettingByName("KillAuraAutoBlock").getValBoolean())
			return;

		mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
				new BlockPos(-1, -1, -1), EnumFacing.DOWN));
	}

	private void block() {
		if (!Client.instance.settingsManager.getSettingByName("KillAuraAutoBlock").getValBoolean())
			return;

		double value = -1;
		mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(value, value, value), 255,
				mc.thePlayer.inventory.getCurrentItem(), 0, 0, 0));
	}

	public boolean isValid(Entity entity) {
		if (!(entity instanceof EntityLivingBase)) {
			return false;
		}

		if (entity.isDead) {
			return false;
		}

		return true;
	}

}
