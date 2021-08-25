package Notorious.ClickGui.clickgui.component.components;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import Notorious.Client;
import Notorious.ClickGui.clickgui.ClickGui;
import Notorious.ClickGui.clickgui.component.Component;
import Notorious.ClickGui.clickgui.component.Frame;
import Notorious.ClickGui.clickgui.component.components.sub.Checkbox;
import Notorious.ClickGui.clickgui.component.components.sub.Keybind;
import Notorious.ClickGui.clickgui.component.components.sub.ModeButton;
import Notorious.ClickGui.clickgui.component.components.sub.Slider;
import Notorious.ClickGui.clickgui.component.components.sub.VisibleButton;
import Notorious.ClickGui.settings.Setting;
import Notorious.ui.animations.Direction;
import Notorious.ui.animations.impl.DecelerateAnimation;
import Notorious.utils.RenderUtil;
import net.minecraft.client.gui.Gui;

public class Button extends Component {

	public Notorious.module.Module mod;
	public Frame parent;
	public int offset;
	private boolean isHovered;
	private ArrayList<Component> subcomponents;
	public boolean open;
	private int height;
	DecelerateAnimation ani;
	String lastModule;

	public Button(Notorious.module.Module mod2, Frame parent, int offset) {
		this.mod = mod2;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<Component>();
		this.open = false;
		height = 12;
		int opY = offset + 12;
		if (Client.instance.settingsManager.getSettingsByMod(mod2) != null) {
			for (Setting s : Client.instance.settingsManager.getSettingsByMod(mod2)) {
				if (s.getDisplayName().equalsIgnoreCase("Dev") && !Client.instance.isDev) {
					return;
				}
				if (s.isCombo()) {
					this.subcomponents.add(new ModeButton(s, this, mod2, opY));
					opY += 12;
				}
				if (s.isSlider()) {
					this.subcomponents.add(new Slider(s, this, opY));
					opY += 12;
				}
				if (s.isCheck()) {
					this.subcomponents.add(new Checkbox(s, this, opY));
					opY += 12;
				}
			}
		}
		this.subcomponents.add(new Keybind(this, opY));
		this.subcomponents.add(new VisibleButton(this, mod2, opY));
	}

	@Override
	public void setOff(int newOff) {
		offset = newOff;
		int opY = offset + 12;
		for (Component comp : this.subcomponents) {
			comp.setOff(opY);
			opY += 12;
		}
	}

	@Override
	public void renderComponent() {

		Gui.drawRect(parent.getX(), this.parent.getY() + this.offset, parent.getX() + parent.getWidth(),
				this.parent.getY() + 12 + this.offset,
				this.isHovered ? (0xFF222222) : (new Color(14, 14, 14).getRGB()));
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		Client.instance.font30.drawString(this.mod.getName(), (parent.getX() + 2) * 2, (parent.getY() + offset + 2) * 2,
				this.mod.isEnabled() ? 0x0099ff : -1, false);
		if (this.subcomponents.size() > 2) {

			Client.instance.font40.drawString(this.open ? "-" : "+", (parent.getX() + parent.getWidth() - 10) * 2,
					(parent.getY() + offset + 4) * 2 - 10, -1, false);
		}
		GL11.glPopMatrix();
		if (this.open) {
			if (!this.subcomponents.isEmpty()) {
				for (Component comp : this.subcomponents) {
					comp.renderComponent();
				}
				Gui.drawRect(parent.getX() + 2, parent.getY() + this.offset + 12, parent.getX() + 3,
						parent.getY() + this.offset + ((this.subcomponents.size() + 1) * 12), ClickGui.color);
			}
		}
	}

	@Override
	public int getHeight() {
		if (this.open) {
			return (11 * (this.subcomponents.size() + 1));
		}
		return 11;
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		RenderUtil.mouseX = mouseX;
		RenderUtil.mouseY = mouseY;
		this.isHovered = isMouseOnButton(mouseX, mouseY);
		if (!this.subcomponents.isEmpty()) {
			for (Component comp : this.subcomponents) {
				comp.updateComponent(mouseX, mouseY);
			}
		}

		if (this.isHovered) {
			ani = new DecelerateAnimation(300, mouseX + Client.instance.font20.getStringWidth(this.mod.desc) + 4,
					Direction.FORWARDS);
			RenderUtil.drawGradientRect(mouseX, mouseY - 14, (int) ani.getOutput(),
					mouseY + Client.instance.font20.getFontHeight() - 14, 0xff2a6df8, 0xff641bcf);
			Client.instance.font20.drawString(this.mod.desc, mouseX, mouseY - 15, -1, false);
//			System.out.println(ani.getOutput());
			if (this.mod.desc != lastModule) {
				System.out.println(lastModule);
			}
		}

		if (isMouseOnButton(mouseX, mouseY)) {
			if (lastModule != this.mod.desc) {
				System.out.println("YES");
			}
			lastModule = this.mod.desc;
		}

	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (isMouseOnButton(mouseX, mouseY) && button == 0) {
			this.mod.toggle();
		}
		if (isMouseOnButton(mouseX, mouseY) && button == 1) {
			this.open = !this.open;
			this.parent.refresh();
		}
		for (Component comp : this.subcomponents) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for (Component comp : this.subcomponents) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void keyTyped(char typedChar, int key) {
		for (Component comp : this.subcomponents) {
			comp.keyTyped(typedChar, key);
		}
	}

	public boolean isMouseOnButton(int x, int y) {
		if (x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset
				&& y < this.parent.getY() + 12 + this.offset) {
			return true;
		}
		return false;
	}
}
