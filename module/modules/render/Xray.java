package Notorious.module.modules.render;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import Notorious.module.Category;
import Notorious.module.Module;
import Notorious.utils.XRayUtils;
import net.minecraft.block.Block;

public class Xray extends Module{
	
	public static ArrayList<Block> xrayBlocks = new ArrayList<Block>();
	
	public Xray(){
		super("Xray", Keyboard.KEY_X, true, Category.RENDER, "Shows all ores in the world.");
	}
	
	public void onEnable(){
		XRayUtils.isXRay = true;
		mc.renderGlobal.loadRenderers();
	}
	
	public void onDisable(){
		XRayUtils.isXRay = false;
		mc.renderGlobal.loadRenderers();
	}
	
	public static boolean isXrayBlock(Block blockToCheck){
		if(xrayBlocks.contains(blockToCheck)){
			return true;
		}
		return false;
	}

}