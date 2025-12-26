package vch.uhc.misc;
import org.bukkit.ChatColor;
/**
 * Centralized message storage for UHC plugin
 * All user-facing messages should be defined here for easy editing
 */
public class Messages {
    
    public static String DEATH_PVP_FINAL(String victim, String killer) {
        return ChatColor.RED + "☠ " + ChatColor.YELLOW + victim + 
               ChatColor.GRAY + " was eliminated by " + 
               ChatColor.GOLD + killer + 
               ChatColor.DARK_RED + " [FINAL ELIMINATION]";
    }
    public static String DEATH_NATURAL_FINAL(String victim) {
        return ChatColor.RED + "☠ " + ChatColor.YELLOW + victim + 
               ChatColor.GRAY + " died" +
               ChatColor.DARK_RED + " [FINAL ELIMINATION]";
    }
    public static String DEATH_NATURAL_WITH_CAUSE_FINAL(String victim, String cause) {
        return ChatColor.RED + "☠ " + ChatColor.YELLOW + victim + 
               ChatColor.GRAY + " died (" + cause + ")" +
               ChatColor.DARK_RED + " [FINAL ELIMINATION]";
    }
    public static String DEATH_NATURAL_LIVES(String victim, int lives) {
        return ChatColor.RED + "☠ " + ChatColor.YELLOW + victim + 
               ChatColor.GRAY + " died" +
               ChatColor.AQUA + " [" + lives + " ❤ remaining]";
    }
    public static String DEATH_NATURAL_WITH_CAUSE_LIVES(String victim, String cause, int lives) {
        return ChatColor.RED + "☠ " + ChatColor.YELLOW + victim + 
               ChatColor.GRAY + " died (" + cause + ")" +
               ChatColor.AQUA + " [" + lives + " ❤ remaining]";
    }
    public static String DEATH_CHEST_COORDS(int x, int y, int z) {
        return ChatColor.GRAY + "Chest coordinates: " + 
               ChatColor.AQUA + "X: " + x + " Y: " + y + " Z: " + z;
    }
    public static String PLAYER_HEAD_NAME(String playerName) {
        return ChatColor.GOLD + playerName + "'s Head";
    }
    
    public static final String ELIMINATED = ChatColor.RED + "You have been eliminated. You are now a spectator.";
    public static final String ELIMINATED_TITLE = ChatColor.DARK_RED + "ELIMINATED";
    public static final String SPECTATOR_MODE_SUBTITLE = ChatColor.GRAY + "Spectator mode activated";
    public static String RESPAWN_MESSAGE(int lives) {
        return ChatColor.GREEN + "You have respawned. Lives remaining: " + ChatColor.GOLD + lives;
    }
    public static final String RESPAWN_TITLE = ChatColor.YELLOW + "RESPAWN";
    
    public static String RESPAWN_SUBTITLE(int lives) {
        return ChatColor.GRAY + "Lives: " + lives;
    }
    
    public static final String TEAM_USAGE = ChatColor.RED + "Usage: /uhc team <create|add|remove|rename|leave|list|info>";
    public static final String TEAM_UNKNOWN_SUBCOMMAND = ChatColor.RED + "Unknown subcommand. Use: create, add, remove, rename, leave, list, info";
    
    public static final String TEAM_MANUAL_ONLY = ChatColor.RED + "Teams can only be managed manually in MANUAL mode.";
    public static String TEAM_CURRENT_MODE(Settings.TeamMode mode) {
        return ChatColor.YELLOW + "Current mode: " + mode;
    }
    
    public static final String TEAM_PLAYERS_ONLY = ChatColor.RED + "Only players can create teams.";
    public static final String TEAM_CREATE_USAGE = ChatColor.RED + "Usage: /uhc team create <name>";
    public static final String TEAM_ALREADY_IN_TEAM = ChatColor.RED + "You are already in a team. Use /uhc team leave first.";
    public static final String TEAM_NAME_TAKEN = ChatColor.RED + "A team with that name already exists.";
    
    public static String TEAM_CREATED(String teamName) {
        return ChatColor.GREEN + "Team '" + teamName + "' created successfully.";
    }
    
    public static final String TEAM_YOU_ARE_LEADER = ChatColor.YELLOW + "You are the team leader.";
    public static final String TEAM_ADD_USAGE = ChatColor.RED + "Usage: /uhc team add <player> <team>";
    public static final String TEAM_PLAYER_NOT_FOUND = ChatColor.RED + "Player not found or offline.";
    
