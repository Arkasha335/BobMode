package com.arkasha335.bobmode.manager;

import com.arkasha335.bobmode.strategy.IBridgeStrategy;
import net.minecraft.client.Minecraft;

public class BridgingManager {

    private static final BridgingManager INSTANCE = new BridgingManager();
    private final Minecraft mc = Minecraft.getMinecraft();

    private boolean isActive = false;
    private IBridgeStrategy currentStrategy = null;

    private BridgingManager() {}

    public static BridgingManager getInstance() {
        return INSTANCE;
    }

    public void toggle(IBridgeStrategy strategy) {
        if (isActive && currentStrategy != null && currentStrategy.getClass().equals(strategy.getClass())) {
            deactivate();
        } else {
            activate(strategy);
        }
    }

    public void activate(IBridgeStrategy strategy) {
        if (isActive) {
            deactivate();
        }
        if (mc.thePlayer == null) return; // Дополнительная проверка безопасности
        this.currentStrategy = strategy;
        this.isActive = true;
        this.currentStrategy.onEnable();
    }

    public void deactivate() {
        if (currentStrategy != null) {
            currentStrategy.onDisable();
        }
        this.currentStrategy = null;
        this.isActive = false;
    }
    
    public void onTick() {
        // Центральные проверки безопасности
        if (!isActive || currentStrategy == null || mc.thePlayer == null || mc.currentScreen != null) {
            if (isActive) {
                deactivate();
            }
            return;
        }

        currentStrategy.onTick();
    }

    public boolean isActive() {
        return isActive;
    }

    public String getCurrentStrategyName() {
        return (currentStrategy != null) ? currentStrategy.getName() : "None";
    }
}