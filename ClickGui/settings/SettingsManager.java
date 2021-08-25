package Notorious.ClickGui.settings;

import java.util.ArrayList;
import java.util.List;

import Notorious.Client;
import Notorious.module.Module;



/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit him
 *
 *  @author HeroCode
 */
public class SettingsManager {
	
	private ArrayList<Setting> settings;
	
	private List<Setting> settttings;
	
	public SettingsManager(){
		this.settings = new ArrayList<Setting>();
	}
	
	public void rSetting(Setting in){
		this.settings.add(in);
	}
	
	public ArrayList<Setting> getSettings(){
		return this.settings;
	}
	
	public ArrayList<Setting> getSettingsByMod(Module mod){
		ArrayList<Setting> out = new ArrayList<Setting>();
		for(Setting s : getSettings()){
			if(s.getParentMod().equals(mod)){
				out.add(s);
			}
		}
		if(out.isEmpty()){
			return null;
		}
		return out;
	}
	
	public Setting getSettingByName(String name){
		for(Setting set : getSettings()){
			if(set.getName().equalsIgnoreCase(name)){
				return set;
			}
		}
		System.err.println("["+ Client.instance.name + "] Error Setting NOT found: '" + name +"'!");
		return null;
	}
	
	public List<Setting> getSettingsByDisplayName(String displayName) {
		settttings.clear();
		for(Setting set : getSettings()) {
			if(set.getDisplayName().equalsIgnoreCase(displayName)) {
				settttings.add(set);
			}
		}
		return settttings;
	}

}