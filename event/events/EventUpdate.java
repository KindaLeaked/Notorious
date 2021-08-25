package Notorious.event.events;

import Notorious.event.Event;
import net.minecraft.client.Minecraft;

public class EventUpdate extends Event<EventUpdate> {

    public float yaw, pitch;
    public boolean ground;

    public double y;
    private boolean pre;

    public EventUpdate(float yaw, float pitch, boolean ground, double y, boolean pre) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.ground = ground;
        this.y = y;
        this.pre = pre;
    }

    public float getYaw() {
        return yaw;
    }


    public void setYaw(float yaw) {
        this.yaw = yaw;
        Minecraft.getMinecraft().thePlayer.renderYawOffset = yaw;
        Minecraft.getMinecraft().thePlayer.rotationYawHead = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isGround() {
        return ground;
    }

    public void setGround(boolean ground) {
        this.ground = ground;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean isPre() {
        return pre;
    }

    public boolean isPost() {
        return !pre;
    }
}
