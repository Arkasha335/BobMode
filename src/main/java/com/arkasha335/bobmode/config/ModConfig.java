package com.arkasha335.bobmode.config;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class ModConfig {

    private static Configuration config;

    // Ninja Bridge Final Version Settings
    public static float ninjaBridgePitch;
    public static int bridgeTickDelay;
    public static int dragClickDurationTicks;
    
    public static void init(File configFile) {
        config = new Configuration(configFile);
        loadConfig();
    }

    public static void loadConfig() {
        config.load();
        
        String category = "NinjaBridge_Final";

        config.addCustomCategoryComment(category, "Final, refined settings for Ninja Bridge.");
        
        ninjaBridgePitch = config.getFloat("Pitch", category, 88.2f, 80.0f, 90.0f,
                "Vertical camera angle. The most important setting to tune for block placement.");

        bridgeTickDelay = config.getInt("BridgeTickDelay", category, 4, 2, 10,
                "The number of ticks between each bridge cycle (un-sneak + re-sneak). Lower is faster but riskier.");

        dragClickDurationTicks = config.getInt("DragClickDuration", category, 3, 1, 10,
                "How many ticks the bot spams right-clicks to place a block. Higher values are safer for laggy servers.");

        if (config.hasChanged()) {
            config.save();
        }
    }
}