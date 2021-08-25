package Notorious.ClickGui.clickgui;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.io.IOException;
import java.math.BigDecimal;

import org.lwjgl.opengl.GL11;

import Notorious.Client;
import Notorious.ClickGui.clickgui.component.Component;
import Notorious.ClickGui.clickgui.component.Frame;
import Notorious.ClickGui.settings.Setting;
import Notorious.module.Category;
import Notorious.module.Module;
import Notorious.notification.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen {

	public static ArrayList<Frame> frames;
	public static int color = Notorious.module.modules.render.ClickGui.ClickGuiColor();
	private ArrayList<Component> subcomponents;

	public ArrayList<Module> modulesToRender = new ArrayList();
	public ArrayList<Setting> settingsToRender = new ArrayList();

	private double renderWidth;
	int opY = -4;
	int y;
	int x;
	boolean hovered = false;
	boolean dragging = false;

	public ClickGui() {
		this.frames = new ArrayList<Frame>();
		int frameX = 5;
		for (Category category : Category.values()) {
			Frame frame = new Frame(category);
			frame.setX(frameX);
			frames.add(frame);
			frameX += frame.getWidth() + 4;
			frame.setOpen(true);
		}
	}

	@Override
	public void initGui() {
	}

	public String catName = "";
	public int catY2 = 0;

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		color = Notorious.module.modules.render.ClickGui.ClickGuiColor();
		if (Client.instance.settingsManager.getSettingByName("CGuiBG").getValBoolean()) {
			this.drawDefaultBackground();
		}
		if (Client.instance.settingsManager.getSettingByName("CGuiMode").getValString().equals("Normal")) {
			for (Frame frame : reverseArrayList(frames)) {
				frame.renderFrame(this.fontRendererObj);
				frame.updatePosition(mouseX, mouseY);
				for (Component comp : frame.getComponents()) {
					comp.updateComponent(mouseX, mouseY);
				}
			}
		}

		if (Client.instance.settingsManager.getSettingByName("CGuiMode").getValString().equals("Discord")) {
			Gui.drawRect(25, 100, 350, 400, 0xff36393F);
			Gui.drawRect(25, 100, 100, 400, 0xff202225);

			for (Frame frame : reverseArrayList(frames)) {
				frame.renderFrame(this.fontRendererObj);
				frame.updatePosition(mouseX, mouseY);
				for (Component comp : frame.getComponents()) {
					comp.updateComponent(mouseX, mouseY);
				}
			}

			int offset = 0;
			for (Category c : Category.values()) {
				Client.instance.font28.drawString(c.name(), 28, 170 + (26 * offset),
						(isMouseOnButton(mouseX, mouseY, c.name(), 170 + (26 * offset))) ? 0xffc7c3c3 : -1, false);
				offset++;
			}

			int modoffset2 = 0;
			for (Module mtr : modulesToRender) {
				if (isMouseOnModuleButton(mouseX, mouseY, mtr.getName(), 104 + (1 + modoffset2 * 12))) {
					Client.instance.font24.drawString(mtr.getName(), 105, 104 + (1 + modoffset2 * 12),
							mtr.isEnabled() ? 0xff3dbf6b : 0xffbf483d, false);
				} else {
					Client.instance.font24.drawString(mtr.getName(), 105, 104 + (1 + modoffset2 * 12),
							mtr.isEnabled() ? 0xff52ff8f : 0xffff6052, false);
				}
				modoffset2++;
			}

			int setoffset = 0;
			int setoffsetAmount = 20;
			for (Setting s : settingsToRender) {
				if (s.isCombo()) { //
//					Gui.drawRect(231 + 2, 107 + opY, 231 + (20 * 1), 107 + opY + 12, this.isMouseOnCheck(mouseX, mouseY, opY) ? 0xFF222222 : 0xFF111111);
//					Gui.drawRect(231, 107 + opY, 231 + 2, 107 + opY + 12, 0xFF111111);
					GL11.glPushMatrix();
					GL11.glScalef(0.5f, 0.5f, 0.5f);
					Client.instance.font40.drawString(s.getDisplayName() + ": " + s.getValString(), (231 + 7) * 2,
							(107 + setoffset - 2) * 2 + 9, -1, false);
					GL11.glPopMatrix();
					setoffset += setoffsetAmount;
				}
				if (s.isSlider()) {
					// Gui.drawRect(231 + 2, 107 + setoffset, 318, 107 + 0 + 12, this.hovered ?
					// 0xFF222222 : 0xff36393F);
					final int drag = (int) (s.getValDouble() / s.getMax() * 50);
					Gui.drawRect(231 + 2, 107 + setoffset, 231 + (int) renderWidth, 107 + setoffset + 12,
							this.hovered ? Color.BLUE.color : 0xFF555555);
					GL11.glPushMatrix();
					GL11.glScalef(0.5f, 0.5f, 0.5f);
					Client.instance.font40.drawString(s.getDisplayName() + ": " + s.getValDouble(), (231 * 2 + 15),
							(107 + setoffset - 2) * 2 + 9, -1, false);
					GL11.glPopMatrix();

					this.hovered = isMouseOnButtonD(mouseX, mouseY, setoffset) || isMouseOnButtonI(mouseX, mouseY);
					this.y = 107 + opY;
					this.x = 231;

					double diff = Math.min(88, Math.max(0, mouseX - this.x));

					double min = s.getMin();
					double max = s.getMax();

					renderWidth = (88) * (s.getValDouble() - min) / (max - min);

					if (dragging) {
						if (diff == 0) {
							s.setValDouble(s.getMin());
						} else {
							double newValue = roundToPlace(((diff / 88) * (max - min) + min), 2);
							s.setValDouble(newValue);
						}
					}

					setoffset += setoffsetAmount;
				}
				if (s.isCheck()) {

					setoffset += setoffsetAmount;
				}

				// this.subcomponents.add(new Keybind(this, opY));
				// this.subcomponents.add(new VisibleButton(this, m, opY));
			}

			for (Setting s : settingsToRender) {
				if (s.isSlider()) {

				}
			}

		}
		// System.out.println(mouseX + " " + mouseY);
	}

	public boolean isMouseOnButtonD(int x, int y, int yval) {
		if (x > 232 && x < 232 + 87 && y > yval && y < yval + 12) {
			return true;
		}
		return false;
	}

	public boolean isMouseOnButtonI(int x, int y) {
		if (x > this.x + 50 / 2 && x < this.x + 50 && y > this.y && y < this.y + 12) {
			return true;
		}
		return false;
	}

	private static double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public boolean isMouseOnButton(int x, int y, String nameofCategory, int catY) {
		if ((x > 28) && x < 28 + Client.instance.font28.getStringWidth(nameofCategory) && y > (catY)
				&& y < catY + 12 + 0) {
			return true;
		}

		return false;
	}

	public boolean isMouseOnModuleButton(int x, int y, String nameofModule, int modY) {
		if ((x > 104) && x < 104 + Client.instance.font28.getStringWidth(nameofModule) && y > (modY)
				&& y < modY + 12 + 0) {
			return true;
		}

		return false;
	}

	public ArrayList<Frame> reverseArrayList(ArrayList<Frame> alist) {
		// Arraylist for storing reversed elements
		ArrayList<Frame> revArrayList = new ArrayList<Frame>();
		for (int i = alist.size() - 1; i >= 0; i--) {

			// Append the elements in reverse order
			revArrayList.add(alist.get(i));
		}

		// Return the reversed arraylist
		return revArrayList;
	}

	@Override
	protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
		for (Frame frame : frames) {
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
				frame.setDrag(true);
				frame.dragX = mouseX - frame.getX();
				frame.dragY = mouseY - frame.getY();
			}
			if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
				frame.setOpen(!frame.isOpen());
			}
			if (frame.isOpen()) {
				if (!frame.getComponents().isEmpty()) {
					for (Component component : frame.getComponents()) {
						component.mouseClicked(mouseX, mouseY, mouseButton);
					}
				}
			}
		}

