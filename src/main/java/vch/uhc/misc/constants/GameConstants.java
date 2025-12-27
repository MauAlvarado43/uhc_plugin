package vch.uhc.misc.constants;

/**
 * Core game constants for UHC gameplay mechanics.
 * Centralizes all magic numbers to improve maintainability.
 */
public final class GameConstants {

    // Prevent instantiation of constants class
    private GameConstants() {
        throw new java.lang.AssertionError("Cannot instantiate constants class");
    }

    // ========================================
    // Time Conversion Constants
    // ========================================
    
    /** Seconds in one hour */
    public static final int SECONDS_PER_HOUR = 3600;
    
    /** Seconds in one minute */
    public static final int SECONDS_PER_MINUTE = 60;
    
    /** Ticks per second in Minecraft (20 TPS) */
    public static final int TICKS_PER_SECOND = 20;
    
    /** Milliseconds in one second */
    public static final int MILLIS_PER_SECOND = 1000;

    // ========================================
    // Health & Gameplay Constants
    // ========================================
    
    /** Default maximum health for players (in half-hearts) */
    public static final double DEFAULT_MAX_HEALTH = 20.0;
    
    /** Minimum health value */
    public static final double MIN_HEALTH = 0.0;
    
    /** Default player lives in UHC */
    public static final int DEFAULT_PLAYER_LIVES = 1;

    // ========================================
    // World Border Constants
    // ========================================
    
    /** Default maximum world border size */
    public static final int DEFAULT_MAX_WORLD_SIZE = 1000;
    
    /** Default minimum world border size */
    public static final int DEFAULT_MIN_WORLD_SIZE = 100;
    
    /** Vanilla Minecraft maximum world border size */
    public static final int VANILLA_MAX_WORLD_BORDER = 29999984;
    
    /** Default world center X coordinate */
    public static final double WORLD_CENTER_X = 0.0;
    
    /** Default world center Z coordinate */
    public static final double WORLD_CENTER_Z = 0.0;

    // ========================================
    // World & Time Constants
    // ========================================
    
    /** Day time in Minecraft (1000 ticks) */
    public static final long DAY_TIME = 1000L;
    
    /** Maximum weather duration to disable weather changes */
    public static final int MAX_WEATHER_DURATION = Integer.MAX_VALUE;

    // ========================================
    // Team Constants
    // ========================================
    
    /** Default team size */
    public static final int DEFAULT_TEAM_SIZE = 2;
    
    /** Minimum team size */
    public static final int MIN_TEAM_SIZE = 1;
    
    /** Maximum proximity distance for team formation (in blocks) */
    public static final int TEAM_PROXIMITY_DISTANCE = 50;

    // ========================================
    // Feature Timing Constants (in minutes)
    // ========================================
    
    /** Default time when End Portal opens (minutes) */
    public static final int DEFAULT_END_PORTAL_TIME_MINUTES = 10;
    
    /** Default time when Shulker feature activates (minutes) */
    public static final int DEFAULT_SHULKER_TIME_MINUTES = 15;
    
    /** Default time when Locator Bar activates (minutes) */
    public static final int DEFAULT_LOCATOR_BAR_TIME_MINUTES = 20;
    
    /** Default time when Buffs are applied (minutes) */
    public static final int DEFAULT_BUFFS_TIME_MINUTES = 30;
    
    /** Default time for skin shuffle interval (minutes) */
    public static final int DEFAULT_SKIN_SHUFFLE_TIME_MINUTES = 5;

    // ========================================
    // Buffs & Effects Constants
    // ========================================
    
    /** Default extra hearts from buffs */
    public static final double DEFAULT_EXTRA_HEARTS = 10.0;
    
    /** Potion effect duration (in ticks) - 1 minute */
    public static final int POTION_EFFECT_DURATION_SHORT = 1200;
    
    /** Potion effect duration (in ticks) - 5 minutes */
    public static final int POTION_EFFECT_DURATION_MEDIUM = 6000;
    
    /** Potion effect duration (in ticks) - infinite */
    public static final int POTION_EFFECT_DURATION_INFINITE = Integer.MAX_VALUE;

    // ========================================
    // Scoreboard Constants
    // ========================================
    
    /** Main UHC scoreboard objective name */
    public static final String SCOREBOARD_OBJECTIVE_UHC = "uhc";
    
    /** Health display scoreboard objective name */
    public static final String SCOREBOARD_OBJECTIVE_HEALTH = "health";

    // ========================================
    // File & Storage Constants
    // ========================================
    
    /** Settings file name */
    public static final String SETTINGS_FILE_NAME = "settings.json";
    
    /** Config file name */
    public static final String CONFIG_FILE_NAME = "config.yml";

    // ========================================
    // Miscellaneous Constants
    // ========================================
    
    /** Maximum integer value for unlimited duration */
    public static final int UNLIMITED_DURATION = Integer.MAX_VALUE;
    
    /** Minimum allowed value for zero */
    public static final int ZERO = 0;

}
