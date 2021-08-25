package Notorious.module.modules.render;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import Notorious.Client;
import Notorious.ClickGui.settings.Setting;
import Notorious.event.events.EventRenderGUI;
import Notorious.module.Category;
import Notorious.module.Module;
import Notorious.ui.animations.Direction;
import Notorious.ui.animations.impl.DecelerateAnimation;
import Notorious.utils.ColorManager;
import Notorious.utils.RenderUtil;
import Notorious.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class HUD extends Module {
	long time = 0;

	long lastPacket = 0;

	public static DecelerateAnimation ani1;
	public static DecelerateAnimation ani2;
	public static DecelerateAnimation ani3;

	public HUD() {
		super("HUD", 0, false, Category.RENDER, "The HUD that displays everything about the client.");
		Client.instance.settingsManager.rSetting(new Setting("HUDLogo", "Watermark Logo", this, true));
		Client.instance.settingsManager.rSetting(new Setting("HUDExtrasRect", "Extras Box", this, true));
		Client.instance.settingsManager.rSetting(new Setting("HUDFPS", "FPS Counter", this, true));
		Client.instance.settingsManager.rSetting(new Setting("HUDBPS", "BPS Counter", this, true));
		this.enabled = true;
	}

	public DecelerateAnimation ani = new DecelerateAnimation(2000, 50);

	public Timer timer = new Timer();

	public int fps = 0;

	int arrayColor1 = 0;

	public Minecraft mc = Minecraft.getMinecraft();
	public FontRenderer fr = mc.fontRendererObj;
	public ScaledResolution sr = new ScaledResolution(mc);

	public int color = 0;

	public List<Long> packets = new ArrayList<Long>();

	public Module lastModule = null;

	public static int fpsY = Minecraft.getMinecraft().displayHeight / 2 - 16;

	public static int displayheight = Minecraft.getMinecraft().displayHeight / 2 - 16;

	@Override
	public void onUpdate() {
		displayheight = Minecraft.getMinecraft().displayHeight / 2 - 16;
	}

	public static int rainbow(float seconds, float saturation, float brightness) {
		float hue = (System.currentTimeMillis() % (int) (seconds * 1000)) / (float) (seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return color;
	}

	public static int offsetRainbow(float seconds, float saturation, float brightness, long offset) {
		float hue = ((System.currentTimeMillis()
				+ (Client.instance.settingsManager.getSettingByName("ArrayDirMode").getValString().equals("Up") ? offset
						: -offset))
				% (int) (seconds * 1000)) / (float) (seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, brightness);

		return color;
	}

	public static class ModuleComparator implements Comparator<Module> {

		@Override
		public int compare(Module args0, Module args1) {
			if (Client.instance.font20Consolas.getStringWidth(args0.displayName) > Client.instance.font20Consolas
					.getStringWidth(args1.displayName)) {
				return -1;
			}
			if (Client.instance.font20Consolas.getStringWidth(args0.displayName) < Client.instance.font20Consolas
					.getStringWidth(args1.displayName)) {
				return 1;
			}
			return 0;
		}

	}

	public int smoothAnimation(double current, double last) {
		return (int) (current * mc.timer.renderPartialTicks + (last * (1.0f - mc.timer.renderPartialTicks)));
	}

	public float smoothTrans(double current, double last) {
		return (float) (current * mc.timer.renderPartialTicks + (last * (1.0f - mc.timer.renderPartialTicks)));
	}

	public int getArrayAlphaColor() {
		return new Color(0, 0, 0, (int) Client.instance.settingsManager.getSettingByName("ArrayA").getValDouble())
				.getRGB();
	}

	public void render() {
		if (mc.gameSettings.showDebugInfo)
			return;

		if (ani2 != null) {
			fpsY = Minecraft.getMinecraft().displayHeight / 2 - 16 - (int) ani2.getOutput();
		} else {
			fpsY = Minecraft.getMinecraft().displayHeight / 2 - 16;
		}
		if (ani3 != null) {
			GuiIngame.anivar = Minecraft.getMinecraft().displayHeight / 2 - 27 - (int) ani3.getOutput();
		} else {
			GuiIngame.anivar = Minecraft.getMinecraft().displayHeight / 2 - 27;
		}

		Collections.sort(Client.instance.moduleManager.modules, new ModuleComparator());

		int count = 0;
		for (Module m : Client.instance.moduleManager.getEnabledModules()) {
			if (!m.visible)
				continue;

			arrayColor1 = new Color((int) Client.instance.settingsManager.getSettingByName("ArrayRed").getValDouble(),
					(int) Client.instance.settingsManager.getSettingByName("ArrayGreen").getValDouble(),
					(int) Client.instance.settingsManager.getSettingByName("ArrayBlue").getValDouble(), 255).getRGB();

			if (Client.instance.settingsManager.getSettingByName("ArrayRainbow").getValBoolean()) {
				color = offsetRainbow(4, 0.8f, 1, count * 150L);
			} else {
				color = ColorManager.fadeBetween(arrayColor1, ColorManager.darker(arrayColor1, 0.5f),
						((System.currentTimeMillis() + (count * 100)) % 1000 / (1000 / 2.0F)));
			}

			double offset = count * (Client.instance.font20.getFontHeight() - 1);

			if (Client.instance.settingsManager.getSettingByName("ArrayBG").getValBoolean()) {
				Gui.drawRect((int) (mc.displayWidth / 2 - (Client.instance.font20Consolas.getStringWidth(m.displayName))
						- 4), offset, mc.displayWidth / 2 - 0, 6 + 9 + offset - 3, getArrayAlphaColor());
			}

			ArrayList<Module> toggledModules = new ArrayList<Module>(Client.instance.moduleManager.modules);
			toggledModules.removeIf(module -> !module.isEnabled() || module.getName().equals("TabGUI")
					|| module.getName().equals("HUD") || module.getName().equals("ArrayList"));
			int toggledIndex = toggledModules.indexOf(m);
			int m1Offset = 0;
			if (toggledIndex != toggledModules.size() - 1) {
				m1Offset += Client.instance.font20Consolas
						.getStringWidth(toggledModules.get(toggledIndex + 1).displayName.toLowerCase());
				m1Offset += 5;
			}

			String modulesText = m.displayName;

			GlStateManager.color(1, 1, 1, 1);
			if (Client.instance.settingsManager.getSettingByName("ArrayOutline").getValBoolean()) {
				Gui.drawRect(mc.displayWidth / 2 - Client.instance.font20Consolas.getStringWidth(modulesText) - 5,
						offset - 1,
						mc.displayWidth / 2 - Client.instance.font20Consolas.getStringWidth(modulesText) - 4,
						Client.instance.font20Consolas.getFontHeight() + offset, color);
				Gui.drawRect(mc.displayWidth / 2 - Client.instance.font20Consolas.getStringWidth(modulesText) - 4,
						offset + Client.instance.font20Consolas.getFontHeight() - 1, mc.displayWidth / 2 - m1Offset,
						offset + Client.instance.font20Consolas.getFontHeight(), color);
			}

			Client.instance.font20Consolas.drawString(m.displayName,
					mc.displayWidth / 2 - Client.instance.font20Consolas.getStringWidth(m.displayName) - 2,
					(float) offset, color, false);

			lastModule = m;

			count++;
		}

		Client.instance.onEvent(new EventRenderGUI(sr));

		// Extra

		if (Client.instance.settingsManager.getSettingByName("HUDExtrasRect").getValBoolean()) {
			if (Client.instance.settingsManager.getSettingByName("HUDFPS").getValBoolean()
					&& !Client.instance.settingsManager.getSettingByName("HUDBPS").getValBoolean()) {
				RenderUtil.color(0x40000000);

				RenderUtil.drawRoundedRect(7, fpsY - 5,
						Client.instance.font20.getStringWidth("FPS: " + mc.getDebugFPS()) + 12, 15, 5);

//				Gui.drawRect(7, fpsY + 2, Client.instance.font20.getStringWidth("FPS: " + mc.getDebugFPS()) + 12,
//						fpsY + 12, 0x40000000);
			} else {
				if (Client.instance.settingsManager.getSettingByName("HUDBPS").getValBoolean()
						&& !Client.instance.settingsManager.getSettingByName("HUDFPS").getValBoolean()) {
					double speed = Math.sqrt(Math.pow(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, 2)
							+ Math.pow(mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ, 2));
					speed = Math.round(speed * 100);
					speed /= 10;
					speed *= 2;
					speed *= mc.timer.timerSpeed;
					DecimalFormat df = new DecimalFormat("###.#");
					speed = Double.parseDouble(df.format(speed));
					String s = String.valueOf(speed);
					RenderUtil.color(0x40000000);

					RenderUtil.drawRoundedRect(7, fpsY - 5, Client.instance.font20.getStringWidth("BPS: " + s) + 11, 15,
							5);
//					Gui.drawRect(7, fpsY + 1, Client.instance.font20.getStringWidth("BPS: " + s) + 11, fpsY + 12,
//							0x40000000);
				}
			}

			if (Client.instance.settingsManager.getSettingByName("HUDFPS").getValBoolean()
					&& Client.instance.settingsManager.getSettingByName("HUDBPS").getValBoolean()) {

				double speed = Math.sqrt(Math.pow(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, 2)
						+ Math.pow(mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ, 2));
				speed = Math.round(speed * 100);
				speed /= 10;
				speed *= 2;
				speed *= mc.timer.timerSpeed;
				DecimalFormat df = new DecimalFormat("###.#");
				speed = Double.parseDouble(df.format(speed));
				String s = String.valueOf(speed);

				if (Client.instance.font20.getStringWidth("BPS: " + s) > Client.instance.font20
						.getStringWidth("FPS: " + mc.getDebugFPS())) {
					RenderUtil.color(0x40000000);

					RenderUtil.drawRoundedRect(7, (fpsY + 2) - 14,
							Client.instance.font20.getStringWidth("BPS: " + s) + 11, 22, 5);

//					Gui.drawRect(7, (fpsY + 2) - 10, Client.instance.font20.getStringWidth("BPS: " + s) + 11, fpsY + 12,
//							0x40000000);
				} else {
					RenderUtil.color(0x40000000);

					RenderUtil.drawRoundedRect(7, (fpsY + 2) - 14,
							Client.instance.font20.getStringWidth("FPS: " + mc.getDebugFPS()) + 12, 22, 5);

//					Gui.drawRect(7, (fpsY + 2) - 9,
//							Client.instance.font20.getStringWidth("FPS: " + mc.getDebugFPS()) + 12, fpsY + 12,
//							0x40000000);
				}

			}

		}

		if (Client.instance.settingsManager.getSettingByName("HUDFPS").getValBoolean()) {
			fps = mc.getDebugFPS();
			Client.instance.font20.drawString(EnumChatFormatting.BLUE + "FPS: " + EnumChatFormatting.WHITE + fps, 8,
					fpsY - 4, -1, false);
		}

		if (Client.instance.settingsManager.getSettingByName("HUDBPS").getValBoolean()) {
			double speed = Math.sqrt(Math.pow(mc.thePlayer.posX - mc.thePlayer.lastTickPosX, 2)
					+ Math.pow(mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ, 2));
			speed = Math.round(speed * 100);
			speed /= 10;
			speed *= 2;
			speed *= mc.timer.timerSpeed;
			DecimalFormat df = new DecimalFormat("###.#");
			speed = Double.parseDouble(df.format(speed));
			String s = String.valueOf(speed);

			Client.instance.font20.drawString(EnumChatFormatting.BLUE + "BPS: " + EnumChatFormatting.WHITE + s, 8,
					Client.instance.settingsManager.getSettingByName("HUDFPS").getValBoolean() ? fpsY - 13 : fpsY - 4,
					-1, false);
		}

		if (Client.instance.settingsManager.getSettingByName("HUDLogo").getValBoolean()) {
			mc.getTextureManager().bindTexture(new ResourceLocation("DemoWare/NotoriousLogo.png"));
			GlStateManager.pushMatrix();
			GlStateManager.scale(0.2, 0.2, 0.2);
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.enableBlend();
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			Gui.drawModalRectWithCustomSizedTexture(-20, -20, 1, 1, 512, 512, 512, 512);
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}

	}

	public static void onChatInit() {
		ani1 = new DecelerateAnimation(
				(int) Client.instance.settingsManager.getSettingByName("ChatAniTime").getValDouble() * 1000,
				Minecraft.getMinecraft().displayWidth / 2 - 2, Direction.FORWARDS);
		ani2 = new DecelerateAnimation(500, 12, Direction.FORWARDS);
		ani3 = new DecelerateAnimation(500, 12, Direction.FORWARDS);
		ani2.reset();
		ani3.reset();
		ani1.reset();
	}

	public static void onChatClose() {
		ani2 = new DecelerateAnimation(500, 10, Direction.BACKWARDS);
		ani3 = new DecelerateAnimation(500, 10, Direction.BACKWARDS);
		ani2.reset();
		ani3.reset();
//		ani2.changeDirection();
//		ani3.changeDirection();
	}

}