package com.arkasha335.bobmode.strategy;

public interface IBridgeStrategy {
    /**
     * @return The user-friendly name of the strategy.
     */
    String getName();

    /**
     * Called once when the strategy is activated.
     * Use this for setup, like saving initial rotations.
     */
    void onEnable();

    /**
     * Called once when the strategy is deactivated.
     * Use this for cleanup, like restoring rotations and un-pressing keys.
     */
    void onDisable();

    /**
     * Called every client tick while the strategy is active.
     * Contains the core bridging logic.
     */
    void onTick();
}