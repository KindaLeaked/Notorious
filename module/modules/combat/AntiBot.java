package Notorious.module.modules.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Notorious.event.Event;
import Notorious.event.events.EventMotion;
import Notorious.event.events.EventPacket;
import Notorious.module.Category;
import Notorious.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

public class AntiBot extends Module {
	
	//Dort gave it to me ;)
	
	public AntiBot() {
		super("AntiBot", 0, true, Category.COMBAT, "Removes anticheat bots from the world.");
	}
	
	private final List<Integer> invalidEntityIDs = new ArrayList<>();
	
	@Override
	public void onEvent(Event e) {
		if(e instanceof EventPacket) {
			if (((EventPacket) e).getPacket() instanceof S0CPacketSpawnPlayer) {
                S0CPacketSpawnPlayer packetSpawnPlayer = (S0CPacketSpawnPlayer) ((EventPacket) e).getPacket();
                if (packetSpawnPlayer.func_148944_c().size() < 7) {
                    invalidEntityIDs.add(packetSpawnPlayer.getEntityID());
                }
            }
		}
		
		if(e instanceof EventMotion) {
			 for (EntityPlayer player : mc.theWorld.playerEntities.stream().filter(player -> player != mc.thePlayer).collect(Collectors.toList())) {
                 player.setValid(!invalidEntityIDs.contains(player.getEntityId()));
             }
		}
		
	}

}