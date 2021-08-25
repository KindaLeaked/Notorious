package Notorious.ClickGui.clickgui.component.components.sub;

import org.lwjgl.opengl.GL11;

import Notorious.Client;
import Notorious.ClickGui.clickgui.component.Component;
import Notorious.ClickGui.clickgui.component.components.Button;
import net.minecraft.client.gui.Gui;

public class VisibleButton extends Component {

	private boolean hovered;
	private Button parent;
	private int offset;
	private int x;
	private int y;
	private Notorious.module.Module mod;

	public VisibleButton(Button button, Notorious.module.Module mod, int offset) {
		this.parent = button;
		this.mod = mod;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	@Override
	public void setOff(int newOff) {
		offset = newOff;
	}

	@Override
	public void renderComponent() {
		Gui.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset,
				parent.parent.getX() + (parent.parent.getWidth() * 1), parent.parent.getY() + offset + 12,
				this.hovered ? 0xFF222222 : 0xFF111111);
		Gui.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + 2,
				parent.parent.getY() + offset + 12, 0xFF111111);
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		Client.instance.font30.drawString("Visible: " + mod.visible, (parent.parent.getX() + 7) * 2,
				(parent.parent.getY() + offset - 4) * 2 + 12, -1, false);
		GL11.glPopMatrix();
	}

	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			mod.visible = (!mod.visible);
		}
	}

	public boolean isMouseOnButton(int x, int y) {
		if (x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}
}
