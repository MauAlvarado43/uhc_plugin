package vch.uhc.misc.constants;

/**
 * Centralized configuration keys for settings and preferences.
 * Ensures consistency across configuration access.
 */
public final class ConfigKeys {

    // Prevent instantiation
    private ConfigKeys() {
        throw new java.lang.AssertionError("Cannot instantiate constants class");
    }

    // ========================================
    // World Settings
    // ========================================
    
    public static final String MAX_WORLD_SIZE = "world.maxSize";
    public static final String MIN_WORLD_SIZE = "world.minSize";
    public static final String GRADUAL_BORDER_ENABLED = "world.gradualBorder";

    // ========================================
    // Game Duration Settings
    // ========================================
    
    public static final String GAME_HOURS = "game.duration.hours";
    public static final String GAME_MINUTES = "game.duration.minutes";
    public static final String GAME_SECONDS = "game.duration.seconds";

    // ========================================
    // Agreement Phase Settings
    // ========================================
    
    public static final String AGREEMENT_HOURS = "agreement.hours";
    public static final String AGREEMENT_MINUTES = "agreement.minutes";
    public static final String AGREEMENT_SECONDS = "agreement.seconds";

    // ========================================
    // Border Shrink Settings
    // ========================================
    
    public static final String MIN_BORDER_HOURS = "border.shrink.hours";
    public static final String MIN_BORDER_MINUTES = "border.shrink.minutes";
    public static final String MIN_BORDER_SECONDS = "border.shrink.seconds";

    // ========================================
    // Team Settings
    // ========================================
    
    public static final String TEAM_MODE = "team.mode";
    public static final String TEAM_SIZE = "team.size";
    public static final String MAX_TEAM_IN_GAME_HOURS = "team.inGame.maxHours";
    public static final String MAX_TEAM_IN_GAME_MINUTES = "team.inGame.maxMinutes";
    public static final String MAX_TEAM_IN_GAME_SECONDS = "team.inGame.maxSeconds";

    // ========================================
    // Player Settings
    // ========================================
    
    public static final String PLAYER_LIVES = "player.lives";

    // ========================================
    // Feature Timing Settings
    // ========================================
    
    public static final String END_PORTAL_HOURS = "features.endPortal.hours";
    public static final String END_PORTAL_MINUTES = "features.endPortal.minutes";
    public static final String END_PORTAL_SECONDS = "features.endPortal.seconds";
    
    public static final String SHULKER_ENABLED = "features.shulker.enabled";
    public static final String SHULKER_HOURS = "features.shulker.hours";
    public static final String SHULKER_MINUTES = "features.shulker.minutes";
    public static final String SHULKER_SECONDS = "features.shulker.seconds";
    
    public static final String LOCATOR_BAR_ENABLED = "features.locatorBar.enabled";
    public static final String LOCATOR_BAR_HOURS = "features.locatorBar.hours";
    public static final String LOCATOR_BAR_MINUTES = "features.locatorBar.minutes";
    public static final String LOCATOR_BAR_SECONDS = "features.locatorBar.seconds";
    
    public static final String BUFFS_ENABLED = "features.buffs.enabled";
    public static final String BUFFS_HOURS = "features.buffs.hours";
    public static final String BUFFS_MINUTES = "features.buffs.minutes";
    public static final String BUFFS_SECONDS = "features.buffs.seconds";
    public static final String BUFFS_EXTRA_HEARTS = "features.buffs.extraHearts";
    
    public static final String SKIN_SHUFFLE_ENABLED = "features.skinShuffle.enabled";
    public static final String SKIN_SHUFFLE_MINUTES = "features.skinShuffle.minutes";
    public static final String SKIN_SHUFFLE_SECONDS = "features.skinShuffle.seconds";

    // ========================================
    // Game Mode Settings
    // ========================================
    
    public static final String GAME_MODE = "game.mode";

}
