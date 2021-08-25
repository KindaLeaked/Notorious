package Notorious.ClickGui.clickgui.component;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import Notorious.Client;
import Notorious.ClickGui.clickgui.ClickGui;
import Notorious.ClickGui.clickgui.component.components.Button;
import Notorious.module.Category;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class Frame {

	public ArrayList<Component> components;
	public Category category;
	private boolean open;
	private int width;
	private int y;
	private int x;
	private int barHeight;
	private boolean isDragging;
	public int dragX;
	public int dragY;

	public Frame(Category cat) {
		this.components = new ArrayList<Component>();
		this.category = cat;
		this.width = 100;
		this.x = 5;
		this.y = 5;
		this.barHeight = 16;
		this.dragX = 0;
		this.open = false;
		this.isDragging = false;
		int tY = this.barHeight;

		/**
		 * public ArrayList<Module> getModulesInCategory(Category categoryIn){
		 * ArrayList<Module> mods = new ArrayList<Module>(); for(Module m :
		 * this.modules){ if(m.getCategory() == categoryIn) mods.add(m); } return mods;
		 * }
		 */

		for (Notorious.module.Module mod : Client.instance.moduleManager.getModulesInCategory(category)) {
			Button modButton = new Button(mod, this, tY);
			this.components.add(modButton);
			tY += 11;
		}
	}

	public ArrayList<Component> getComponents() {
		return components;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public static int rainbow(float seconds, float saturation, float brightness) {
		float hue = (System.currentTimeMillis() % (int) (seconds * 1000)) / (float) (seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return color;
	}

	public void renderFrame(FontRenderer fontRenderer) {
		Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, ClickGui.color);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		Client.instance.font36.drawString(this.category.name(), (this.x + 2) * 2 + 5, (this.y - 2) * 2 + 10, 0xFFFFFFFF,
				false);
		Client.instance.font56.drawString(this.open ? "-" : "+", (this.x + this.width - 10) * 2 + 3,
				(this.y - 2.5f) * 2 + 2, -1, false);
		GL11.glPopMatrix();
		if (this.open) {
			if (!this.components.isEmpty()) {
				// Gui.drawRect(this.x, this.y + this.barHeight, this.x + 1, this.y +
				// this.barHeight + (12 * components.size()), rainbow(6, 1, 8f));
//				Gui.drawRect(this.x, this.y + this.barHeight + (12 * components.size()), this.x + this.width, this.y + this.barHeight + (12 * components.size()) + 1, rainbow(6, 1, 1));
				// Gui.drawRect(this.x + this.width, this.y + this.barHeight, this.x +
				// this.width - 1, this.y + this.barHeight + (12 * components.size()), new
				// Color(0, 200, 20, 150).getRGB());
				for (Component component : components) {
					component.renderComponent();
				}
			}
		}
	}

	public void refresh() {
		int off = this.barHeight;
		for (Component comp : components) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public void updatePosition(int mouseX, int mouseY) {
		if (this.isDragging) {
			this.setX(mouseX - dragX);
			this.setY(mouseY - dragY);
		}
	}

	public boolean isWithinHeader(int x, int y) {
		if (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight) {
			return true;
		}
		return false;
	}

}
