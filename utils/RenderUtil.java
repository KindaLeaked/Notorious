package Notorious.utils;

import org.lwjgl.opengl.GL11;

import Notorious.Client;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;

public class RenderUtil {

	public static int mouseX;
	public static int mouseY;

	public static void drawRoundedRect(double x, double y, double width, double height, float cornerRadius) {
		final int slices = 10;

		drawFillRectangle(x + cornerRadius, y, width - 2 * cornerRadius, height);
		drawFillRectangle(x, y + cornerRadius, cornerRadius, height - 2 * cornerRadius);
		drawFillRectangle(x + width - cornerRadius, y + cornerRadius, cornerRadius, height - 2 * cornerRadius);

		drawCirclePart(x + cornerRadius, y + cornerRadius, -MathHelper.PI, -MathHelper.PId2, cornerRadius, slices);
		drawCirclePart(x + cornerRadius, y + height - cornerRadius, -MathHelper.PId2, 0.0F, cornerRadius, slices);

		drawCirclePart(x + width - cornerRadius, y + cornerRadius, MathHelper.PId2, MathHelper.PI, cornerRadius,
				slices);
		drawCirclePart(x + width - cornerRadius, y + height - cornerRadius, 0, MathHelper.PId2, cornerRadius, slices);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GlStateManager.disableBlend();

		// GlStateManager.enableAlpha();
		// GlStateManager.alphaFunc(GL11.GL_NOTEQUAL, 0);
	}

	public static void drawFillRectangle(double x, double y, double width, double height) {
		GlStateManager.enableBlend();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x, y + height);
		GL11.glVertex2d(x + width, y + height);
		GL11.glVertex2d(x + width, y);
		GL11.glVertex2d(x, y);
		GL11.glEnd();
	}

	public static void drawCirclePart(double x, double y, float fromAngle, float toAngle, float radius, int slices) {
		GlStateManager.enableBlend();
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2d(x, y);
		final float increment = (toAngle - fromAngle) / slices;

		for (int i = 0; i <= slices; i++) {
			final float angle = fromAngle + i * increment;

			final float dX = MathHelper.sin(angle);
			final float dY = MathHelper.cos(angle);

			GL11.glVertex2d(x + dX * radius, y + dY * radius);
		}
		GL11.glEnd();
	}

	public static void color(int color) {
		float red = (color & 255) / 255f, green = (color >> 8 & 255) / 255f, blue = (color >> 16 & 255) / 255f,
				alpha = (color >> 24 & 255) / 255f;

		GlStateManager.color(red, green, blue, alpha);
	}

	public static void colorRGBA(int color) {
		float a = (float) (color >> 24 & 255) / 255.0F;
		float r = (float) (color >> 16 & 255) / 255.0F;
		float g = (float) (color >> 8 & 255) / 255.0F;
		float b = (float) (color & 255) / 255.0F;

		GlStateManager.color(r, g, b, a);
	}

	public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
		float f = (float) (startColor >> 24 & 255) / 255.0F;
		float f1 = (float) (startColor >> 16 & 255) / 255.0F;
		float f2 = (float) (startColor >> 8 & 255) / 255.0F;
		float f3 = (float) (startColor & 255) / 255.0F;
		float f4 = (float) (endColor >> 24 & 255) / 255.0F;
		float f5 = (float) (endColor >> 16 & 255) / 255.0F;
		float f6 = (float) (endColor >> 8 & 255) / 255.0F;
		float f7 = (float) (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldrenderer.pos((double) right, (double) top, (double) Gui.zLevel).color(f1, f2, f3, f).endVertex();
		worldrenderer.pos((double) left, (double) top, (double) Gui.zLevel).color(f1, f2, f3, f).endVertex();
		worldrenderer.pos((double) left, (double) bottom, (double) Gui.zLevel).color(f5, f6, f7, f4).endVertex();
		worldrenderer.pos((double) right, (double) bottom, (double) Gui.zLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}

}
