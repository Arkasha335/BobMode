package com.arkasha335.bobmode.handler;

import com.arkasha335.bobmode.manager.BridgingManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickHandler {
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            BridgingManager.getInstance().onTick();
        }
    }
}