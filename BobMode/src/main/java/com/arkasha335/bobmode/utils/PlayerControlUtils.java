package com.Arkasha335.bobmode.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import java.lang.reflect.Method;

public class PlayerControlUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private static Method rightClickMouseMethod;

    static {
        // Using reflection to call the private method for right-clicking.
        // This is a common technique in 1.8.9 modding.
        try {
            rightClickMouseMethod = Minecraft.class.getDeclaredMethod("rightClickMouse");
            rightClickMouseMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            // In a production environment, you would log this error.
            e.printStackTrace();
        }
    }

    public static void setPlayerRotation(float yaw, float pitch) {
        if (mc.thePlayer == null) return;
        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationPitch = pitch;
    }

    public static void setKeyPressed(KeyBinding key, boolean pressed) {
        KeyBinding.setKeyBindState(key.getKeyCode(), pressed);
    }

    public static void rightClick() {
        // A more robust way than sending packets, as it uses the game's own logic.
        try {
            if (rightClickMouseMethod != null) {
                rightClickMouseMethod.invoke(mc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void unpressAll() {
        setKeyPressed(mc.gameSettings.keyBindForward, false);
        setKeyPressed(mc.gameSettings.keyBindBack, false);
        setKeyPressed(mc.gameSettings.keyBindLeft, false);
        setKeyPressed(mc.gameSettings.keyBindRight, false);
        setKeyPressed(mc.gameSettings.keyBindJump, false);
        setKeyPressed(mc.gameSettings.keyBindSneak, false);
        setKeyPressed(mc.gameSettings.keyBindAttack, false);
        setKeyPressed(mc.gameSettings.keyBindUseItem, false);
    }
}