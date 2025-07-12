package com.Arkasha335.bobmode.manager;

public class BridgingManager {
    private static BridgingManager instance;
    private BridgingManager() {}
    public static BridgingManager getInstance() {
        if (instance == null) instance = new BridgingManager();
        return instance;
    }
    // Логика управления строительством
} 