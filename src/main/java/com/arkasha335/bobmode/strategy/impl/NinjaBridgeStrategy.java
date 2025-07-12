package com.arkasha335.bobmode.strategy.impl;

import com.arkasha335.bobmode.config.ModConfig;
import com.arkasha335.bobmode.strategy.IBridgeStrategy;
import com.arkasha335.bobmode.utils.Humanizer;
import com.arkasha335.bobmode.utils.PlayerControlUtils;
import com.arkasha335.bobmode.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemBlock;

public class NinjaBridgeStrategy implements IBridgeStrategy {

    private final Minecraft mc = Minecraft.getMinecraft();

    private float originalYaw, originalPitch;
    private float targetYaw;
    private int ticksSinceLastAction = 0;
    private State currentState = State.IDLE;
    private boolean placedSinceSneak = false;

    private enum State {
        IDLE,
        PLACING,
        UNSNEAKING,
        COOLDOWN
    }

    @Override
    public String getName() {
        return "Ninja Bridge";
    }

    @Override
    public void onEnable() {
        this.originalYaw = mc.thePlayer.rotationYaw;
        this.originalPitch = mc.thePlayer.rotationPitch;

        // Snap yaw to the nearest 45-degree angle for diagonal bridging
        this.targetYaw = Math.round(mc.thePlayer.rotationYaw / 45.0f) * 45.0f;
        
        this.ticksSinceLastAction = 0;
        this.currentState = State.PLACING;
        this.placedSinceSneak = false;
    }

    @Override
    public void onDisable() {
        PlayerControlUtils.setPlayerRotation(originalYaw, originalPitch);
        PlayerControlUtils.unpressAll();
    }

    @Override
    public void onTick() {
        // Safety check: ensure player is holding blocks
        if (mc.thePlayer.getHeldItem() == null || !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
            BridgingManager.getInstance().deactivate(); // Using the manager's public method
            return;
        }
        
        // --- Core Logic ---
        // 1. Aim Control
        float humanizedYaw = Humanizer.getHumanizedRotation(targetYaw);
        float humanizedPitch = Humanizer.getHumanizedRotation(ModConfig.ninjaBridgePitch);
        PlayerControlUtils.setPlayerRotation(humanizedYaw, humanizedPitch);

        // 2. Movement Control (always active)
        PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindBack, true);
        // Determine direction based on yaw (simplistic but effective)
        if (((int)targetYaw % 180) != 0) { // Assuming diagonal
            PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindRight, true);
        } else {
             PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindLeft, false); // Turn off just in case
        }
        
        // 3. State Machine for Shift & Clicks
        ticksSinceLastAction++;

        switch (currentState) {
            case PLACING:
                PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindSneak, true);
                PlayerControlUtils.rightClick(); // Click every tick during the burst

                if (ticksSinceLastAction >= ModConfig.ninjaClickBurstDurationTicks) {
                    placedSinceSneak = WorldUtils.isBlockPlacedUnderPlayer();
                    currentState = State.UNSNEAKING;
                    ticksSinceLastAction = 0;
                }
                break;

            case UNSNEAKING:
                // Only unsneak if a block was successfully placed. This is the reactive part.
                if (placedSinceSneak) {
                    PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindSneak, false);
                }

                if (ticksSinceLastAction >= ModConfig.ninjaBridgeUnsneakTicks) {
                    PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindSneak, true);
                    currentState = State.COOLDOWN;
                    ticksSinceLastAction = 0;
                }
                break;
            
            case COOLDOWN:
                PlayerControlUtils.setKeyPressed(mc.gameSettings.keyBindSneak, true);
                if (ticksSinceLastAction >= ModConfig.ninjaClickCooldownTicks) {
                    currentState = State.PLACING;
                    ticksSinceLastAction = 0;
                    placedSinceSneak = false;
                }
                break;
        }
    }
}