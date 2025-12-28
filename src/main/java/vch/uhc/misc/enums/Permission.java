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
     * Permission to use the /uhc command.
     */
    USE("uhc.use"),

    /**
     * Permission to use the /uhc info command.
     */
    INFO("uhc.info"),

    /**
     * Permission to use the /uhc start command.
     */
    START("uhc.start"),

    /**
     * Permission to use the /uhc cancel command.
     */
    CANCEL("uhc.cancel"),

    /**
     * Permission to use the /uhc pause command.
     */
    PAUSE("uhc.pause"),

    /**
     * Permission to use the /uhc reload command.
     */
    RELOAD("uhc.reload"),

    /**
     * Permission to use the /uhc join command.
     */
    JOIN("uhc.join"),

    /**
     * Permission to use the /uhc leave command.
     */
    LEAVE("uhc.leave"),

    /**
     * Permission to manage players.
     */
    PLAYERS("uhc.players"),

    /**
     * Permission to list players.
     */
    PLAYERS_LIST("uhc.players.list"),

    /**
     * Permission to set lives for a player.
     */
    PLAYERS_SETLIVES("uhc.players.setLives"),

    /**
     * Permission to set health for a player.
     */
    PLAYERS_SETHEALTH("uhc.players.setHealth"),

    /**
     * Permission to revive a player.
     */
    PLAYERS_REVIVE("uhc.players.revive"),

    /**
     * Permission to change UHC settings.
     */
    SETTINGS("uhc.settings"),

    /**
     * Permission to set a UHC setting.
     */
    SETTINGS_SET("uhc.settings.set"),

    /**
     * Permission to open the settings menu.
     */
    MENU("uhc.menu"),

    /**
     * Permission to show game statistics.
     */
    STATS("uhc.stats"),

    /**
     * Permission to manage teams.
     */
    TEAM("uhc.team"),

    /**
     * Permission to create teams.
     */
    TEAM_CREATE("uhc.team.create"),

    /**
     * Permission to add a member to a team.
     */
    TEAM_ADD("uhc.team.add"),

    /**
     * Permission to remove a member from a team.
     */
    TEAM_REMOVE("uhc.team.remove"),

    /**
     * Permission to rename a team.
     */
    TEAM_RENAME("uhc.team.rename"),

    /**
     * Permission to leave a team.
     */
    TEAM_LEAVE("uhc.team.leave"),

    /**
     * Permission to list all teams.
     */
    TEAM_LIST("uhc.team.list"),

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
