package com.arkasha335.bobmode.handler;

import com.arkasha335.bobmode.manager.BridgingManager;
import com.arkasha335.bobmode.strategy.impl.NinjaBridgeStrategy;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyInputHandler {

    public static final KeyBinding ninjaBridgeKey = new KeyBinding("key.bobmode.ninja", Keyboard.KEY_NONE, "key.categories.bobmode");

    public static void register() {
        ClientRegistry.registerKeyBinding(ninjaBridgeKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ninjaBridgeKey.isPressed()) {
            BridgingManager.getInstance().toggle(new NinjaBridgeStrategy());
        }
    }
}