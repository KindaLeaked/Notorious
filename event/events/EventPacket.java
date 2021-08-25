package Notorious.event.events;

import Notorious.event.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event<EventPacket> {
	
 	public Packet packet;
	private boolean incoming;
    
	public EventPacket(Packet packet, boolean incoming) {
		this.packet = packet;
		this.incoming = incoming;
	}
	
	public boolean isSending() {
		return !this.incoming;
	}
	
	public boolean isRecieving() {
		return this.incoming;
	}
	
	public Packet getPacket() {
		return packet;
	}
	
	public void setPacket(Packet packet) {
		this.packet = packet;
	}

}
