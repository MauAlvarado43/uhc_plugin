package vch.uhc.misc.enums;

/**
 * Defines how the world border shrinks during a UHC game.
 */
public enum BorderType {
    
    /**
     * Border never shrinks, remains at initial size.
     */
    NONE("None"),
    
    /**
     * Border shrinks gradually throughout the entire game duration.
     */
    GRADUAL("Gradual"),
    
    /**
     * Border shrinks instantly at the end of the game.
     */
    INSTANT("Instant"),
    
    /**
     * Border starts shrinking when a threshold time is reached.
     * Requires start and end time configuration.
     */
    THRESHOLD("Threshold");
    
    private final String displayName;
    
    BorderType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the human-readable display name for this border type.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
