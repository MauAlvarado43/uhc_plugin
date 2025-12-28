package vch.uhc.misc.enums;

/**
 * Defines what happens to eliminated players.
 */
public enum EliminationMode {
    
    /**
     * Eliminated players become spectators and can watch the game.
     */
    SPECTATOR("Spectator"),
    
    /**
     * Eliminated players are kicked from the server until the game ends.
     */
    KICK("Kick");
    
    private final String displayName;
    
    EliminationMode(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the human-readable display name for this elimination mode.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