    public static String TEAM_PLAYER_HAS_TEAM(String playerName) {
        return ChatColor.RED + playerName + " is already in a team.";
    }
    
    public static String TEAM_NOT_FOUND(String teamName) {
        return ChatColor.RED + "Team '" + teamName + "' not found.";
    }
    
    public static String TEAM_FULL(int maxSize) {
        return ChatColor.RED + "Team is full. Maximum size: " + maxSize;
    }
    
    public static String TEAM_PLAYER_ADDED(String playerName, String teamName) {
        return ChatColor.GREEN + playerName + " added to team '" + teamName + "'.";
    }
    
    public static String TEAM_YOU_WERE_ADDED(String teamName) {
        return ChatColor.GREEN + "You have been added to team '" + teamName + "'.";
    }
    
    public static final String TEAM_REMOVE_USAGE = ChatColor.RED + "Usage: /uhc team remove <player>";
    
    public static String TEAM_PLAYER_NO_TEAM(String playerName) {
        return ChatColor.RED + playerName + " is not in any team.";
    }
    
    public static String TEAM_PLAYER_REMOVED(String playerName, String teamName) {
        return ChatColor.GREEN + playerName + " removed from team '" + teamName + "'.";
    }
    
    public static String TEAM_YOU_WERE_REMOVED(String teamName) {
        return ChatColor.YELLOW + "You have been removed from team '" + teamName + "'.";
    }
    
    public static final String TEAM_RENAME_USAGE = ChatColor.RED + "Usage: /uhc team rename <currentTeam> <newName>";
    
    public static String TEAM_RENAMED(String oldName, String newName) {
        return ChatColor.GREEN + "Team renamed from '" + oldName + "' to '" + newName + "'.";
    }
    
    public static String TEAM_YOU_WERE_RENAMED(String newName) {
        return ChatColor.YELLOW + "Your team has been renamed to '" + newName + "'.";
    }
    
    public static final String TEAM_NOT_IN_TEAM = ChatColor.RED + "You are not in any team.";
    public static final String TEAM_LEADER_ONLY = ChatColor.RED + "Only the team leader can rename the team.";
    public static final String PLAYER_NOT_FOUND = ChatColor.RED + "Player not found.";
    
    public static String TEAM_YOU_LEFT(String teamName) {
        return ChatColor.YELLOW + "You left team '" + teamName + "'.";
    }
    
    public static String TEAM_PLAYER_LEFT(String playerName) {
        return ChatColor.GRAY + playerName + " has left the team.";
    }
    
    public static final String TEAM_NONE_CREATED = ChatColor.YELLOW + "No teams have been created yet.";
    public static final String TEAM_LIST_HEADER = ChatColor.GOLD + "========== UHC Teams ==========";
    public static final String TEAM_LIST_FOOTER = ChatColor.GOLD + "================================";
    
    public static String TEAM_LIST_ENTRY(String teamName, int memberCount) {
        return ChatColor.AQUA + "▸ " + ChatColor.WHITE + teamName + 
               ChatColor.GRAY + " (" + memberCount + " members)";
    }
    
    public static String TEAM_LEADER(String leaderName) {
        return ChatColor.YELLOW + "  Leader: " + ChatColor.WHITE + leaderName;
    }
    
    public static final String TEAM_MEMBERS_PREFIX = ChatColor.GRAY + "  Members: ";
    public static final String TEAM_INFO_USAGE = ChatColor.RED + "Usage: /uhc team info <team>";
    
    public static String TEAM_INFO_HEADER(String teamName) {
        return ChatColor.GOLD + "======== Info: " + teamName + " ========";
    }
    
    public static String TEAM_TOTAL_MEMBERS(int count) {
        return ChatColor.YELLOW + "Total members: " + ChatColor.WHITE + count;
    }
    
    public static final String TEAM_MEMBER_LIST = ChatColor.AQUA + "Member list:";
    
    public static final String PVP_DISABLED_AGREEMENT = ChatColor.RED + "PvP is disabled during the agreement period!";
    
    public static String PVP_TIME_REMAINING(int minutes, int seconds) {
        return ChatColor.YELLOW + "Time remaining: " + minutes + ":" + String.format("%02d", seconds);
    }
    
