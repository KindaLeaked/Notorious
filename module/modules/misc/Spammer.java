package Notorious.module.modules.misc;

import Notorious.event.Event;
import Notorious.event.events.EventChat;
import Notorious.module.Category;
import Notorious.module.Module;
import Notorious.utils.Console;
import Notorious.utils.MathUtils;
import Notorious.utils.Timer;

public class Spammer extends Module {

	public Timer timer = new Timer();
	
	public Spammer() {
		super("Spammer", 0, true, Category.MISC, "Spammes the chat with advertisements for Notorious");
	}
	
	@Override
	public void onUpdate() {
		if(timer.hasTimeElapsed(1000, true)){
			Console.sendChat("Notorious on TOP! DemoDev#7399 on Discord!! " + MathUtils.getRandomInRange(1, 100000000) + MathUtils.getRandomInRange(1, 500000000) + MathUtils.getRandomInRange(1, 5000));
		}
	}
	
	@Override
	public void onEvent(Event e) {
		if(e instanceof EventChat) {
			if(((EventChat) e).getChatComponent().getUnformattedText().contains("You are now invisible.")) {
				Console.sendChatToPlayerWithPrefix("Disabled Spammer due to game end.");
				this.toggle();
			}
		}
	}

}
