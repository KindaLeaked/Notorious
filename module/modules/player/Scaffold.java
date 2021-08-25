package Notorious.module.modules.player;

import java.awt.Color;
import java.util.ArrayList;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.event.Event;
import Notorious.event.events.EventMotion;
import Notorious.event.events.EventPacket;
import Notorious.event.events.EventRenderGUI;
import Notorious.module.Category;
import Notorious.module.Module;
import Notorious.utils.MoveUtils;
import Notorious.utils.RenderUtils;
import Notorious.utils.RotationUtils;
import Notorious.utils.ScaffoldUtils;
import Notorious.utils.Timer;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {
	public static boolean swing;

	public static boolean tower = false;

	int lastItem = -1;
	int lastItemBeforeStart = (int) 0f;
	boolean down = false;
	Timer downwardsTimer, towerTimer, slowDownTimer, timer;
	public static BlockCache blockCache, lastBlockCache;
	static Minecraft mc = Minecraft.getMinecraft();
	private ArrayList<Packet> packets = new ArrayList<Packet>();
	private double yPos;

	public static int sessionScaffoldTime = 0;
	private long startTime = 0;

	public static int sessionScaffoldPlaced = 0;

	public Scaffold() {
		super("Scaffold", 0, true, Category.PLAYER, "Places blocks under you while you walk");
		ArrayList<String> options = new ArrayList<>();
		options.add("Hypixel");
		options.add("Mineplex");
		options.add("Dev");
		this.downwardsTimer = new Timer();
		this.towerTimer = new Timer();
		this.slowDownTimer = new Timer();
		this.timer = new Timer();
		Client.instance.settingsManager.rSetting(new Setting("Scaffold Rotation", "Mode", this, "Hypixel", options));
		Client.instance.settingsManager.rSetting(new Setting("Tower", "Tower", this, false));
		Client.instance.settingsManager.rSetting(new Setting("Sprint", "Sprint", this, false));
		Client.instance.settingsManager.rSetting(new Setting("Timer", "Timer", this, 1, 0.5, 5, false));

	}

	@Override
	public void onEnable() {
		startTime = System.currentTimeMillis();
		down = false;
		tower = Client.instance.settingsManager.getSettingByName("Tower").getValBoolean();
		mc.timer.timerSpeed = (float) Client.instance.settingsManager.getSettingByName("Timer").getValDouble();
		RenderUtils.setCustomPitch(mc.thePlayer.rotationPitch);
		this.downwardsTimer.reset();
		this.towerTimer.reset();
		this.slowDownTimer.reset();
		this.timer.reset();
		this.packets.clear();
		lastItemBeforeStart = mc.thePlayer.inventory.currentItem;
	}

	@Override
	public void onDisable() {
		sessionScaffoldTime += -(startTime - System.currentTimeMillis());
		mc.timer.timerSpeed = 1f;
		this.downwardsTimer.reset();
		this.towerTimer.reset();
		this.slowDownTimer.reset();
		this.timer.reset();
		blockCache = null;
		lastBlockCache = null;
		down = false;
		mc.thePlayer.inventory.currentItem = lastItemBeforeStart;
	}

	private boolean placeBlock(final BlockPos pos, final EnumFacing facing) {
		final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				mc.thePlayer.posZ);

		if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, facing,
				new Vec3(blockCache.position).addVector(0.5, 0.5, 0.5)
						.add(new Vec3(blockCache.facing.getDirectionVec()).scale(0.5)))) {
			sessionScaffoldPlaced += 1;
			if (swing) {
				mc.thePlayer.swingItem();
			} else {
				mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
			}
			return true;
		}
		return false;
	}

	private BlockCache grab() {
		final EnumFacing[] invert = { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH,
				EnumFacing.EAST, EnumFacing.WEST };
		BlockPos position = new BlockPos(0, 0, 0);
		if (mc.thePlayer.isMoving() && !mc.gameSettings.keyBindJump.isKeyDown()) {
			for (double n2 = 0.08D, n3 = 0.0; n3 <= n2; n3 += n2 / (Math.floor(n2) + 1.0)) {
				final BlockPos blockPos2 = new BlockPos(
						mc.thePlayer.posX - MathHelper.sin(ScaffoldUtils.clampRotation()) * n3, mc.thePlayer.posY - 1.0,
						mc.thePlayer.posZ + MathHelper.cos(ScaffoldUtils.clampRotation()) * n3);
				final IBlockState blockState = mc.theWorld.getBlockState(blockPos2);
				if (blockState != null && blockState.getBlock() == Blocks.air) {
					position = blockPos2;
					break;
				}
			}
			// position = new BlockPos(new
			// BlockPos(this.mc.thePlayer.getPositionVector().xCoord,
			// this.mc.thePlayer.getPositionVector().yCoord,
			// this.mc.thePlayer.getPositionVector().zCoord)).offset(EnumFacing.DOWN);
		} else {
			position = new BlockPos(new BlockPos(mc.thePlayer.getPositionVector().xCoord,
					mc.thePlayer.getPositionVector().yCoord, mc.thePlayer.getPositionVector().zCoord))
							.offset(EnumFacing.DOWN);
		}

		if (!(mc.theWorld.getBlockState(position).getBlock() instanceof BlockAir)
				&& !(mc.theWorld.getBlockState(position).getBlock() instanceof BlockLiquid)) {
			return null;
		}
		EnumFacing[] values;
		for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
			final EnumFacing facing = values[i];
			final BlockPos offset = position.offset(facing);
			final IBlockState blockState = mc.theWorld.getBlockState(offset);
			if (!(mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir)
					&& !(mc.theWorld.getBlockState(position).getBlock() instanceof BlockLiquid)) {
				return new BlockCache(offset, invert[facing.ordinal()], (BlockCache) null);
			}
		}
		final BlockPos[] offsets = { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1),
				new BlockPos(0, 0, 1) };
		BlockPos[] array;
		for (int length2 = (array = offsets).length, j = 0; j < length2; ++j) {
			final BlockPos offset2 = array[j];
			final BlockPos offsetPos = position.add(offset2.getX(), 0, offset2.getZ());
			final IBlockState blockState2 = mc.theWorld.getBlockState(offsetPos);
			if (mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir) {
				EnumFacing[] values2;
				for (int length3 = (values2 = EnumFacing.values()).length, k = 0; k < length3; ++k) {
					final EnumFacing facing2 = values2[k];
					final BlockPos offset3 = offsetPos.offset(facing2);
					final IBlockState blockState3 = mc.theWorld.getBlockState(offset3);
					if (!(mc.theWorld.getBlockState(offset3).getBlock() instanceof BlockAir)) {
						return new BlockCache(offset3, invert[facing2.ordinal()], (BlockCache) null);
					}
				}
			}

		}
		return null;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPacket) {
			if (((EventPacket) event).getPacket() instanceof C03PacketPlayer) {
				if (((EventPacket) event).isSending()) {
					if (lastBlockCache != null) {
						if (Client.instance.settingsManager.getSettingByName("Scaffold Rotation").getValString()
								.equalsIgnoreCase("Hypixel")) {
//                            float[] rot = RotationUtils.aimAtLocation(lastBlockCache.position, lastBlockCache.facing);
//                            mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw + 0.001f;
//                            mc.thePlayer.rotationPitch = mc.thePlayer.rotationPitch + 0.001f;
//                            RenderUtils.setCustomPitch(pitch);
//                            C03.setYaw(rot[0]);
//                            C03.setPitch((int) rot[1]);
//                            yaw = rot[0];
//                            pitch = rot[1];

						}
						if (Client.instance.settingsManager.getSettingByName("Scaffold Rotation").getValString()
								.equalsIgnoreCase("Dev")) {
							// float[] rotations = ScaffoldUtils.getRotations(lastBlockCache.position,
							// lastBlockCache.facing);
							// mc.thePlayer.rotationYawHead = rotations[0];
							// mc.thePlayer.renderYawOffset = rotations[0];
							// RenderUtils.setCustomPitch(rotations[1]);
							// C03.setYaw(rotations[0]);
							// C03.setPitch(rotations[1]);
						}
						if (Client.instance.settingsManager.getSettingByName("Scaffold Rotation").getValString()
								.equalsIgnoreCase("Mineplex")) {

						}

					} else {
						RenderUtils.setCustomPitch(mc.thePlayer.rotationPitch);
					}
				}
			}
		}

		if (event instanceof EventMotion) {

			if (lastBlockCache != null) {
				if (Client.instance.settingsManager.getSettingByName("Scaffold Rotation").getValString()
						.equalsIgnoreCase("Hypixel")) {
					mc.thePlayer.rotationYawHead = mc.thePlayer.rotationYaw + 180;
					mc.thePlayer.renderYawOffset = mc.thePlayer.rotationYaw + 180;
					RenderUtils.setCustomPitch(90);
					mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw + 0.001f;
					mc.thePlayer.rotationPitch = mc.thePlayer.rotationPitch + 0.001f;
					((EventMotion) event).setYaw(mc.thePlayer.rotationYaw + 180);
					((EventMotion) event).setPitch(90);
				}
				if (Client.instance.settingsManager.getSettingByName("Scaffold Rotation").getValString()
						.equalsIgnoreCase("Dev")) {
					float[] rotations = ScaffoldUtils.getRotations(lastBlockCache.position, lastBlockCache.facing);
					mc.thePlayer.rotationYawHead = rotations[0];
					mc.thePlayer.renderYawOffset = rotations[0];
					RenderUtils.setCustomPitch(rotations[1]);
					mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw + 0.001f;
					mc.thePlayer.rotationPitch = mc.thePlayer.rotationPitch + 0.001f;
					((EventMotion) event).setYaw(rotations[0]);
					((EventMotion) event).setPitch(rotations[1]);
				}
				if (Client.instance.settingsManager.getSettingByName("Scaffold Rotation").getValString()
						.equalsIgnoreCase("Mineplex")) {
					float[] rot = ScaffoldUtils.getRotations(lastBlockCache.position, lastBlockCache.facing);
					mc.thePlayer.rotationYawHead = rot[0];
					mc.thePlayer.renderYawOffset = rot[0];
					RenderUtils.setCustomPitch(rot[1]);
					mc.thePlayer.rotationYaw = mc.thePlayer.rotationYaw + 0.001f;
					mc.thePlayer.rotationPitch = mc.thePlayer.rotationPitch + 0.001f;
					((EventMotion) event).setYaw(rot[0]);
					((EventMotion) event).setPitch(rot[1]);
					if (mc.thePlayer.onGround) {
						if (mc.thePlayer.isSprinting()) {
							mc.thePlayer.motionX *= 0.5;
							mc.thePlayer.motionZ *= 0.5;
						}
					} else {
						mc.thePlayer.motionX *= 0.9;
						mc.thePlayer.motionZ *= 0.9;
					}
					double motionx = mc.thePlayer.motionX;
					double motionz = mc.thePlayer.motionZ;
				}

				// float[] rotations = ScaffoldUtils.getRotations(lastBlockCache.position,
				// lastBlockCache.facing);

				// mc.thePlayer.rotationPitch = 90f;

			}

			// mc.thePlayer.sendQueue.addToSendQueue(new
			// C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY,
			// mc.thePlayer.posZ, mc.thePlayer.getRotationYaw(), 85.0f,
			// mc.thePlayer.onGround));

			if (Client.instance.settingsManager.getSettingByName("Sprint").getValBoolean()) {
				if (mc.thePlayer.moveForward > 0 && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isSneaking()
						&& !mc.thePlayer.isCollidedHorizontally && mc.thePlayer.getFoodStats().getFoodLevel() > 6)
					mc.thePlayer.setSprinting(true);
			} else {
				mc.thePlayer.setSprinting(false);
			}

			if (lastBlockCache != null) {
				// mc.thePlayer.sendQueue.addToSendQueue(new
				// C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY,
				// mc.thePlayer.posZ, rotations[0], mc.thePlayer.getRotationPitch(),
				// mc.thePlayer.onGround));
			}

			if (event.isPre()) {

				if (mc.gameSettings.keyBindSneak.isKeyDown() && !down) {
					mc.thePlayer.setSneaking(false);
					// mc.gameSettings.keyBindSneak.pressed = false;
					down = true;
				}

				if (mc.gameSettings.keyBindSneak.isKeyDown() && down) {
					mc.thePlayer.setSneaking(false);
					// mc.gameSettings.keyBindSneak.pressed = false;
					down = false;
				}

				if (ScaffoldUtils.grabBlockSlot() == -1) {
					return;
				}
				blockCache = this.grab();
				if (blockCache != null) {
					lastBlockCache = this.grab();
				}
				if (blockCache == null) {
					return;
				}
			} else {
				if (blockCache == null)
					return;

				if (mc.gameSettings.keyBindJump.isKeyDown() && !mc.thePlayer.isMoving() && tower) {
					if (MoveUtils.getJumpEffect() == 0) {
						mc.thePlayer.motionY = 0;
					}

					mc.thePlayer.motionX = 0.0;
					mc.thePlayer.motionZ = 0.0;
					mc.thePlayer.isJumping = false;
					mc.thePlayer.setJumping(false);

					if (towerTimer.hasReached(100)) {
						if (MoveUtils.getJumpEffect() == 0) {
							mc.thePlayer.jump();

							if (slowDownTimer.delay(1500)) {
								mc.thePlayer.motionY = -0.28;
								slowDownTimer.reset();
							}
						}
						towerTimer.reset();
					}
				} else {
					towerTimer.reset();
				}

				final int slot = ScaffoldUtils.grabBlockSlot();
				int time = 30;

				if (MoveUtils.getSpeedEffect() > 0) {
					time = time / (MoveUtils.getSpeedEffect() * 8);
				}
				int blockCount = getBlockCount();
				if (timer.hasReached(time) && blockCount != 0) {
					mc.thePlayer.inventory.currentItem = slot;
					if (this.placeBlock(blockCache.position, blockCache.facing)) {

						blockCache = null;
					}
					timer.reset();
				}
			}
		}

		if (event instanceof EventRenderGUI)

		{
			ScaledResolution sr = new ScaledResolution(mc);
			int blockCount = getBlockCount();
			Color color = new Color(0, 255, 0);
			if (this.getBlockCount() > 100) {
				color = new Color(0, 255, 0);
			}
			if (this.getBlockCount() < 64) {
				color = new Color(255, 255, 0);
			}
			if (this.getBlockCount() < 32) {
				color = new Color(255, 0, 0);
			}

			Client.instance.font24.drawString(blockCount + "",
					(sr.getScaledWidth() / 2 - -10) - mc.fontRendererObj.getStringWidth(blockCount + "") / 2,
					(sr.getScaledHeight() / 2 + 30) + -21, color.getRGB(), true);
		}
	}

	private class BlockCache {
		private BlockPos position;
		private EnumFacing facing;

		private BlockCache(final BlockPos position, final EnumFacing facing, BlockCache blockCache) {
			this.position = position;
			this.facing = facing;
		}

		private BlockPos getPosition() {
			return this.position;
		}

		private EnumFacing getFacing() {
			return this.facing;
		}

	}

	public int getBlockCount() {
		int blockCount = 0;
		for (int i = 0; i < 45; ++i) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				final Item item = is.getItem();
				if (is.getItem() instanceof ItemBlock && canIItemBePlaced(item)) {
					blockCount += is.stackSize;
				}
			}
		}
		return blockCount;
	}

	private boolean canIItemBePlaced(Item item) {
		if (Item.getIdFromItem(item) == 116)
			return false;
		if (Item.getIdFromItem(item) == 30)
			return false;
		if (Item.getIdFromItem(item) == 31)
			return false;
		if (Item.getIdFromItem(item) == 175)
			return false;
		if (Item.getIdFromItem(item) == 28)
			return false;
		if (Item.getIdFromItem(item) == 27)
			return false;
		if (Item.getIdFromItem(item) == 66)
			return false;
		if (Item.getIdFromItem(item) == 157)
			return false;
		if (Item.getIdFromItem(item) == 31)
			return false;
		if (Item.getIdFromItem(item) == 6)
			return false;
		if (Item.getIdFromItem(item) == 31)
			return false;
		if (Item.getIdFromItem(item) == 32)
			return false;
		if (Item.getIdFromItem(item) == 140)
			return false;
		if (Item.getIdFromItem(item) == 390)
			return false;
		if (Item.getIdFromItem(item) == 37)
			return false;
		if (Item.getIdFromItem(item) == 38)
			return false;
		if (Item.getIdFromItem(item) == 39)
			return false;
		if (Item.getIdFromItem(item) == 40)
			return false;
		if (Item.getIdFromItem(item) == 69)
			return false;
		if (Item.getIdFromItem(item) == 50)
			return false;
		if (Item.getIdFromItem(item) == 75)
			return false;
		if (Item.getIdFromItem(item) == 76)
			return false;
		if (Item.getIdFromItem(item) == 54)
			return false;
		if (Item.getIdFromItem(item) == 130)
			return false;
		if (Item.getIdFromItem(item) == 146)
			return false;
		if (Item.getIdFromItem(item) == 342)
			return false;
		if (Item.getIdFromItem(item) == 12)
			return false;
		if (Item.getIdFromItem(item) == 77)
			return false;
		if (Item.getIdFromItem(item) == 143)
			return false;
		return true;
	}

	public static float[] getRotations(BlockPos block, EnumFacing face) {
		double x = block.getX() + 0.5 - mc.thePlayer.posX + (double) face.getFrontOffsetX() / 2;
		double z = block.getZ() + 0.5 - mc.thePlayer.posZ + (double) face.getFrontOffsetZ() / 2;
		double y = (block.getY() + 0.5);
		double d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y;
		double d3 = MathHelper.sqrt_double(x * x + z * z);
		float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);
		if (yaw < 0.0F) {
			yaw += 360f;
		}
		return new float[] { yaw, pitch };
	}

}
