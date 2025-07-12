package com.arkasha335.bobmode.config;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class ModConfig {

    private static Configuration config;

    // General
    public static boolean humanizationEnabled;
    public static float humanizationFactor;

    // Ninja Bridge Settings
    public static float ninjaBridgePitch;
    public static int ninjaBridgeUnsneakTicks;
    public static int ninjaClickBurstDurationTicks; // Длительность "драг-клика" в тиках
    public static int ninjaClickCooldownTicks;      // Пауза между "драг-кликами"

    public static void init(File configFile) {
        config = new Configuration(configFile);
        loadConfig();
    }

    public static void loadConfig() {
        config.load();

        config.addCustomCategoryComment("General", "Settings applicable to all bridging modes.");
        humanizationEnabled = config.getBoolean("HumanizationEnabled", "General", true, "Enable slight randomization to mimic human actions and bypass anti-cheats.");
        humanizationFactor = config.getFloat("HumanizationFactor", "General", 0.25f, 0.0f, 1.0f, "How much randomness to apply. 0.0 = none, 1.0 = max.");

        config.addCustomCategoryComment("NinjaBridge", "Specific settings for the Ninja Bridge module.");
        ninjaBridgePitch = config.getFloat("Pitch", "NinjaBridge", 82.5f, 75.0f, 90.0f, "The vertical camera angle for placing blocks.");
        ninjaBridgeUnsneakTicks = config.getInt("UnsneakDuration", "NinjaBridge", 1, 1, 5, "How many ticks to stay unsneaked to move forward. (1 tick = 50ms). Higher values are faster but riskier.");
        ninjaClickBurstDurationTicks = config.getInt("ClickBurstDuration", "NinjaBridge", 8, 1, 20, "How many ticks the right mouse button is held down for placing a block burst.");
        ninjaClickCooldownTicks = config.getInt("ClickCooldown", "NinjaBridge", 4, 0, 20, "How many ticks to wait before starting the next click burst.");


        if (config.hasChanged()) {
            config.save();
        }
    }
}