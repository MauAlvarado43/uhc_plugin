package vch.uhc.misc.enums;

/**
 * Defines how teams are formed in a UHC game.
 * Controls team assignment behavior.
 */
public enum TeamMode {
    
    /**
     * Teams are formed during gameplay based on proximity.
     * Players near each other can form teams dynamically.
     */
    IN_GAME("In-Game Formation"),
    
    /**
     * Teams are automatically assigned by the system.
     * Usually distributed evenly among all players.
     */
    AUTO("Automatic Assignment"),
    
    /**
     * Teams are manually created and managed.
     * Players join teams via commands.
     */
    MANUAL("Manual Creation");
    
    private final String displayName;
    
    TeamMode(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the human-readable display name for this team mode.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Checks if teams can be formed during active gameplay.
     * 
     * @return true if team mode is IN_GAME
     */
    public boolean allowsInGameFormation() {
        return this == IN_GAME;
    }
    
    /**
     * Checks if teams require manual intervention.
     * 
     * @return true if team mode is MANUAL
     */
    public boolean requiresManualSetup() {
        return this == MANUAL;
    }
    
}