    public static String AGREEMENT_WARNING_MINUTES(int minutes) {
        return ChatColor.YELLOW + "⚠ PvP disabled. Time remaining: " + 
               ChatColor.GOLD + minutes + " minutes";
    }
    
    public static String AGREEMENT_WARNING_SECONDS(int seconds) {
        return ChatColor.RED + "⚠ PvP will activate in " + seconds + " seconds!";
    }
    
    public static final String PVP_ACTIVATED_LINE1 = "";
    public static final String PVP_ACTIVATED_LINE2 = ChatColor.DARK_RED + "════════════════════════════════";
    public static final String PVP_ACTIVATED_LINE3 = ChatColor.RED + "⚔ " + ChatColor.BOLD + "PVP ACTIVATED" + ChatColor.RED + " ⚔";
    public static final String PVP_ACTIVATED_LINE4 = ChatColor.DARK_RED + "════════════════════════════════";
    public static final String PVP_ACTIVATED_LINE5 = "";
    
    public static final String PVP_ACTIVATED_TITLE = ChatColor.DARK_RED + "⚔ PVP ACTIVATED ⚔";
    public static final String PVP_ACTIVATED_SUBTITLE = ChatColor.YELLOW + "The agreement has ended!";
    
    public static final String SCOREBOARD_TITLE = ChatColor.GOLD + "" + ChatColor.BOLD + "UHC";
    
