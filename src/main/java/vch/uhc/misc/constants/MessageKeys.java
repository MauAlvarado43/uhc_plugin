package vch.uhc.misc.constants;

/**
 * Centralized message keys for language localization.
 * Prevents hardcoded message keys throughout the codebase.
 */
public final class MessageKeys {

    // Prevent instantiation
    private MessageKeys() {
        throw new java.lang.AssertionError("Cannot instantiate constants class");
    }

    // ========================================
    // Death Messages
    // ========================================
    
    public static final String DEATH_PVP_FINAL = "death.pvp.final";
    public static final String DEATH_NATURAL_FINAL = "death.natural.final";
    public static final String DEATH_NATURAL_WITH_CAUSE_FINAL = "death.natural.withCause.final";
    public static final String DEATH_NATURAL_LIVES = "death.natural.lives";
    public static final String DEATH_NATURAL_WITH_CAUSE_LIVES = "death.natural.withCause.lives";
    public static final String DEATH_CHEST_COORDS = "death.chest.coords";
    public static final String DEATH_PLAYER_HEAD_NAME = "death.playerHead.name";

    // ========================================
    // Elimination Messages
    // ========================================
    
    public static final String ELIMINATION_MESSAGE = "elimination.message";
    public static final String ELIMINATION_TITLE = "elimination.title";
    public static final String ELIMINATION_SUBTITLE = "elimination.subtitle";

    // ========================================
    // Respawn Messages
    // ========================================
    
    public static final String RESPAWN_MESSAGE = "respawn.message";
    public static final String RESPAWN_TITLE = "respawn.title";
    public static final String RESPAWN_SUBTITLE = "respawn.subtitle";

    // ========================================
    // Team Messages
    // ========================================
    
    public static final String TEAM_USAGE = "team.usage";
    public static final String TEAM_UNKNOWN = "team.unknown";
    public static final String TEAM_MANUAL_ONLY = "team.manualOnly";
    public static final String TEAM_CURRENT_MODE = "team.currentMode";
    public static final String TEAM_PLAYERS_ONLY = "team.playersOnly";
    public static final String TEAM_CREATE_USAGE = "team.create.usage";
    public static final String TEAM_ALREADY_IN_TEAM = "team.alreadyInTeam";
    public static final String TEAM_NAME_TAKEN = "team.nameTaken";
    public static final String TEAM_FORMED = "uhc.teamFormed";
    public static final String TEAM_YOU_JOINED = "uhc.youJoined";
    public static final String TEAM_PLAYER_JOINED = "uhc.playerJoinedTeam";
    public static final String TEAM_TEAMS_MERGED = "uhc.teamsMerged";

    // ========================================
    // Game State Messages
    // ========================================
    
    public static final String GAME_STARTING = "game.starting";
    public static final String GAME_STARTED = "game.started";
    public static final String GAME_PAUSED = "game.paused";
    public static final String GAME_RESUMED = "game.resumed";
    public static final String GAME_STOPPED = "game.stopped";
    public static final String GAME_ENDED = "game.ended";

    // ========================================
    // Error Messages
    // ========================================
    
    public static final String ERROR_NO_PERMISSION = "error.noPermission";
    public static final String ERROR_PLAYER_ONLY = "error.playerOnly";
    public static final String ERROR_INVALID_ARGUMENTS = "error.invalidArguments";
    public static final String ERROR_UNKNOWN_COMMAND = "error.unknownCommand";
    public static final String ERROR_GAME_NOT_RUNNING = "error.gameNotRunning";
    public static final String ERROR_GAME_ALREADY_RUNNING = "error.gameAlreadyRunning";

    // ========================================
    // Info Messages
    // ========================================
    
    public static final String INFO_AGREEMENT_PHASE = "info.agreementPhase";
    public static final String INFO_BORDER_SHRINKING = "info.borderShrinking";
    public static final String INFO_BUFFS_APPLIED = "info.buffsApplied";
    public static final String INFO_END_PORTAL_OPENED = "info.endPortalOpened";
    public static final String INFO_SHULKER_ENABLED = "info.shulkerEnabled";
    public static final String INFO_LOCATOR_BAR_ENABLED = "info.locatorBarEnabled";

    // ========================================
    // Command Messages
    // ========================================
    
    public static final String COMMAND_HELP = "command.help";
    public static final String COMMAND_RELOAD_SUCCESS = "command.reload.success";
    public static final String COMMAND_START_SUCCESS = "command.start.success";
    public static final String COMMAND_STOP_SUCCESS = "command.stop.success";
    public static final String COMMAND_PAUSE_SUCCESS = "command.pause.success";
    public static final String COMMAND_RESUME_SUCCESS = "command.resume.success";

}
