package com.arkasha335.bobmode.utils;

import java.util.concurrent.ThreadLocalRandom;

import com.arkasha335.bobmode.config.ModConfig;

public class Humanizer {

    /**
     * Applies a small, random offset to a value.
     * @param base The original value (e.g., yaw or pitch).
     * @param factor The maximum deviation from the base value.
     * @return The value with a random offset applied.
     */
    private static float applyRandomOffset(float base, float factor) {
        if (!ModConfig.humanizationEnabled) {
            return base;
        }
        return base + (ThreadLocalRandom.current().nextFloat() - 0.5f) * 2.0f * factor;
    }

    /**
     * Gets a humanized rotation value.
     * @param baseRotation The target rotation.
     * @return A slightly randomized rotation.
     */
    public static float getHumanizedRotation(float baseRotation) {
        return applyRandomOffset(baseRotation, ModConfig.humanizationFactor);
    }
}