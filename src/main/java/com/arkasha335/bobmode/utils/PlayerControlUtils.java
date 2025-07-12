package com.arkasha335.bobmode.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Mouse;

public class PlayerControlUtils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void setPlayerRotation(float yaw, float pitch) {
        if (mc.thePlayer == null) return;
        mc.thePlayer.rotationYaw = yaw;
        mc.thePlayer.rotationPitch = pitch;
    }

    public static void setKeyPressed(KeyBinding key, boolean pressed) {
        KeyBinding.setKeyBindState(key.getKeyCode(), pressed);
    }
    
    /**
     * НАДЕЖНЫЙ метод для симуляции правого клика.
     * Он напрямую вызывает логику обработки тика для кнопки.
     */
    public static void rightClick() {
        if (mc.gameSettings.keyBindUseItem.getKeyCode() < 0) {
            // Для кнопок мыши
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
        } else {
             // Для клавиш клавиатуры (на всякий случай)
             setKeyPressed(mc.gameSettings.keyBindUseItem, true);
             setKeyPressed(mc.gameSettings.keyBindUseItem, false);
        }
    }
    
    public static void unpressAll() {
        // Этот метод остается без изменений
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