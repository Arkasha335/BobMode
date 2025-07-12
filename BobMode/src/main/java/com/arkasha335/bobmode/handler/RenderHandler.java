package com.arkasha335.bobmode.handler;

import com.arkasha335.bobmode.manager.BridgingManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderHandler {

    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        
        BridgingManager manager = BridgingManager.getInstance();
        FontRenderer fr = mc.fontRendererObj;
        String statusText;

        if (manager.isActive()) {
            statusText = String.format("§aBobMode: ON - %s", manager.getCurrentStrategyName());
        } else {
            statusText = "§cBobMode: OFF";
        }

        fr.drawStringWithShadow(statusText, 5, 5, 0xFFFFFF);
    }
}