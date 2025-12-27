package vch.uhc.misc.enums;

/**
 * Represents the different states of a UHC game session.
 * Used to control game flow and behavior.
 */
public enum GameState {
    
    /**
     * No game is currently running or scheduled.
     * Default state when server starts.
     */
    NONE,
    
    /**
     * Game is actively running.
     * Players are competing and game mechanics are active.
     */
    IN_PROGRESS,
    
    /**
     * Game is temporarily paused.
     * Game state is preserved but timers are stopped.
     */
    PAUSED,
    
    /**
     * Game has ended.
     * Winner has been determined or game was terminated.
     */
    ENDED;
    
    /**
     * Checks if the game is currently active (in progress or paused).
     * 
     * @return true if game is in progress or paused
     */
    public boolean isActive() {
        return this == IN_PROGRESS || this == PAUSED;
    }
    
    /**
     * Checks if the game is in a playable state.
     * 
     * @return true if game is in progress
     */
    public boolean isPlayable() {
        return this == IN_PROGRESS;
    }
    
}
