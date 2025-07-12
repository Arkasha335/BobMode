package com.arkasha335.bobmode.strategy.impl;

import com.arkasha335.bobmode.config.ModConfig;
import com.arkasha335.bobmode.manager.BridgingManager;
import com.arkasha335.bobmode.strategy.IBridgeStrategy;
import com.arkasha335.bobmode.utils.Humanizer;
import com.arkasha335.bobmode.utils.PlayerControlUtils;
import com.arkasha335.bobmode.utils.RotationUtils; // <-- Новый импорт
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.MathHelper;

public class NinjaBridgeStrategy implements IBridgeStrategy {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final BridgingManager bridgingManager = BridgingManager.getInstance();

    private float originalYaw, originalPitch;
    private float targetYaw;
    private int ticksPassed = 0;
    private State currentState = State.IDLE;

    private enum State {
        IDLE,
        SNEAKING,
        UNSNEAKING
    }

    @Override
    public String getName() {
        return "Ninja Bridge";
    }

    @Override
    public void onEnable() {
        this.originalYaw = mc.thePlayer.rotationYaw;
        this.originalPitch = mc.thePlayer.rotationPitch;

        // Используем новый надежный метод для расчета угла
        this.targetYaw = RotationUtils.snapTo45DegreeYaw(mc.thePlayer.rotationYaw);
        
        this.ticksPassed = 0;
        this.currentState = State.SNEAKING; // Начинаем сразу с шифта
    }

    @Override
    public void onDisable() {
        PlayerControlUtils.setPlayerRotation(originalYaw, originalPitch);
        PlayerControlUtils.unpressAll();
    }

    @Override
    public void onTick() {
        // Проверка безопасности на наличие блоков в руке
        if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
            bridgingManager.deactivate();
            return;
        }
        
        // 1. Управление прицелом (постоянно)
        float humanizedPitch = Humanizer.getHumanizedRotation(ModConfig.ninjaBridgePitch);
        PlayerControlUtils.setPlayerRotation(this.targetYaw, humanizedPitch);

        // 2. Управление движением (постоянно)
        PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindBack, true);
        
        // Новая, более надежная логика для стрейфа
        int yawQuadrant = (MathHelper.floor_double((double)(this.targetYaw * 4.0F / 360.0F) + 0.5D) & 3);
        boolean isDiagonal = (this.targetYaw % 90 != 0);

        if (isDiagonal) {
             // Определяем, влево или вправо стрейфить для диагонали
            if(yawQuadrant == 1 || yawQuadrant == 2) { // Юго-Восток или Юго-Запад
                PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindLeft, true);
                PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindRight, false);
            } else { // Северо-Запад или Северо-Восток (вид сзади)
                PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindRight, true);
                PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindLeft, false);
            }
        } else {
            PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindLeft, false);
            PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindRight, false);
        }
        
        // 3. Новая, упрощенная и надежная стейт-машина
        ticksPassed++;
        
        if (currentState == State.SNEAKING) {
            PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindSneak, true);
            // Ставим блок
            PlayerControlUtils.rightClick();

            // Переход в состояние отжатия шифта после паузы
            if (ticksPassed >= ModConfig.ninjaClickCooldownTicks) {
                currentState = State.UNSNEAKING;
                ticksPassed = 0;
            }
        } else if (currentState == State.UNSNEAKING) {
            PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindSneak, false);

            // Переход обратно в состояние шифта через 1-2 тика
            if (ticksPassed >= ModConfig.ninjaBridgeUnsneakTicks) {
                currentState = State.SNEAKING;
                ticksPassed = 0;
            }
        }
    }
}