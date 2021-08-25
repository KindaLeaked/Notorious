package Notorious.module;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import Notorious.Client;
import Notorious.module.modules.combat.AntiBot;
import Notorious.module.modules.combat.KillAura;
import Notorious.module.modules.exploit.Blink;
import Notorious.module.modules.exploit.Disabler;
import Notorious.module.modules.misc.CakeEater;
import Notorious.module.modules.misc.ChatBypass;
import Notorious.module.modules.misc.Spammer;
import Notorious.module.modules.misc.UnfocusedCPU;
import Notorious.module.modules.movement.Flight;
import Notorious.module.modules.movement.LongJump;
import Notorious.module.modules.movement.Speed;
import Notorious.module.modules.player.AutoArmor;
import Notorious.module.modules.player.ChestStealer;
import Notorious.module.modules.player.FastStair;
import Notorious.module.modules.player.InvManager;
import Notorious.module.modules.player.InvMove;
import Notorious.module.modules.player.NoFall;
import Notorious.module.modules.player.Parkour;
import Notorious.module.modules.player.Phase;
import Notorious.module.modules.player.Scaffold;
import Notorious.module.modules.player.Sprint;
import Notorious.module.modules.player.Velocity;
import Notorious.module.modules.render.Animations;
import Notorious.module.modules.render.BetterChat;
import Notorious.module.modules.render.Cape;
import Notorious.module.modules.render.FPSBooster;
import Notorious.module.modules.render.FullBright;
import Notorious.module.modules.render.HUD;
import Notorious.module.modules.render.TargetHUD;
import Notorious.module.modules.render.Xray;

public class ModuleManager {

	public CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();

	public ModuleManager() {

		// Movement
		modules.add(new Flight());
		modules.add(new Speed());
		modules.add(new LongJump());

		// Render
		modules.add(new Notorious.module.modules.render.ArrayList());
		modules.add(new Notorious.module.modules.render.ClickGui());
		modules.add(new Xray());
		modules.add(new FullBright());
		modules.add(new TargetHUD());
		modules.add(new FPSBooster());
		modules.add(new HUD());
		modules.add(new Animations());
		modules.add(new BetterChat());
		modules.add(new Cape());
		
		// Player
		modules.add(new NoFall());
		modules.add(new Velocity());
		modules.add(new Phase());
		modules.add(new AutoArmor());
		modules.add(new ChestStealer());
		modules.add(new InvManager());
		modules.add(new Scaffold());
		modules.add(new FastStair());
		modules.add(new Sprint());
		modules.add(new Parkour());
		modules.add(new InvMove());

		// Combat
		modules.add(new KillAura());
		modules.add(new AntiBot());

		// Misc
		modules.add(new ChatBypass());
		modules.add(new Spammer());
		modules.add(new CakeEater());
		modules.add(new UnfocusedCPU());

		//Exploit
		modules.add(new Disabler());
		modules.add(new Blink());
	}

	public CopyOnWriteArrayList<Module> getModules() {
		return modules;
	}

	public CopyOnWriteArrayList<Module> getEnabledModules() {
		CopyOnWriteArrayList<Module> enabledModules = new CopyOnWriteArrayList<>();

		for (Module m : getModules()) {
			if (m.isEnabled()) {
				enabledModules.add(m);
			}
		}

		return enabledModules;
	}

	public CopyOnWriteArrayList<String> getEnabledModulesNames() {
		CopyOnWriteArrayList<String> enabledModulesNames = new CopyOnWriteArrayList<>();
		
		for(Module m : getModules()) {
			if(m.isEnabled()) {
				if(m.visible) {
					enabledModulesNames.add(m.displayName);
				}
			}
		}
		return enabledModulesNames;
	}

	public ArrayList<Module> getModulesInCategory(Category categoryIn) {
		ArrayList<Module> mods = new ArrayList<Module>();
		for (Module m : Client.instance.moduleManager.getModules()) {
			if (m.getCategory() == categoryIn)
				mods.add(m);
		}
		return mods;
	}

	public Module getModuleByName(String name) {
		for (Module m : getModules()) {
			if (m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		return null;
	}

	private void addModule(Module module) {
		modules.add(module);
	}

}
