package Notorious.utils;

import java.awt.Color;

import org.lwjgl.opengl.GL11;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class RenderUtils {
    
    public static boolean SetCustomYaw = false;
    public static float CustomYaw = 0;
    
    public static void setCustomYaw(float customYaw) {
        CustomYaw = customYaw;
        SetCustomYaw = true;
        mc.thePlayer.rotationYawHead = customYaw;
    }
    
    public static void resetPlayerYaw() {
        SetCustomYaw = false;
    }
    
    public static float getCustomYaw() {
        return CustomYaw;
    }
    public static boolean SetCustomPitch = false;
    public static float CustomPitch = 0;
    
    public static void setCustomPitch(float customPitch) {
        CustomPitch = customPitch;
    }
    public static boolean isCustomPitch() {
        if(CustomPitch != 0) {
            return true;
        }else {
            return false;
        }
    }
    public static void resetPlayerPitch() {
        SetCustomPitch = false;
    }
    
    public static float getCustomPitch() {
        return CustomPitch;
    }
    
    public static int getColorFromPercentage(float current, float max) {
        float percentage = (current / max) / 3;
        return Color.HSBtoRGB(percentage, 1.0F, 1.0F);
    }
    
    public static Minecraft mc = Minecraft.getMinecraft();
    
}