package com.arkasha335.bobmode.config;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class ModConfig {

    private static Configuration config;

    // --- General Settings ---
    public static boolean humanizationEnabled;
    public static float humanizationFactor;
    
    // --- Ninja Bridge Final Version Settings ---
    public static float ninjaBridgePitch;
    public static int bridgeTickDelay;
    public static int dragClickDurationTicks;
    
    public static void init(File configFile) {
        config = new Configuration(configFile);
        loadConfig();
    }

    public static void loadConfig() {
        config.load();
        
        // --- General Category ---
        String generalCategory = "General";
        config.addCustomCategoryComment(generalCategory, "Settings applicable to all bridging modes.");
        humanizationEnabled = config.getBoolean("HumanizationEnabled", generalCategory, true, "Enable slight randomization to mimic human actions. Currently unused but planned for future modules.");
        humanizationFactor = config.getFloat("HumanizationFactor", generalCategory, 0.25f, 0.0f, 1.0f, "How much randomness to apply. 0.0 = none, 1.0 = max.");

        // --- Ninja Bridge Category ---
        String ninjaCategory = "NinjaBridge_Final";
        config.addCustomCategoryComment(ninjaCategory, "Final, refined settings for Ninja Bridge.");
        
        ninjaBridgePitch = config.getFloat("Pitch", ninjaCategory, 88.2f, 80.0f, 90.0f,
                "Vertical camera angle. The most important setting to tune for block placement.");

        bridgeTickDelay = config.getInt("BridgeTickDelay", ninjaCategory, 4, 2, 10,
                "The number of ticks between each bridge cycle (un-sneak + re-sneak). Lower is faster but riskier.");

        dragClickDurationTicks = config.getInt("DragClickDuration", ninjaCategory, 3, 1, 10,
                "How many ticks the bot spams right-clicks to place a block. Higher values are safer for laggy servers.");

        if (config.hasChanged()) {
            config.save();
        }
    }
}