//		if(mouseButton == 0) {
//			int offset2 = 0;
//			for (Category c : Category.values()) {
//				if (isMouseOnButton(mouseX, mouseY, c.name(), 170 + (26 * offset2))) {
//					int modOffset = 0;
//					modulesToRender.clear();
//					for (Module m : Client.instance.moduleManager.getModulesInCategory(c)) {
//						modulesToRender.add(m);
//						modOffset++;
//					}
//				}
//				offset2++;
//			}
//
//			int modoffset3 = 0;
//			for (Module m : modulesToRender) {
//				if (isMouseOnModuleButton(mouseX, mouseY, m.getName(), 104 + (1 + modoffset3 * 12))) {
//					m.toggle();
//				}
//				modoffset3++;
//			}
//		}
//		if(mouseButton == 1) {
//			int modoffset3 = 0;
//			for (Module m : modulesToRender) {
//				if (isMouseOnModuleButton(mouseX, mouseY, m.getName(), 104 + (1 + modoffset3 * 12))) {
//					int opY = 0;
//					settingsToRender.clear();
//					if(Client.instance.settingsManager.getSettingsByMod(m) != null) {
//						for(Setting s : Client.instance.settingsManager.getSettingsByMod(m)){
//							if(s.isCombo()){
//								settingsToRender.add(s);
//								opY += 12;
//							}
//							if(s.isSlider()){
//								settingsToRender.add(s);
//								opY += 12;
//							}
//							if(s.isCheck()){
//								settingsToRender.add(s);
//								opY += 12;
//							}
//						}
//					}
//					//this.subcomponents.add(new Keybind(this, opY));
//					//this.subcomponents.add(new VisibleButton(this, m, opY));
//				}
//				modoffset3++;
//			}
//		}
//		
//		if(mouseButton == 0) {
//			int opY = 0;
//			for(Setting s : settingsToRender){
//				if(s.isCombo()){                            
//					
//					opY += 12;
//				}
//				if(s.isSlider()){
//					if(isMouseOnButtonD(mouseX, mouseY, opY)) {
//						dragging = true;
//					}
//					opY += 12;
//				}
//				if(s.isCheck()){
//					
//					opY += 12;
//				}
//			}
//			//this.subcomponents.add(new Keybind(this, opY));
//			//this.subcomponents.add(new VisibleButton(this, m, opY));
//		}

	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		for (Frame frame : frames) {
			if (frame.isOpen() && keyCode != 1) {
				if (!frame.getComponents().isEmpty()) {
					for (Component component : frame.getComponents()) {
						component.keyTyped(typedChar, keyCode);
					}
				}
			}
		}
		if (keyCode == 1) {
			this.mc.displayGuiScreen(null);
		}
	}

	public boolean isMouseOnCheck(int x, int y, int boxY) {
		if (x > 231 && x < 231 + 88 && y > boxY && y < boxY + 12) {
			return true;
		}
		return false;
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		dragging = false;
		for (Frame frame : frames) {
			frame.setDrag(false);
		}
		for (Frame frame : frames) {
			if (frame.isOpen()) {
				if (!frame.getComponents().isEmpty()) {
					for (Component component : frame.getComponents()) {
						component.mouseReleased(mouseX, mouseY, state);
					}
				}
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}
