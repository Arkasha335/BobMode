package com.arkasha335.bobmode;

import com.arkasha335.bobmode.config.ModConfig;
import com.arkasha335.bobmode.handler.KeyInputHandler;
import com.arkasha335.bobmode.handler.RenderHandler;
import com.arkasha335.bobmode.handler.TickHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BobMode.MODID, name = BobMode.NAME, version = BobMode.VERSION, clientSideOnly = true)
public class BobMode {
    public static final String MODID = "bobmode";
    public static final String NAME = "BobMode";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Load configuration
        ModConfig.init(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Register keybindings
        KeyInputHandler.register();
        
        // Register event handlers
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        MinecraftForge.EVENT_BUS.register(new TickHandler());
        MinecraftForge.EVENT_BUS.register(new RenderHandler());
    }
}