    public static String SCOREBOARD_TIME(int hours, int minutes, int seconds) {
        return ChatColor.YELLOW + "Time: " + ChatColor.WHITE + 
               String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    public static final String SCOREBOARD_PVP = ChatColor.YELLOW + "PvP: ";
    public static final String SCOREBOARD_PVP_DISABLED = ChatColor.RED + "✖ Disabled";
    public static final String SCOREBOARD_PVP_ENABLED = ChatColor.GREEN + "✔ Enabled";
    
    public static String SCOREBOARD_ALIVE_PLAYERS(long alive, long total) {
        return ChatColor.AQUA + "Alive: " + ChatColor.WHITE + alive + "/" + total;
    }
    
    public static String SCOREBOARD_TEAMS(long aliveTeams) {
        return ChatColor.GREEN + "Teams: " + ChatColor.WHITE + aliveTeams;
    }
    
    public static String SCOREBOARD_BORDER(int borderSize) {
        return ChatColor.LIGHT_PURPLE + "Border: " + ChatColor.WHITE + "±" + borderSize;
    }
    
    public static String SCOREBOARD_YOUR_KILLS(int kills) {
        return ChatColor.GOLD + "Your Kills: " + ChatColor.WHITE + kills;
    }
    
    public static String SCOREBOARD_YOUR_LIVES(int lives) {
        return ChatColor.RED + "Your Lives: " + ChatColor.WHITE + lives;
    }
    
    public static final String VICTORY_HEADER = ChatColor.GOLD + "════════════════════════════════";
    public static final String VICTORY_TITLE_LINE = ChatColor.YELLOW + "       🏆 " + ChatColor.BOLD + "VICTORY!" + ChatColor.YELLOW + " 🏆";
    public static final String VICTORY_FOOTER = ChatColor.GOLD + "════════════════════════════════";
    
    public static String VICTORY_WINNER(String winnerName) {
        return ChatColor.GREEN + "Winner: " + ChatColor.GOLD + ChatColor.BOLD + winnerName;
    }
    
    public static String VICTORY_DURATION(String duration) {
        return ChatColor.AQUA + "Game duration: " + ChatColor.WHITE + duration;
    }
    
    public static String VICTORY_KILLS(int kills) {
        return ChatColor.AQUA + "Winner's kills: " + ChatColor.WHITE + kills;
    }
    
    public static String VICTORY_PLAYER_TITLE(String winnerName) {
        return ChatColor.GOLD + "🏆 " + winnerName + " 🏆";
    }
    
    public static final String VICTORY_PLAYER_SUBTITLE = ChatColor.YELLOW + "Has won the UHC!";
    
    public static String VICTORY_TEAM_WINNER(String teamName) {
        return ChatColor.GREEN + "Winning team: " + ChatColor.GOLD + ChatColor.BOLD + teamName;
    }
    
    public static final String VICTORY_TEAM_MEMBERS = ChatColor.AQUA + "Team members:";
    
    public static String VICTORY_TEAM_TOTAL_KILLS(int kills) {
        return ChatColor.AQUA + "Team total kills: " + ChatColor.WHITE + kills;
    }
    
    public static String VICTORY_TEAM_TITLE(String teamName) {
        return ChatColor.GOLD + "🏆 " + teamName + " 🏆";
    }
    
    public static final String DRAW_HEADER = ChatColor.GRAY + "════════════════════════════════";
    public static final String DRAW_TITLE_LINE = ChatColor.YELLOW + "         ⚔ " + ChatColor.BOLD + "DRAW" + ChatColor.YELLOW + " ⚔";
    public static final String DRAW_MESSAGE = ChatColor.RED + "All players have been eliminated!";
    public static final String DRAW_FOOTER = ChatColor.GRAY + "════════════════════════════════";
    public static final String DRAW_TITLE = ChatColor.GRAY + "⚔ DRAW ⚔";
    public static final String DRAW_SUBTITLE = ChatColor.RED + "No winner";
    
    public static String PROXIMITY_NEARBY_PLAYER(String playerName) {
        return "You are close to " + playerName;
    }
    
    public static final String COMMAND_SPECIFY_SUBCOMMAND = ChatColor.RED + "Please specify a subcommand.";
    public static final String COMMAND_UNKNOWN_SUBCOMMAND = ChatColor.RED + "Unknown subcommand.";
    
    public static final String MAIN_PLAYERS_ONLY_JOIN = ChatColor.RED + "Only players can join the UHC.";
    public static final String MAIN_PLAYERS_ONLY_LEAVE = ChatColor.RED + "Only players can leave the UHC.";
    public static final String MAIN_JOINED_UHC = ChatColor.GREEN + "You have joined the UHC.";
    public static final String MAIN_LEFT_UHC = ChatColor.RED + "You have left the UHC.";
    
    public static final String GAME_STARTED = ChatColor.GREEN + "UHC started.";
    public static final String GAME_CANCELLED = ChatColor.RED + "UHC cancelled.";
    
    public static final String SETTINGS_SAVED = ChatColor.GREEN + "✓ Configuration saved successfully!";
    public static final String SETTINGS_LOADED = ChatColor.GREEN + "✓ Configuration loaded successfully!";
    
    public static final String INFO_HEADER = ChatColor.YELLOW + "UHC Information:";
    
    public static String INFO_GAME_STATUS(Settings.GameStatus status) {
        return ChatColor.GOLD + "\n- Game Status: " + status;
    }
    
    public static String INFO_TEAM_MODE(Settings.TeamMode mode) {
        return ChatColor.GOLD + "\n- Team mode: " + mode;
    }
    
    public static String INFO_TEAM_SIZE(int size) {
        return ChatColor.GOLD + "\n- Team size: " + size;
    }
    
    public static String INFO_PLAYER_LIVES(int lives) {
        return ChatColor.GOLD + "\n- Player lives: " + lives;
    }
    
    public static String INFO_MAX_WORLD_SIZE(int size) {
        return ChatColor.GOLD + "\n- Max World Size: " + size;
    }
    
    public static String INFO_MIN_WORLD_SIZE(int size) {
        return ChatColor.GOLD + "\n- Min World Size: " + size;
    }
    
    public static String INFO_GAME_TIME(int h, int m, int s) {
        return ChatColor.GOLD + "  Game Time: " + ChatColor.WHITE + String.format("%02d:%02d:%02d", h, m, s);
    }
    
    public static String INFO_AGREEMENT_TIME(int h, int m, int s) {
        return ChatColor.GOLD + "  Agreement Time: " + ChatColor.WHITE + String.format("%02d:%02d:%02d", h, m, s);
    }
    
    public static String INFO_MIN_BORDER_TIME(int h, int m, int s) {
        return ChatColor.GOLD + "  Min Border Time: " + ChatColor.WHITE + String.format("%02d:%02d:%02d", h, m, s);
    }
    
    public static String INFO_MAX_INGAME_TEAMS_TIME(int h, int m, int s) {
        return ChatColor.GOLD + "  Max In-Game Teams Time: " + ChatColor.WHITE + String.format("%02d:%02d:%02d", h, m, s);
    }
    
    public static String INFO_TEAMS_COUNT(int count) {
        return ChatColor.GOLD + "  Teams: " + ChatColor.WHITE + count;
    }
    
    public static String INFO_PLAYERS_COUNT(int count) {
        return ChatColor.GOLD + "  Players: " + ChatColor.WHITE + count;
    }
    
    public static final String INFO_RECIPES_HEADER = ChatColor.GOLD + "  Recipes:";
    
    public static final String INFO_SEPARATOR = ChatColor.GOLD + "" + ChatColor.STRIKETHROUGH + "======================";
    public static final String INFO_BORDER = ChatColor.YELLOW + "⬛" + ChatColor.GOLD + ChatColor.STRIKETHROUGH + "====================" + ChatColor.YELLOW + "⬛";
    
    public static final String PLAYER_JOINED_UHC = ChatColor.GREEN + "You have joined the UHC.";
    
    public static final String JOINED_UHC = ChatColor.GREEN + "You have joined the UHC.";
    public static final String LEFT_UHC = ChatColor.RED + "You have left the UHC.";
    public static final String ONLY_PLAYERS_JOIN = ChatColor.RED + "Only players can join the UHC.";
    public static final String ONLY_PLAYERS_LEAVE = ChatColor.RED + "Only players can leave the UHC.";
    
    public static final String SETTINGS_SPECIFY_SUBCOMMAND = ChatColor.RED + "Please specify a settings subcommand.";
    public static final String SETTINGS_SPECIFY_VALUE = ChatColor.RED + "Please specify a setting and value.";
    public static final String SETTINGS_UNKNOWN_SUBCOMMAND = ChatColor.RED + "Unknown subcommand.";
    public static final String SETTINGS_UNKNOWN = ChatColor.RED + "Unknown setting.";
    
    public static final String SETTINGS_TEAM_MODE_UNKNOWN = ChatColor.RED + "Unknown team mode.";
    public static String SETTINGS_TEAM_MODE_SET(String mode) {
        return ChatColor.GREEN + "Team mode set to " + mode + ".";
    }
    
    public static String SETTINGS_TEAM_SIZE_SET(int size) {
        return ChatColor.GREEN + "Team size set to " + size + ".";
    }
    public static final String SETTINGS_TEAM_SIZE_INVALID = ChatColor.RED + "Invalid team size.";
    
    public static String SETTINGS_PLAYER_LIVES_SET(int lives) {
        return ChatColor.GREEN + "Player lives set to " + lives + ".";
    }
    public static final String SETTINGS_PLAYER_LIVES_INVALID = ChatColor.RED + "Invalid player lives.";
    
    public static String SETTINGS_MAX_WORLD_SIZE_SET(int size) {
        return ChatColor.GREEN + "Max world size set to " + size + ".";
    }
    public static final String SETTINGS_MAX_WORLD_SIZE_INVALID = ChatColor.RED + "Invalid max world size.";
    
    public static String SETTINGS_MIN_WORLD_SIZE_SET(int size) {
        return ChatColor.GREEN + "Min world size set to " + size + ".";
    }
    public static final String SETTINGS_MIN_WORLD_SIZE_INVALID = ChatColor.RED + "Invalid min world size.";
    
    public static String SETTINGS_HOURS_SET(int hours) {
        return ChatColor.GREEN + "Hours set to " + hours + ".";
    }
    public static final String SETTINGS_HOURS_INVALID = ChatColor.RED + "Invalid hours.";
    
    public static String SETTINGS_MINUTES_SET(int minutes) {
        return ChatColor.GREEN + "Minutes set to " + minutes + ".";
    }
    public static final String SETTINGS_MINUTES_INVALID = ChatColor.RED + "Invalid minutes.";
    
    public static String SETTINGS_SECONDS_SET(int seconds) {
        return ChatColor.GREEN + "Seconds set to " + seconds + ".";
    }
    public static final String SETTINGS_SECONDS_INVALID = ChatColor.RED + "Invalid seconds.";
    
    public static String SETTINGS_AGREEMENT_HOURS_SET(int hours) {
        return ChatColor.GREEN + "Agreement hours set to " + hours + ".";
    }
    public static final String SETTINGS_AGREEMENT_HOURS_INVALID = ChatColor.RED + "Invalid agreement hours.";
    
    public static String SETTINGS_AGREEMENT_MINUTES_SET(int minutes) {
        return ChatColor.GREEN + "Agreement minutes set to " + minutes + ".";
    }
    public static final String SETTINGS_AGREEMENT_MINUTES_INVALID = ChatColor.RED + "Invalid agreement minutes.";
    
    public static String SETTINGS_AGREEMENT_SECONDS_SET(int seconds) {
        return ChatColor.GREEN + "Agreement seconds set to " + seconds + ".";
    }
    public static final String SETTINGS_AGREEMENT_SECONDS_INVALID = ChatColor.RED + "Invalid agreement seconds.";
    
    public static String SETTINGS_MIN_BORDER_HOURS_SET(int hours) {
        return ChatColor.GREEN + "Min world border hours set to " + hours + ".";
    }
    public static final String SETTINGS_MIN_BORDER_HOURS_INVALID = ChatColor.RED + "Invalid min world border hours.";
    
    public static String SETTINGS_MIN_BORDER_MINUTES_SET(int minutes) {
        return ChatColor.GREEN + "Min world border minutes set to " + minutes + ".";
    }
    public static final String SETTINGS_MIN_BORDER_MINUTES_INVALID = ChatColor.RED + "Invalid min world border minutes.";
    
    public static String SETTINGS_MIN_BORDER_SECONDS_SET(int seconds) {
        return ChatColor.GREEN + "Min world border seconds set to " + seconds + ".";
    }
    public static final String SETTINGS_MIN_BORDER_SECONDS_INVALID = ChatColor.RED + "Invalid min world border seconds.";
    
    public static String SETTINGS_MAX_TEAM_HOURS_SET(int hours) {
        return ChatColor.GREEN + "Max team in game hours set to " + hours + ".";
    }
    public static final String SETTINGS_MAX_TEAM_HOURS_INVALID = ChatColor.RED + "Invalid max team in game hours.";
    
    public static String SETTINGS_MAX_TEAM_MINUTES_SET(int minutes) {
        return ChatColor.GREEN + "Max team in game minutes set to " + minutes + ".";
    }
    public static final String SETTINGS_MAX_TEAM_MINUTES_INVALID = ChatColor.RED + "Invalid max team in game minutes.";
    
    public static String SETTINGS_MAX_TEAM_SECONDS_SET(int seconds) {
        return ChatColor.GREEN + "Max team in game seconds set to " + seconds + ".";
    }
    public static final String SETTINGS_MAX_TEAM_SECONDS_INVALID = ChatColor.RED + "Invalid max team in game seconds.";
    
    public static final String SETTINGS_RECIPE_SPECIFY = ChatColor.RED + "Please specify a recipe name and value.";
    public static final String SETTINGS_RECIPE_INVALID_VALUE = ChatColor.RED + "Invalid recipe value. Use 'enabled' or 'disabled'.";
    public static final String SETTINGS_RECIPE_UNKNOWN = ChatColor.RED + "Unknown recipe name.";
    public static String SETTINGS_RECIPE_SET(String name, boolean enabled) {
        return ChatColor.GREEN + "Recipe " + name + " set to " + (enabled ? "enabled" : "disabled") + ".";
    }
    
    public static final String SETTINGS_TIME_FORMAT_INVALID = ChatColor.RED + "Invalid time format. Use HH:MM:SS (e.g., 01:30:00)";
    public static String SETTINGS_GAME_TIME_SET(String time) {
        return ChatColor.GREEN + "Game time set to " + time;
    }
    public static String SETTINGS_AGREEMENT_TIME_SET(String time) {
        return ChatColor.GREEN + "Agreement time set to " + time;
    }
    public static String SETTINGS_MIN_BORDER_TIME_SET(String time) {
        return ChatColor.GREEN + "Min world border time set to " + time;
    }
    public static String SETTINGS_MAX_TEAM_TIME_SET(String time) {
        return ChatColor.GREEN + "Max team in-game time set to " + time;
    }
    
    public static final String PLAYER_SPECIFY_SUBCOMMAND = ChatColor.RED + "Please specify a player subcommand.";
    public static final String PLAYER_UNKNOWN_SUBCOMMAND = ChatColor.RED + "Unknown subcommand.";
    public static final String PLAYER_LIST_HEADER = ChatColor.YELLOW + "UHC Players:";
    
    public static String PLAYER_LIST_ITEM(String name, int lives) {
        return ChatColor.AQUA + "- " + ChatColor.GOLD + name + ChatColor.AQUA + " (" + lives + " lives)";
    }
    public static final String PLAYER_LIST_NONE = ChatColor.AQUA + "None";
    
    public static String PLAYER_LIVES_SET(String playerName, int lives) {
        return ChatColor.GREEN + "Set lives for " + playerName + " to " + lives;
    }
    public static String PLAYER_LIVES_ERROR(String playerName) {
        return ChatColor.RED + "Error setting lives for " + playerName;
    }
    
    public static String PLAYER_HEALTH_SET(String playerName, double health) {
        return ChatColor.GREEN + "Set health for " + playerName + " to " + health;
    }
    public static String PLAYER_HEALTH_ERROR(String playerName) {
        return ChatColor.RED + "Error setting health for " + playerName;
    }
    
    public static String PLAYER_REVIVED(String playerName) {
        return ChatColor.GREEN + "Revived " + playerName;
    }
    public static String PLAYER_REVIVE_ERROR(String playerName) {
        return ChatColor.RED + "Error reviving " + playerName;
    }
    
    public static String CHAT_GLOBAL_PREFIX(String randomName, String message) {
        return ChatColor.AQUA + "[GLOBAL] " + randomName + ": " + ChatColor.WHITE + message;
    }
    
    public static String CHAT_TEAM_PREFIX(String playerName, String message) {
        return ChatColor.GREEN + "[TEAM] " + playerName + ": " + ChatColor.WHITE + message;
    }
    
    public static String ADVANCEMENT_MADE(String randomName, String advancementTitle) {
        return ChatColor.YELLOW + randomName + ChatColor.WHITE + " has made the advancement " + 
               ChatColor.GREEN + "[" + advancementTitle + "]";
    }
    
    public static final String SKIN_SHUFFLE_BORDER = ChatColor.GOLD + "═══════════════════════════════════════";
    public static final String SKIN_SHUFFLE_TITLE = ChatColor.YELLOW + "🎭 " + ChatColor.BOLD + "SKINS SHUFFLED" + ChatColor.RESET;
    public static final String SKIN_SHUFFLE_DESCRIPTION = ChatColor.GRAY + "Identities have been mixed!";
    public static final String SKIN_SHUFFLE_HINT = ChatColor.GRAY + "Hit a player to reveal their identity";
    
    public static final String SKIN_REVEAL_BORDER = ChatColor.GOLD + "═════════════════════════════════════";
    public static final String SKIN_REVEAL_TITLE = ChatColor.YELLOW + "🎭 " + ChatColor.BOLD + "IDENTITY REVEALED!";
    
    public static String SKIN_REVEAL_SKIN(String skinName) {
        return ChatColor.WHITE + "  Skin: " + ChatColor.AQUA + skinName;
    }
    
    public static String SKIN_REVEAL_REAL(String realName) {
        return ChatColor.WHITE + "  Real: " + ChatColor.GREEN + ChatColor.BOLD + realName;
    }
    
    public static String SKIN_REVEAL_BROADCAST(String attackerName, String revealedName) {
        return ChatColor.YELLOW + "⚠ " + 
               ChatColor.WHITE + attackerName + 
               ChatColor.GRAY + " revealed the identity of " +
               ChatColor.GREEN + revealedName + 
               ChatColor.GRAY + "!";
    }
    
    public static final String SKIN_RESTORE_SUCCESS = ChatColor.GREEN + "✓ All skins have been restored";
    
    public static final String SKINS_USAGE = ChatColor.RED + "Usage: /uhc skins <shuffle|restore|info>";
    public static final String SKINS_SHUFFLED = ChatColor.GREEN + "✓ Skins shuffled successfully";
    public static final String SKINS_RESTORED = ChatColor.GREEN + "✓ Skins restored to original values";
    public static final String SKINS_STATUS_SHUFFLED = ChatColor.YELLOW + "Status: " + ChatColor.GREEN + "Skins shuffled";
    public static final String SKINS_STATUS_NORMAL = ChatColor.YELLOW + "Status: " + ChatColor.GRAY + "Normal skins";
    public static final String SKINS_UNKNOWN_SUBCOMMAND = ChatColor.RED + "Unknown subcommand. Use: shuffle, restore, info";
    
    public static String SKIN_STATS(int revealed, int hidden) {
        return ChatColor.YELLOW + "Skins: " + 
               ChatColor.GREEN + revealed + " revealed" + 
               ChatColor.GRAY + " | " +
               ChatColor.RED + hidden + " hidden";
    }
}
