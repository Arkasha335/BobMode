package com.arkasha335.bobmode.strategy.impl;

import com.arkasha335.bobmode.config.ModConfig;
import com.arkasha335.bobmode.manager.BridgingManager;
import com.arkasha335.bobmode.strategy.IBridgeStrategy;
import com.arkasha335.bobmode.utils.PlayerControlUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.MathHelper;

public class NinjaBridgeStrategy implements IBridgeStrategy {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final BridgingManager bridgingManager = BridgingManager.getInstance();

    private int ticksPassed = 0;
    private State currentState = State.IDLE;
    private int dragClickCounter = 0;

    // Новая, надежная стейт-машина
    private enum State {
        IDLE,           // Неактивен
        STARTING,       // Начальная фаза: зажимаем шифт, готовимся
        PLACING_BLOCK,  // Фаза постановки блока (drag-click)
        UNSNEAKING,     // Короткое отжатие шифта для шага
        COOLDOWN        // Пауза перед следующим циклом
    }

    @Override
    public String getName() {
        return "Ninja Bridge";
    }

    @Override
    public void onEnable() {
        if (mc.thePlayer == null) {
            bridgingManager.deactivate();
            return;
        }

        // --- ОДНОКРАТНАЯ УСТАНОВКА УГЛА ---
        // 1. Берем текущее направление игрока
        float playerYaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
        // 2. Добавляем 135 градусов для диагонального вида вправо-назад
        float targetYaw = playerYaw + 135.0f;
        // 3. Устанавливаем углы ОДИН РАЗ
        PlayerControlUtils.setPlayerRotation(targetYaw, ModConfig.ninjaBridgePitch);

        // --- ИНИЦИАЛИЗАЦИЯ ---
        this.ticksPassed = 0;
        this.dragClickCounter = 0;
        this.currentState = State.STARTING; // Начинаем
    }

    @Override
    public void onDisable() {
        // Очистка при выключении
        PlayerControlUtils.unpressAll();
    }

    @Override
    public void onTick() {
        // Проверка безопасности
        if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
            bridgingManager.deactivate();
            return;
        }

        // --- УПРАВЛЕНИЕ ДВИЖЕНИЕМ (постоянно активно) ---
        PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindBack, true);
        PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindRight, true); // Всегда вправо-назад

        // --- ГЛАВНАЯ ЛОГИКА (СТЕЙТ-МАШИНА) ---
        ticksPassed++;

        switch (currentState) {
            case STARTING:
                // Зажимаем шифт и сразу переходим к постановке блока
                PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindSneak, true);
                this.currentState = State.PLACING_BLOCK;
                this.ticksPassed = 0;
                break;

            case PLACING_BLOCK:
                // --- ИСКУССТВЕННЫЙ DRAG CLICK ---
                PlayerControlUtils.rightClick(); // Кликаем каждый тик
                
                if (ticksPassed >= ModConfig.dragClickDurationTicks) {
                    // "Драг-клик" завершен, переходим к шагу
                    this.currentState = State.UNSNEAKING;
                    this.ticksPassed = 0;
                }
                break;

            case UNSNEAKING:
                // Делаем шаг вперед
                PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindSneak, false);

                // Отжимаем шифт только на 1 тик - это самый быстрый и стабильный способ
                if (ticksPassed >= 1) {
                    this.currentState = State.COOLDOWN;
                    this.ticksPassed = 0;
                }
                break;

            case COOLDOWN:
                // Снова зажимаем шифт и ждем
                PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindSneak, true);

                // Ждем заданную паузу перед следующим циклом
                if (ticksPassed >= ModConfig.bridgeTickDelay) {
                    this.currentState = State.PLACING_BLOCK;
                    this.ticksPassed = 0;
                }
                break;

            case IDLE:
                // Ничего не делаем
                break;
        }
    }
}