package vch.uhc.misc.enums;

/**
 * Centralized permission nodes for the UHC plugin.
 * Prevents hardcoded permission strings scattered throughout the code.
 */
public enum Permission {
    
    /**
     * Admin permission - grants access to all administrative commands.
     */
    ADMIN("uhc.admin"),
    
    /**
     * Permission to use the /uhc start command.
     */
    START("uhc.start"),
    
    /**
     * Permission to use the /uhc stop command.
     */
    STOP("uhc.stop"),
    
    /**
     * Permission to use the /uhc pause command.
     */
    PAUSE("uhc.pause"),
    
    /**
     * Permission to use the /uhc resume command.
     */
    RESUME("uhc.resume"),
    
    /**
     * Permission to use the /uhc reload command.
     */
    RELOAD("uhc.reload"),
    
    /**
     * Permission to manage teams.
     */
    TEAM_MANAGE("uhc.team.manage"),
    
    /**
     * Permission to join teams.
     */
    TEAM_JOIN("uhc.team.join"),
    
    /**
     * Permission to create teams.
     */
    TEAM_CREATE("uhc.team.create"),
    
    /**
     * Permission to use the /afk command.
     */
    AFK("uhc.afk"),
    
    /**
     * Bypass permission for various restrictions.
     */
    BYPASS("uhc.bypass");
    
    private final String node;
    
    Permission(String node) {
        this.node = node;
    }
    
    /**
     * Gets the permission node as a string.
     * 
     * @return the permission node
     */
    public String getNode() {
        return node;
    }
    
    /**
     * Converts this permission to its string representation.
     * Allows direct use in hasPermission() calls.
     * 
     * @return the permission node
     */
    @Override
    public String toString() {
        return node;
    }
    
}
