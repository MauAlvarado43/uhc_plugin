package vch.uhc.misc.enums;

/**
 * Represents different UHC game modes available.
 * Each mode has unique rules and objectives.
 */
public enum GameMode {
    
    /**
     * Player vs Dragon mode.
     * Players must defeat the Ender Dragon.
     */
    PVD("Player vs Dragon"),
    
    /**
     * Player vs Player mode.
     * Traditional UHC where players fight each other.
     */
    PVP("Player vs Player"),
    
    /**
     * Resource Rush mode.
     * Players compete to gather resources quickly.
     */
    RESOURCE_RUSH("Resource Rush");
    
    private final String displayName;
    
    GameMode(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the human-readable display name for this game mode.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
}
