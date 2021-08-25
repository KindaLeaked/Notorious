package Notorious.event.events;

import Notorious.event.Event;
import net.minecraft.client.gui.ScaledResolution;

public class EventRenderGUI extends Event<EventRenderGUI> {
	
	public ScaledResolution sr;
	
	public EventRenderGUI(ScaledResolution sr) {
		this.sr = sr;
	}

}
