package Notorious.ClickGui.settings;

import java.util.ArrayList;

import Notorious.module.Module;


/**
 *  Made by HeroCode
 *  it's free to use
 *  but you have to credit him
 *
 *  @author HeroCode
 */
public class Setting {
	
	private String name;
	private String displayName;
	private Module parent;
	public String mode;
	
	private String sval;
	private ArrayList<String> options;
	
	private boolean bval;
	
	private double dval;
	private double min;
	private double max;
	private boolean onlyint = false;
	

	public Setting(String name, String displayName, Module module, String sval, ArrayList<String> options){
		this.name = name;
		this.displayName = displayName;
		this.parent = module;
		this.sval = sval;
		this.options = options;
		this.mode = "Combo";
	}
	
	public Setting(String name, String displayName, Module parent, boolean bval){
		this.name = name;
		this.displayName = displayName;
		this.parent = parent;
		this.bval = bval;
		this.mode = "Check";
	}
	
	public Setting(String name, String displayName, Module parent, double dval, double min, double max, boolean onlyint){
		this.name = name;
		this.displayName = displayName;
		this.parent = parent;
		this.dval = dval;
		this.min = min;
		this.max = max;
		this.onlyint = onlyint;
		this.mode = "Slider";
	}
	
	public Setting(String name, String displayName, Module parent) {
		this.name = name;
		this.displayName = displayName;
		this.parent = parent;
		this.mode = "Label";
	}
	
	public String getName(){
		return name;
	}
	
	public Module getParentMod(){
		return parent;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getValString(){
		return this.sval;
	}
	
	public void setValString(String in){
		this.sval = in;
	}
	
	public ArrayList<String> getOptions(){
		return this.options;
	}
	
	public boolean getValBoolean(){
		return this.bval;
	}
	
	public void setValBoolean(boolean in){
		this.bval = in;
	}
	
	public double getValDouble(){
		if(this.onlyint){
			this.dval = (int)dval;
		}
		return this.dval;
	}

	public void setValDouble(double in){
		this.dval = in;
	}
	
	public double getMin(){
		return this.min;
	}
	
	public double getMax(){
		return this.max;
	}
	
	public boolean isCombo(){
		return this.mode.equalsIgnoreCase("Combo") ? true : false;
	}
	
	public boolean isCheck(){
		return this.mode.equalsIgnoreCase("Check") ? true : false;
	}
	
	public boolean isSlider(){
		return this.mode.equalsIgnoreCase("Slider") ? true : false;
	}
	
	public boolean isLabel() {
		return this.mode.equalsIgnoreCase("Label") ? true : false;
	}
	
	public boolean onlyInt(){
		return this.onlyint;
	}
}
