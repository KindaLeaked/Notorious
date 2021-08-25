package Notorious.module.modules.render;

import Notorious.module.Category;
import Notorious.module.Module;
import net.minecraft.client.renderer.entity.RenderItem;

public class FPSBooster extends Module {
	
	private RenderItem RI;

	public FPSBooster() {
		super("Fps+", 0, true, Category.RENDER, "Improves FPS by not rendering lag causing entities such as item frames.");
        this.RI = new RenderItem(mc.renderEngine, mc.modelManager);
	}

	@Override
	public void onEnable() {

	}
	
	@Override
	public void onDisable() {
		
	}
	
}
