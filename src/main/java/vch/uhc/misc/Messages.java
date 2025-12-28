package vch.uhc.misc;

import vch.uhc.UHC;
import vch.uhc.misc.enums.GameState;
import vch.uhc.misc.enums.TeamMode;

public class Messages {

    private static LanguageManager lang() {
        UHC plugin = UHC.getPlugin();
        if (plugin == null) {
            throw new IllegalStateException("UHC plugin not initialized");
        }
        LanguageManager lm = plugin.getLanguageManager();
        if (lm == null) {
            throw new IllegalStateException("LanguageManager not initialized");
        }
        return lm;
    }

    public static String DEATH_PVP_FINAL(String victim, String killer) {
        return lang().getMessage("death.pvp.final", victim, killer);
    }

    public static String DEATH_NATURAL_FINAL(String victim) {
        return lang().getMessage("death.natural.final", victim);
    }

    public static String DEATH_NATURAL_WITH_CAUSE_FINAL(String victim, String cause) {
        return lang().getMessage("death.natural.withCause.final", victim, cause);
    }

    public static String DEATH_NATURAL_LIVES(String victim, int lives) {
        return lang().getMessage("death.natural.lives", victim, lives);
    }

    public static String DEATH_NATURAL_WITH_CAUSE_LIVES(String victim, String cause, int lives) {
        return lang().getMessage("death.natural.withCause.lives", victim, cause, lives);
    }

    public static String DEATH_CHEST_COORDS(int x, int y, int z) {
        return lang().getMessage("death.chest.coords", x, y, z);
    }

    public static String PLAYER_HEAD_NAME(String playerName) {
        return lang().getMessage("death.playerHead.name", playerName);
    }

    public static String ELIMINATED() {
        return lang().getMessage("elimination.message");
    }

    public static String BAN_ELIMINATED() {
        return lang().getMessage("elimination.ban_message");
    }
    

    public static String ELIMINATED_TITLE() {
        return lang().getMessage("elimination.title");
    }

    public static String SPECTATOR_MODE_SUBTITLE() {
        return lang().getMessage("elimination.subtitle");
    }

    public static String RESPAWN_MESSAGE(int lives) {
        return lang().getMessage("respawn.message", lives);
    }

    public static String RESPAWN_TITLE() {
        return lang().getMessage("respawn.title");
    }

    public static String RESPAWN_SUBTITLE(int lives) {
        return lang().getMessage("respawn.subtitle", lives);
    }

    public static String TEAM_USAGE() {
        return lang().getMessage("team.usage");
    }

    public static String TEAM_UNKNOWN_SUBCOMMAND() {
        return lang().getMessage("team.unknown");
    }

    public static String TEAM_MANUAL_ONLY() {
        return lang().getMessage("team.manualOnly");
    }

    public static String TEAM_CURRENT_MODE(TeamMode mode) {
        return lang().getMessage("team.currentMode", mode.toString());
    }

    public static String TEAM_PLAYERS_ONLY() {
        return lang().getMessage("team.playersOnly");
    }

    public static String TEAM_CREATE_USAGE() {
        return lang().getMessage("team.create.usage");
    }

    public static String TEAM_ALREADY_IN_TEAM() {
        return lang().getMessage("team.alreadyInTeam");
    }

    public static String TEAM_NAME_TAKEN() {
        return lang().getMessage("team.nameTaken");
    }

    public static String TEAM_CREATED(String teamName) {
        return lang().getMessage("team.created", teamName);
    }

    public static String TEAM_CREATED_WITH_LEADER(String teamName, String leaderName) {
        return lang().getMessage("team.createdWithLeader", teamName, leaderName);
    }

    public static String TEAM_NO_PLAYERS_AVAILABLE() {
        return lang().getMessage("team.noPlayersAvailable");
    }

    public static String TEAM_YOU_ARE_LEADER() {
        return lang().getMessage("team.youAreLeader");
    }

    public static String TEAM_ADD_USAGE() {
        return lang().getMessage("team.add.usage");
    }

    public static String TEAM_PLAYER_NOT_FOUND() {
        return lang().getMessage("team.playerNotFound");
    }

    public static String TEAM_PLAYER_HAS_TEAM(String playerName) {
        return lang().getMessage("team.playerHasTeam", playerName);
    }

    public static String TEAM_NOT_FOUND(String teamName) {
        return lang().getMessage("team.notFound", teamName);
    }

    public static String TEAM_FULL(int maxSize) {
        return lang().getMessage("team.full", maxSize);
    }

    public static String TEAM_PLAYER_ADDED(String playerName, String teamName) {
        return lang().getMessage("team.playerAdded", playerName, teamName);
    }

    public static String TEAM_YOU_WERE_ADDED(String teamName) {
        return lang().getMessage("team.youWereAdded", teamName);
    }

    public static String TEAM_REMOVE_USAGE() {
        return lang().getMessage("team.remove.usage");
    }

    public static String TEAM_PLAYER_NO_TEAM(String playerName) {
        return lang().getMessage("team.playerNoTeam", playerName);
    }

    public static String TEAM_PLAYER_REMOVED(String playerName, String teamName) {
        return lang().getMessage("team.playerRemoved", playerName, teamName);
    }

    public static String TEAM_YOU_WERE_REMOVED(String teamName) {
        return lang().getMessage("team.youWereRemoved", teamName);
    }

    public static String TEAM_RENAME_USAGE() {
        return lang().getMessage("team.rename.usage");
    }

    public static String TEAM_RENAMED(String oldName, String newName) {
        return lang().getMessage("team.renamed", oldName, newName);
    }

    public static String TEAM_YOU_WERE_RENAMED(String newName) {
        return lang().getMessage("team.youWereRenamed", newName);
    }

    public static String TEAM_NOT_IN_TEAM() {
        return lang().getMessage("team.notInTeam");
    }

    public static String TEAM_LEADER_ONLY() {
        return lang().getMessage("team.leaderOnly");
    }

    public static String PLAYER_NOT_FOUND() {
        return lang().getMessage("team.playerNotFound");
    }

    public static String TEAM_YOU_LEFT(String teamName) {
        return lang().getMessage("team.youLeft", teamName);
    }

    public static String TEAM_PLAYER_LEFT(String playerName) {
        return lang().getMessage("team.playerLeft", playerName);
    }

    public static String TEAM_NONE_CREATED() {
        return lang().getMessage("team.noneCreated");
    }

    public static String TEAM_LIST_HEADER() {
        return lang().getMessage("team.list.header");
    }

    public static String TEAM_LIST_FOOTER() {
        return lang().getMessage("team.list.footer");
    }

    public static String TEAM_LIST_ENTRY(String teamName, int memberCount) {
        return lang().getMessage("team.list.entry", teamName, memberCount);
    }

    public static String TEAM_LEADER(String leaderName) {
        return lang().getMessage("team.leader", leaderName);
    }

    public static String TEAM_MEMBERS_PREFIX() {
        return lang().getMessage("team.membersPrefix");
    }

    public static String TEAM_INFO_USAGE() {
        return lang().getMessage("team.info.usage");
    }

    public static String TEAM_INFO_HEADER(String teamName) {
        return lang().getMessage("team.info.header", teamName);
    }

    public static String TEAM_TOTAL_MEMBERS(int count) {
        return lang().getMessage("team.totalMembers", count);
    }

    public static String TEAM_MEMBER_LIST() {
        return lang().getMessage("team.memberList");
    }

    public static String PVP_DISABLED_AGREEMENT() {
        return lang().getMessage("pvp.disabled");
    }

    public static String PVP_TIME_REMAINING(int minutes, int seconds) {
        return lang().getMessage("pvp.timeRemaining", minutes, String.format("%02d", seconds));
    }

    public static String AGREEMENT_WARNING_MINUTES(int minutes) {
        return lang().getMessage("pvp.agreementWarning.minutes", minutes);
    }

    public static String AGREEMENT_WARNING_SECONDS(int seconds) {
        return lang().getMessage("pvp.agreementWarning.seconds", seconds);
    }

    public static String PVP_ACTIVATED_LINE1() {
        return lang().getMessage("pvp.activated.line1");
    }

    public static String PVP_ACTIVATED_LINE2() {
        return lang().getMessage("pvp.activated.line2");
    }

    public static String PVP_ACTIVATED_LINE3() {
        return lang().getMessage("pvp.activated.line3");
    }

    public static String PVP_ACTIVATED_LINE4() {
        return lang().getMessage("pvp.activated.line4");
    }

    public static String PVP_ACTIVATED_LINE5() {
        return lang().getMessage("pvp.activated.line5");
    }

    public static String PVP_ACTIVATED_TITLE() {
        return lang().getMessage("pvp.activated.title");
    }

    public static String PVP_ACTIVATED_SUBTITLE() {
        return lang().getMessage("pvp.activated.subtitle");
    }

    public static String SCOREBOARD_TITLE() {
        return lang().getMessage("scoreboard.title");
    }

    public static String SCOREBOARD_TIME(int hours, int minutes, int seconds) {
        return lang().getMessage("scoreboard.time", String.format("%02d", hours), String.format("%02d", minutes), String.format("%02d", seconds));
    }

    public static String SCOREBOARD_PVP() {
        return lang().getMessage("scoreboard.pvp");
    }

    public static String SCOREBOARD_PVP_DISABLED() {
        return lang().getMessage("scoreboard.pvp.disabled");
    }

    public static String SCOREBOARD_PVP_ENABLED() {
        return lang().getMessage("scoreboard.pvp.enabled");
    }

    public static String SCOREBOARD_ALIVE_PLAYERS(long alive, long total) {
        return lang().getMessage("scoreboard.alivePlayers", alive, total);
    }

    public static String SCOREBOARD_TEAMS(long aliveTeams) {
        return lang().getMessage("scoreboard.teams", aliveTeams);
    }

    public static String SCOREBOARD_BORDER(int borderSize) {
        return lang().getMessage("scoreboard.border", borderSize);
    }

    public static String SCOREBOARD_YOUR_KILLS(int kills) {
        return lang().getMessage("scoreboard.yourKills", kills);
    }

    public static String SCOREBOARD_YOUR_LIVES(int lives) {
        return lang().getMessage("scoreboard.yourLives", lives);
    }

    public static String VICTORY_HEADER() {
        return lang().getMessage("victory.header");
    }

    public static String VICTORY_TITLE_LINE() {
        return lang().getMessage("victory.titleLine");
    }

    public static String VICTORY_FOOTER() {
        return lang().getMessage("victory.footer");
    }

    public static String VICTORY_WINNER(String winnerName) {
        return lang().getMessage("victory.winner", winnerName);
    }

    public static String VICTORY_DURATION(String duration) {
        return lang().getMessage("victory.duration", duration);
    }

    public static String VICTORY_KILLS(int kills) {
        return lang().getMessage("victory.kills", kills);
    }

    public static String VICTORY_PLAYER_TITLE(String winnerName) {
        return lang().getMessage("victory.player.title", winnerName);
    }

    public static String VICTORY_PLAYER_SUBTITLE() {
        return lang().getMessage("victory.player.subtitle");
    }

    public static String VICTORY_TEAM_WINNER(String teamName) {
        return lang().getMessage("victory.team.winner", teamName);
    }

    public static String VICTORY_TEAM_MEMBERS() {
        return lang().getMessage("victory.team.members");
    }

    public static String VICTORY_TEAM_TOTAL_KILLS(int kills) {
        return lang().getMessage("victory.team.totalKills", kills);
    }

    public static String VICTORY_TEAM_TITLE(String teamName) {
        return lang().getMessage("victory.team.title", teamName);
    }

    public static String DRAW_HEADER() {
        return lang().getMessage("draw.header");
    }

    public static String DRAW_TITLE_LINE() {
        return lang().getMessage("draw.titleLine");
    }

    public static String DRAW_MESSAGE() {
        return lang().getMessage("draw.message");
    }

    public static String DRAW_FOOTER() {
        return lang().getMessage("draw.footer");
    }

    public static String DRAW_TITLE() {
        return lang().getMessage("draw.title");
    }

    public static String DRAW_SUBTITLE() {
        return lang().getMessage("draw.subtitle");
    }

    public static String UHC_TEAM_FORMED(String playerName) {
        return lang().getMessage("uhc.teamFormed", playerName);
    }

    public static String UHC_YOU_JOINED(String teamName) {
        return lang().getMessage("uhc.youJoined", teamName);
    }

    public static String UHC_PLAYER_JOINED_TEAM(String playerName) {
        return lang().getMessage("uhc.playerJoinedTeam", playerName);
    }

    public static String UHC_TEAMS_MERGED(String teamName) {
        return lang().getMessage("uhc.teamsMerged", teamName);
    }

    public static String PVP_WARNING_SECONDS(int seconds) {
        return lang().getMessage("pvp.warningSeconds", seconds);
    }

    public static String PVP_ACTIVATED() {
        return lang().getMessage("pvp.activated");
    }

    public static String PVP_ACTIVATED_LINE() {
        return lang().getMessage("pvp.activatedLine");
    }

    public static String VICTORY_WINNER_SOLO(String winnerName) {
        return lang().getMessage("victory.winnerSolo", winnerName);
    }

    public static String VICTORY_GAME_DURATION(String duration) {
        return lang().getMessage("victory.gameDuration", duration);
    }

    public static String VICTORY_WINNING_TEAM(String teamName) {
        return lang().getMessage("victory.winningTeam", teamName);
    }

    public static String VICTORY_MEMBER_ALIVE() {
        return lang().getMessage("victory.memberAlive");
    }

    public static String VICTORY_MEMBER_DEAD() {
        return lang().getMessage("victory.memberDead");
    }

    public static String VICTORY_MEMBER_STATUS(String status, String name) {
        return lang().getMessage("victory.memberStatus", status, name);
    }

    public static String VICTORY_TITLE_WON() {
        return lang().getMessage("victory.titleWon");
    }

    public static String VICTORY_TEAM_PREFIX() {
        return lang().getMessage("victory.teamPrefix");
    }

    public static String DRAW_ALL_ELIMINATED() {
        return lang().getMessage("draw.allEliminated");
    }

    public static String DRAW_DURATION(String duration) {
        return lang().getMessage("draw.duration", duration);
    }

    public static String DRAW_TITLE_SCREEN() {
        return lang().getMessage("draw.titleScreen");
    }

    public static String DRAW_NO_WINNER() {
        return lang().getMessage("draw.noWinner");
    }

    public static String DRAW_WINNER_TEXT() {
        return lang().getMessage("draw.winnerText");
    }

    public static String MENU_CLICK_TO_CONFIGURE() {
        return lang().getMessage("menu.clickToConfigure");
    }

    public static String MENU_CLICK_TO_CHANGE() {
        return lang().getMessage("menu.clickToChange");
    }

    public static String MENU_END_PORTAL_TIME() {
        return lang().getMessage("menu.endPortalTime");
    }

    public static String MENU_CURRENT(int hours, int minutes, int seconds) {
        return lang().getMessage("menu.current", hours, minutes, seconds);
    }

    public static String MENU_CURRENT_SIMPLE() {
        return lang().getMessage("menu.currentSimple");
    }

    public static String MENU_STATUS(String status) {
        return lang().getMessage("menu.status", status);
    }

    public static String MENU_ENABLED() {
        return lang().getMessage("menu.enabled");
    }

    public static String MENU_DISABLED() {
        return lang().getMessage("menu.disabled");
    }

    public static String MENU_LOCATOR_BAR_TIME() {
        return lang().getMessage("menu.locatorBarTime");
    }

    public static String MENU_LOCATOR_BAR_DESC() {
        return lang().getMessage("menu.locatorBarDesc");
    }

    public static String MENU_SHULKER_SPAWN() {
        return lang().getMessage("menu.shulkerSpawn");
    }

    public static String MENU_TIME(int hours, int minutes, int seconds) {
        return lang().getMessage("menu.time", hours, minutes, seconds);
    }

    public static String MENU_TIME_SIMPLE() {
        return lang().getMessage("menu.timeSimple");
    }

    public static String MENU_CUSTOM_RECIPES() {
        return lang().getMessage("menu.customRecipes");
    }

    public static String MENU_CLICK_TO_VIEW() {
        return lang().getMessage("menu.clickToView");
    }

    public static String MENU_CUSTOM_RECIPES_DESC() {
        return lang().getMessage("menu.customRecipesDesc");
    }

    public static String MENU_VIEW_STATS() {
        return lang().getMessage("menu.viewStats");
    }

    public static String MENU_VIEW_STATS_DESC() {
        return lang().getMessage("menu.viewStatsDesc");
    }

    public static String MENU_CURRENT_GAME_STATS() {
        return lang().getMessage("menu.currentGameStats");
    }

    public static String MENU_SAVE_CONFIG() {
        return lang().getMessage("menu.saveConfig");
    }

    public static String MENU_SAVE_ALL_CHANGES() {
        return lang().getMessage("menu.saveAllChanges");
    }

    public static String MENU_CLOSE_MENU() {
        return lang().getMessage("menu.closeMenu");
    }

    public static String MENU_HOURS(int hours) {
        return lang().getMessage("menu.hours", hours);
    }

    public static String MENU_MINUTES(int minutes) {
        return lang().getMessage("menu.minutes", minutes);
    }

    public static String MENU_SECONDS(int seconds) {
        return lang().getMessage("menu.seconds", seconds);
    }

    public static String MENU_LEFT_CLICK_PLUS() {
        return lang().getMessage("menu.leftClickPlus");
    }

    public static String MENU_RIGHT_CLICK_MINUS() {
        return lang().getMessage("menu.rightClickMinus");
    }

    public static String MENU_CLICK_TO_TOGGLE(String action) {
        return lang().getMessage("menu.clickToToggle", action);
    }

    public static String MENU_ENABLE() {
        return lang().getMessage("menu.enable");
    }

    public static String MENU_DISABLE() {
        return lang().getMessage("menu.disable");
    }

    public static String MENU_BACK_TO_MAIN() {
        return lang().getMessage("menu.backToMain");
    }

    public static String MENU_CONFIG_SAVED() {
        return lang().getMessage("menu.configSaved");
    }

    public static String GAMEMODE_ENDER_DRAGON() {
        return lang().getMessage("gamemode.enderDragon");
    }

    public static String ITEM_SUPER_GOLDEN_APPLE() {
        return lang().getMessage("item.superGoldenApple");
    }

    public static String ITEM_HEAD_APPLE() {
        return lang().getMessage("item.headApple");
    }

    public static String ITEM_HYPER_GOLDEN_APPLE() {
        return lang().getMessage("item.hyperGoldenApple");
    }

    public static String ITEM_GLISTERING_MELON_SLICE() {
        return lang().getMessage("item.glisteringMelonSlice");
    }

    public static String ITEM_GOLDEN_CARROT() {
        return lang().getMessage("item.goldenCarrot");
    }

    public static String ITEM_DRAGON_BREATH() {
        return lang().getMessage("item.dragonBreath");
    }

    public static String PROXIMITY_NEARBY_PLAYER(String playerName) {
        return lang().getMessage("proximity.nearbyPlayer", playerName);
    }

    public static String COMMAND_SPECIFY_SUBCOMMAND() {
        return lang().getMessage("command.specifySubcommand");
    }

    public static String COMMAND_UNKNOWN_SUBCOMMAND() {
        return lang().getMessage("command.unknownSubcommand");
    }

    public static String MAIN_PLAYERS_ONLY_JOIN() {
        return lang().getMessage("main.playersOnly.join");
    }

    public static String MAIN_PLAYERS_ONLY_LEAVE() {
        return lang().getMessage("main.playersOnly.leave");
    }

    public static String MAIN_JOINED_UHC() {
        return lang().getMessage("main.joined");
    }

    public static String MAIN_LEFT_UHC() {
        return lang().getMessage("main.left");
    }

    public static String GAME_STARTED() {
        return lang().getMessage("game.started");
    }

    public static String GAME_PAUSED() {
        return lang().getMessage("game.paused");
    }

    public static String GAME_CANCELLED() {
        return lang().getMessage("game.cancelled");
    }

    public static String SETTINGS_SAVED() {
        return lang().getMessage("settings.saved");
    }

    public static String SETTINGS_LOADED() {
        return lang().getMessage("settings.loaded");
    }

    public static String INFO_HEADER() {
        return lang().getMessage("info.header");
    }

    public static String INFO_GAME_STATE(GameState status) {
        return lang().getMessage("info.gameStatus", status.toString());
    }

    public static String INFO_TEAM_MODE(TeamMode mode) {
        return lang().getMessage("info.teamMode", mode.toString());
    }

    public static String INFO_TEAM_SIZE(int size) {
        return lang().getMessage("info.teamSize", size);
    }

    public static String INFO_PLAYER_LIVES(int lives) {
        return lang().getMessage("info.playerLives", lives);
    }

    public static String INFO_MAX_WORLD_SIZE(int size) {
        return lang().getMessage("info.maxWorldSize", size);
    }

    public static String INFO_MIN_WORLD_SIZE(int size) {
        return lang().getMessage("info.minWorldSize", size);
    }

    public static String INFO_GAME_TIME(int h, int m, int s) {
        return lang().getMessage("info.gameTime", String.format("%02d", h), String.format("%02d", m), String.format("%02d", s));
    }

    public static String INFO_AGREEMENT_TIME(int h, int m, int s) {
        return lang().getMessage("info.agreementTime", String.format("%02d", h), String.format("%02d", m), String.format("%02d", s));
    }

    public static String INFO_MIN_BORDER_TIME(int h, int m, int s) {
        return lang().getMessage("info.minBorderTime", String.format("%02d", h), String.format("%02d", m), String.format("%02d", s));
    }

    public static String INFO_MAX_INGAME_TEAMS_TIME(int h, int m, int s) {
        return lang().getMessage("info.maxTeamTime", String.format("%02d", h), String.format("%02d", m), String.format("%02d", s));
    }

    public static String INFO_TEAMS_COUNT(int count) {
        return lang().getMessage("info.teamsCount", count);
    }

    public static String INFO_PLAYERS_COUNT(int count) {
        return lang().getMessage("info.playersCount", count);
    }

    public static String INFO_RECIPES_HEADER() {
        return lang().getMessage("info.recipesHeader");
    }

    public static String INFO_SEPARATOR() {
        return lang().getMessage("info.separator");
    }

    public static String INFO_BORDER() {
        return lang().getMessage("info.border");
    }

    public static String PLAYER_JOINED_UHC() {
        return MAIN_JOINED_UHC();
    }

    public static String JOINED_UHC() {
        return MAIN_JOINED_UHC();
    }

    public static String LEFT_UHC() {
        return MAIN_LEFT_UHC();
    }

    public static String ONLY_PLAYERS_JOIN() {
        return MAIN_PLAYERS_ONLY_JOIN();
    }

    public static String ONLY_PLAYERS_LEAVE() {
        return MAIN_PLAYERS_ONLY_LEAVE();
    }

    public static String SETTINGS_SPECIFY_SUBCOMMAND() {
        return lang().getMessage("settings.specifySubcommand");
    }

    public static String SETTINGS_SPECIFY_VALUE() {
        return lang().getMessage("settings.specifyValue");
    }

    public static String SETTINGS_UNKNOWN_SUBCOMMAND() {
        return lang().getMessage("settings.unknownSubcommand");
    }

    public static String SETTINGS_UNKNOWN() {
        return lang().getMessage("settings.unknown");
    }

    public static String SETTINGS_TEAM_MODE_UNKNOWN() {
        return lang().getMessage("settings.teamMode.unknown");
    }

    public static String SETTINGS_TEAM_MODE_SET(String mode) {
        return lang().getMessage("settings.teamMode.set", mode);
    }

    public static String SETTINGS_TEAM_SIZE_SET(int size) {
        return lang().getMessage("settings.teamSize.set", size);
    }

    public static String SETTINGS_TEAM_SIZE_INVALID() {
        return lang().getMessage("settings.teamSize.invalid");
    }

    public static String SETTINGS_PLAYER_LIVES_SET(int lives) {
        return lang().getMessage("settings.playerLives.set", lives);
    }

    public static String SETTINGS_PLAYER_LIVES_INVALID() {
        return lang().getMessage("settings.playerLives.invalid");
    }

    public static String SETTINGS_MAX_WORLD_SIZE_SET(int size) {
        return lang().getMessage("settings.maxWorldSize.set", size);
    }

    public static String SETTINGS_MAX_WORLD_SIZE_INVALID() {
        return lang().getMessage("settings.maxWorldSize.invalid");
    }

    public static String SETTINGS_MIN_WORLD_SIZE_SET(int size) {
        return lang().getMessage("settings.minWorldSize.set", size);
    }

    public static String SETTINGS_MIN_WORLD_SIZE_INVALID() {
        return lang().getMessage("settings.minWorldSize.invalid");
    }

    public static String SETTINGS_HOURS_SET(int hours) {
        return lang().getMessage("settings.hours.set", hours);
    }

    public static String SETTINGS_HOURS_INVALID() {
        return lang().getMessage("settings.hours.invalid");
    }

    public static String SETTINGS_MINUTES_SET(int minutes) {
        return lang().getMessage("settings.minutes.set", minutes);
    }

    public static String SETTINGS_MINUTES_INVALID() {
        return lang().getMessage("settings.minutes.invalid");
    }

    public static String SETTINGS_SECONDS_SET(int seconds) {
        return lang().getMessage("settings.seconds.set", seconds);
    }

    public static String SETTINGS_SECONDS_INVALID() {
        return lang().getMessage("settings.seconds.invalid");
    }

    public static String SETTINGS_AGREEMENT_HOURS_SET(int hours) {
        return lang().getMessage("settings.hours.set", hours);
    }

    public static String SETTINGS_AGREEMENT_HOURS_INVALID() {
        return lang().getMessage("settings.hours.invalid");
    }

    public static String SETTINGS_AGREEMENT_MINUTES_SET(int minutes) {
        return lang().getMessage("settings.minutes.set", minutes);
    }

    public static String SETTINGS_AGREEMENT_MINUTES_INVALID() {
        return lang().getMessage("settings.minutes.invalid");
    }

    public static String SETTINGS_AGREEMENT_SECONDS_SET(int seconds) {
        return lang().getMessage("settings.seconds.set", seconds);
    }

    public static String SETTINGS_AGREEMENT_SECONDS_INVALID() {
        return lang().getMessage("settings.seconds.invalid");
    }

    public static String SETTINGS_MIN_BORDER_HOURS_SET(int hours) {
        return lang().getMessage("settings.hours.set", hours);
    }

    public static String SETTINGS_MIN_BORDER_HOURS_INVALID() {
        return lang().getMessage("settings.hours.invalid");
    }

    public static String SETTINGS_MIN_BORDER_MINUTES_SET(int minutes) {
        return lang().getMessage("settings.minutes.set", minutes);
    }

    public static String SETTINGS_MIN_BORDER_MINUTES_INVALID() {
        return lang().getMessage("settings.minutes.invalid");
    }

    public static String SETTINGS_MIN_BORDER_SECONDS_SET(int seconds) {
        return lang().getMessage("settings.seconds.set", seconds);
    }

    public static String SETTINGS_MIN_BORDER_SECONDS_INVALID() {
        return lang().getMessage("settings.seconds.invalid");
    }

    public static String SETTINGS_MAX_TEAM_HOURS_SET(int hours) {
        return lang().getMessage("settings.hours.set", hours);
    }

    public static String SETTINGS_MAX_TEAM_HOURS_INVALID() {
        return lang().getMessage("settings.hours.invalid");
    }

    public static String SETTINGS_MAX_TEAM_MINUTES_SET(int minutes) {
        return lang().getMessage("settings.minutes.set", minutes);
    }

    public static String SETTINGS_MAX_TEAM_MINUTES_INVALID() {
        return lang().getMessage("settings.minutes.invalid");
    }

    public static String SETTINGS_MAX_TEAM_SECONDS_SET(int seconds) {
        return lang().getMessage("settings.seconds.set", seconds);
    }

    public static String SETTINGS_MAX_TEAM_SECONDS_INVALID() {
        return lang().getMessage("settings.seconds.invalid");
    }

    public static String SETTINGS_RECIPE_SPECIFY() {
        return lang().getMessage("settings.recipe.specify");
    }

    public static String SETTINGS_RECIPE_INVALID_VALUE() {
        return lang().getMessage("settings.recipe.invalidValue");
    }

    public static String SETTINGS_RECIPE_UNKNOWN() {
        return lang().getMessage("settings.recipe.unknown");
    }

    public static String SETTINGS_RECIPE_SET(String name, boolean enabled) {
        return lang().getMessage("settings.recipe.set", name, enabled ? "enabled" : "disabled");
    }

    public static String SETTINGS_TIME_FORMAT_INVALID() {
        return lang().getMessage("settings.timeFormat.invalid");
    }

    public static String SETTINGS_GAME_TIME_SET(String time) {
        return lang().getMessage("settings.gameTime.set", time);
    }

    public static String SETTINGS_AGREEMENT_TIME_SET(String time) {
        return lang().getMessage("settings.agreementTime.set", time);
    }

    public static String SETTINGS_MIN_BORDER_TIME_SET(String time) {
        return lang().getMessage("settings.minBorderTime.set", time);
    }

    public static String SETTINGS_MAX_TEAM_TIME_SET(String time) {
        return lang().getMessage("settings.maxTeamTime.set", time);
    }

    public static String PLAYER_SPECIFY_SUBCOMMAND() {
        return lang().getMessage("player.specifySubcommand");
    }

    public static String PLAYER_UNKNOWN_SUBCOMMAND() {
        return lang().getMessage("player.unknownSubcommand");
    }

    public static String PLAYER_LIST_HEADER() {
        return lang().getMessage("player.list.header");
    }

    public static String PLAYER_LIST_ITEM(String name, int lives) {
        return lang().getMessage("player.list.item", name, lives);
    }

    public static String PLAYER_LIST_NONE() {
        return lang().getMessage("player.list.none");
    }

    public static String PLAYER_LIVES_SET(String playerName, int lives) {
        return lang().getMessage("player.lives.set", playerName, lives);
    }

    public static String PLAYER_LIVES_ERROR(String playerName) {
        return lang().getMessage("player.lives.error", playerName);
    }

    public static String PLAYER_HEALTH_SET(String playerName, double health) {
        return lang().getMessage("player.health.set", playerName, health);
    }

    public static String PLAYER_HEALTH_ERROR(String playerName) {
        return lang().getMessage("player.health.error", playerName);
    }

    public static String PLAYER_REVIVED(String playerName) {
        return lang().getMessage("player.revived", playerName);
    }

    public static String PLAYER_REVIVE_ERROR(String playerName) {
        return lang().getMessage("player.revive.error", playerName);
    }

    public static String CHAT_GLOBAL_PREFIX(String randomName, String message) {
        return lang().getMessage("chat.global.prefix", randomName, message);
    }

    public static String CHAT_TEAM_PREFIX(String playerName, String message) {
        return lang().getMessage("chat.team.prefix", playerName, message);
    }

    public static String ADVANCEMENT_MADE(String randomName, String advancementTitle) {
        return lang().getMessage("chat.advancement", randomName, advancementTitle);
    }

    public static String SKIN_SHUFFLE_BORDER() {
        return lang().getMessage("skin.shuffle.border");
    }

    public static String SKIN_SHUFFLE_TITLE() {
        return lang().getMessage("skin.shuffle.title");
    }

    public static String SKIN_SHUFFLE_DESCRIPTION() {
        return lang().getMessage("skin.shuffle.description");
    }

    public static String SKIN_SHUFFLE_HINT() {
        return lang().getMessage("skin.shuffle.hint");
    }

    public static String SKIN_REVEAL_BORDER() {
        return lang().getMessage("skin.reveal.border");
    }

    public static String SKIN_REVEAL_TITLE() {
        return lang().getMessage("skin.reveal.title");
    }

    public static String SKIN_REVEAL_SKIN(String skinName) {
        return lang().getMessage("skin.reveal.skin", skinName);
    }

    public static String SKIN_REVEAL_REAL(String realName) {
        return lang().getMessage("skin.reveal.real", realName);
    }

    public static String SKIN_REVEAL_BROADCAST(String attackerName, String revealedName) {
        return lang().getMessage("skin.reveal.broadcast", attackerName, revealedName);
    }

    public static String SKIN_RESTORE_SUCCESS() {
        return lang().getMessage("skin.restore.success");
    }

    public static String SKINS_USAGE() {
        return lang().getMessage("skins.usage");
    }

    public static String SKINS_SHUFFLED() {
        return lang().getMessage("skins.shuffled");
    }

    public static String SKINS_RESTORED() {
        return lang().getMessage("skins.restored");
    }

    public static String SKINS_STATUS_SHUFFLED() {
        return lang().getMessage("skins.status.shuffled");
    }

    public static String SKINS_STATUS_NORMAL() {
        return lang().getMessage("skins.status.normal");
    }

    public static String SKINS_UNKNOWN_SUBCOMMAND() {
        return lang().getMessage("skins.unknown");
    }

    public static String SKIN_STATS(int revealed, int hidden) {
        return lang().getMessage("skin.stats", revealed, hidden);
    }

    public static String SHULKER_BORDER() {
        return lang().getMessage("shulker.border");
    }

    public static String SHULKER_SPAWNED_TITLE() {
        return lang().getMessage("shulker.spawned.title");
    }

    public static String SHULKER_HUNT_INFO() {
        return lang().getMessage("shulker.hunt.info");
    }

    public static String SHULKER_LOCATION() {
        return lang().getMessage("shulker.location");
    }

    public static String SHULKER_WILL_SPAWN(int minutes) {
        return lang().getMessage("shulker.willSpawn", minutes);
    }

    public static String LOCATOR_BAR_BORDER() {
        return lang().getMessage("locatorBar.border");
    }

    public static String LOCATOR_BAR_ACTIVATED_TITLE() {
        return lang().getMessage("locatorBar.activated.title");
    }

    public static String LOCATOR_BAR_ACTIVATED_DESC() {
        return lang().getMessage("locatorBar.activated.desc");
    }

    public static String LOCATOR_BAR_HIDDEN_INFO() {
        return lang().getMessage("locatorBar.hidden.info");
    }

    public static String LOCATOR_BAR_WILL_ACTIVATE(int minutes) {
        return lang().getMessage("locatorBar.willActivate", minutes);
    }

    public static String GAMEMODE_PVD_HEADER() {
        return lang().getMessage("gameMode.pvd.header");
    }

    public static String GAMEMODE_PVD_OBJECTIVE() {
        return lang().getMessage("gameMode.pvd.objective");
    }

    public static String GAMEMODE_PVD_PVP_ENABLED() {
        return lang().getMessage("gameMode.pvd.pvpEnabled");
    }

    public static String GAMEMODE_PVD_END_PORTAL(int minutes) {
        return lang().getMessage("gameMode.pvd.endPortal", minutes);
    }

    public static String GAMEMODE_PVP_HEADER() {
        return lang().getMessage("gameMode.pvp.header");
    }

    public static String GAMEMODE_PVP_OBJECTIVE() {
        return lang().getMessage("gameMode.pvp.objective");
    }

    public static String GAMEMODE_PVP_PREPARE() {
        return lang().getMessage("gameMode.pvp.prepare");
    }

    public static String GAMEMODE_RESOURCE_RUSH_HEADER() {
        return lang().getMessage("gameMode.resourceRush.header");
    }

    public static String GAMEMODE_RESOURCE_RUSH_OBJECTIVE() {
        return lang().getMessage("gameMode.resourceRush.objective");
    }

    public static String GAMEMODE_RESOURCE_RUSH_ITEMS_LIST() {
        return lang().getMessage("gameMode.resourceRush.itemsList");
    }

    public static String GAMEMODE_RESOURCE_RUSH_ITEM(String itemName) {
        return lang().getMessage("gameMode.resourceRush.item", itemName);
    }

    public static String GAMEMODE_GAME_ENDED() {
        return lang().getMessage("gameMode.gameEnded");
    }

    public static String GAMEMODE_GAME_ENDED_TITLE() {
        return lang().getMessage("gameMode.gameEndedTitle");
    }

    public static String GAMEMODE_WINNING_TEAM(String teamName) {
        return lang().getMessage("gameMode.winningTeam", teamName);
    }

    public static String GAMEMODE_MODE_USED(String mode) {
        return lang().getMessage("gameMode.modeUsed", mode);
    }

    public static String GAMEMODE_END_PORTAL_ACTIVATED() {
        return GAMEMODE_PVD_END_PORTAL(0);
    }

    public static String GAMEMODE_END_PORTAL_LOCATION(int y) {
        return SHULKER_LOCATION();
    }

    public static String GAMEMODE_END_PORTAL_GO() {
        return GAMEMODE_PVD_OBJECTIVE();
    }

    public static String MENU_MAIN_TITLE() {
        return lang().getMessage("menu.main.title");
    }

    public static String MENU_TIME_TITLE() {
        return lang().getMessage("menu.time.title");
    }

    public static String MENU_RECIPES_TITLE() {
        return lang().getMessage("menu.recipes.title");
    }

    public static String MENU_GAME_MODE() {
        return lang().getMessage("menu.gameMode");
    }

    public static String MENU_GAME_MODE_CURRENT(String mode) {
        return lang().getMessage("menu.gameMode.current", mode);
    }

    public static String MENU_GAME_MODE_CLICK() {
        return lang().getMessage("menu.gameMode.clickToChange");
    }

    public static String MENU_GAME_MODE_PVD() {
        return lang().getMessage("menu.gameMode.pvd");
    }

    public static String MENU_GAME_MODE_PVP() {
        return lang().getMessage("menu.gameMode.pvp");
    }

    public static String MENU_GAME_MODE_RESOURCE_RUSH() {
        return lang().getMessage("menu.gameMode.resourceRush");
    }

    public static String MENU_TEAM_MODE() {
        return lang().getMessage("menu.teamMode");
    }

    public static String MENU_TEAM_MODE_CURRENT(String mode) {
        return lang().getMessage("menu.teamMode.current", mode);
    }

    public static String MENU_TEAM_MODE_CLICK() {
        return lang().getMessage("menu.teamMode.clickToChange");
    }

    public static String MENU_TEAM_MODE_AUTO() {
        return lang().getMessage("menu.teamMode.auto");
    }

    public static String MENU_TEAM_MODE_MANUAL() {
        return lang().getMessage("menu.teamMode.manual");
    }

    public static String MENU_TEAM_MODE_IN_GAME() {
        return lang().getMessage("menu.teamMode.inGame");
    }

    public static String MENU_TEAM_SIZE() {
        return lang().getMessage("menu.teamSize");
    }

    public static String MENU_TEAM_SIZE_CURRENT(int size) {
        return lang().getMessage("menu.teamSize.current", size);
    }

    public static String MENU_TEAM_SIZE_LEFT() {
        return lang().getMessage("menu.teamSize.leftClick");
    }

    public static String MENU_TEAM_SIZE_RIGHT() {
        return lang().getMessage("menu.teamSize.rightClick");
    }

    public static String MENU_PLAYER_LIVES() {
        return lang().getMessage("menu.playerLives");
    }

    public static String MENU_PLAYER_LIVES_CURRENT(int lives) {
        return lang().getMessage("menu.playerLives.current", lives);
    }

    public static String MENU_PLAYER_LIVES_LEFT() {
        return lang().getMessage("menu.playerLives.leftClick");
    }

    public static String MENU_PLAYER_LIVES_RIGHT() {
        return lang().getMessage("menu.playerLives.rightClick");
    }

    public static String MENU_ELIMINATION_MODE() {
        return lang().getMessage("menu.eliminationMode");
    }

    public static String MENU_ELIMINATION_MODE_DESC() {
        return lang().getMessage("menu.eliminationMode.desc");
    }

    public static String MENU_ELIMINATION_MODE_SPECTATOR() {
        return lang().getMessage("menu.eliminationMode.spectator");
    }

    public static String MENU_ELIMINATION_MODE_KICK() {
        return lang().getMessage("menu.eliminationMode.kick");
    }

    public static String MENU_ELIMINATION_MODE_CLICK() {
        return lang().getMessage("menu.eliminationMode.click");
    }

    public static String MENU_MAX_WORLD_SIZE() {
        return lang().getMessage("menu.maxWorldSize");
    }

    public static String MENU_MAX_WORLD_SIZE_CURRENT(int size) {
        return lang().getMessage("menu.maxWorldSize.current", size);
    }

    public static String MENU_MAX_WORLD_SIZE_LEFT() {
        return lang().getMessage("menu.maxWorldSize.leftClick");
    }

    public static String MENU_MAX_WORLD_SIZE_RIGHT() {
        return lang().getMessage("menu.maxWorldSize.rightClick");
    }

    public static String MENU_MIN_WORLD_SIZE() {
        return lang().getMessage("menu.minWorldSize");
    }

    public static String MENU_MIN_WORLD_SIZE_CURRENT(int size) {
        return lang().getMessage("menu.minWorldSize.current", size);
    }

    public static String MENU_MIN_WORLD_SIZE_LEFT() {
        return lang().getMessage("menu.minWorldSize.leftClick");
    }

    public static String MENU_MIN_WORLD_SIZE_RIGHT() {
        return lang().getMessage("menu.minWorldSize.rightClick");
    }

    public static String MENU_GAME_TIME() {
        return lang().getMessage("menu.gameTime");
    }

    public static String MENU_GAME_TIME_CURRENT(int h, int m, int s) {
        return lang().getMessage("menu.gameTime.current", h, m, s);
    }

    public static String MENU_GAME_TIME_CLICK() {
        return lang().getMessage("menu.gameTime.clickToConfigure");
    }

    public static String MENU_AGREEMENT_TIME() {
        return lang().getMessage("menu.agreementTime");
    }

    public static String MENU_AGREEMENT_TIME_CURRENT(int h, int m, int s) {
        return lang().getMessage("menu.agreementTime.current", h, m, s);
    }

    public static String MENU_AGREEMENT_TIME_CLICK() {
        return lang().getMessage("menu.agreementTime.clickToConfigure");
    }

    public static String MENU_MIN_BORDER_TIME() {
        return lang().getMessage("menu.minBorderTime");
    }

    public static String MENU_MIN_BORDER_TIME_CURRENT(int h, int m, int s) {
        return lang().getMessage("menu.minBorderTime.current", h, m, s);
    }

    public static String MENU_MIN_BORDER_TIME_CLICK() {
        return lang().getMessage("menu.minBorderTime.clickToConfigure");
    }

    public static String MENU_MAX_TEAM_TIME() {
        return lang().getMessage("menu.maxTeamTime");
    }

    public static String MENU_MAX_TEAM_TIME_CURRENT(int h, int m, int s) {
        return lang().getMessage("menu.maxTeamTime.current", h, m, s);
    }

    public static String MENU_MAX_TEAM_TIME_CLICK() {
        return lang().getMessage("menu.maxTeamTime.clickToConfigure");
    }

    public static String MENU_RECIPES() {
        return lang().getMessage("menu.recipes");
    }

    public static String MENU_RECIPES_CLICK() {
        return lang().getMessage("menu.recipes.clickToConfigure");
    }

    public static String MENU_BACK() {
        return lang().getMessage("menu.back");
    }

    public static String MENU_CLOSE() {
        return lang().getMessage("menu.close");
    }

    public static String MENU_TIME_HOURS(int hours) {
        return lang().getMessage("menu.time.hours", hours);
    }

    public static String MENU_TIME_MINUTES(int minutes) {
        return lang().getMessage("menu.time.minutes", minutes);
    }

    public static String MENU_TIME_SECONDS(int seconds) {
        return lang().getMessage("menu.time.seconds", seconds);
    }

    public static String MENU_TIME_INCREASE_HOUR() {
        return lang().getMessage("menu.time.increaseHour");
    }

    public static String MENU_TIME_DECREASE_HOUR() {
        return lang().getMessage("menu.time.decreaseHour");
    }

    public static String MENU_TIME_INCREASE_MINUTE() {
        return lang().getMessage("menu.time.increaseMinute");
    }

    public static String MENU_TIME_DECREASE_MINUTE() {
        return lang().getMessage("menu.time.decreaseMinute");
    }

    public static String MENU_TIME_INCREASE_SECOND() {
        return lang().getMessage("menu.time.increaseSecond");
    }

    public static String MENU_TIME_DECREASE_SECOND() {
        return lang().getMessage("menu.time.decreaseSecond");
    }

    public static String STATS_HEADER() {
        return lang().getMessage("stats.header");
    }

    public static String STATS_TOP_KILLER(String name, int kills) {
        return lang().getMessage("stats.topKiller", name, kills);
    }

    public static String STATS_IRONMAN(int count) {
        return lang().getMessage("stats.ironman", count);
    }

    public static String STATS_IRONMAN_ENTRY(String name, boolean alive) {
        return lang().getMessage("stats.ironman.entry", name,
                alive ? lang().getMessage("stats.ironman.alive") : lang().getMessage("stats.ironman.dead"));
    }

    public static String STATS_TOP_KILLERS_HEADER() {
        return lang().getMessage("stats.topKillersHeader");
    }

    public static String STATS_TOP_KILLERS_ENTRY(int position, String name, int kills) {
        return lang().getMessage("stats.topKillers.entry", position, name, kills);
    }

    public static String UHC_SCOREBOARD_TITLE() {
        return lang().getMessage("scoreboard.title");
    }

    public static String UHC_SCOREBOARD_TIME(int h, int m, int s) {
        return lang().getMessage("scoreboard.time", String.format("%02d", h), String.format("%02d", m), String.format("%02d", s));
    }

    public static String UHC_SCOREBOARD_PVP() {
        return lang().getMessage("scoreboard.pvp");
    }

    public static String UHC_SCOREBOARD_PVP_DISABLED() {
        return lang().getMessage("scoreboard.pvp.disabled");
    }

    public static String UHC_SCOREBOARD_PVP_ENABLED() {
        return lang().getMessage("scoreboard.pvp.enabled");
    }

    public static String UHC_SCOREBOARD_ALIVE(long alive, long total) {
        return lang().getMessage("scoreboard.alivePlayers", alive, total);
    }

    public static String UHC_SCOREBOARD_TEAMS(long teams) {
        return lang().getMessage("scoreboard.teams", teams);
    }

    public static String UHC_SCOREBOARD_BORDER(int size) {
        return lang().getMessage("scoreboard.border", size);
    }

    public static String LOCATOR_BAR_NEARBY_PLAYER(int blocks) {
        return lang().getMessage("locatorBar.nearbyPlayer", blocks);
    }

    public static String SCOREBOARD_TEAM(String name) {
        return lang().getMessage("scoreboard.team", name);
    }

    public static String SCOREBOARD_HEALTH(double health) {
        return lang().getMessage("scoreboard.health", String.format("%.1f", health));
    }

    public static String SCOREBOARD_OBJECTIVES() {
        return lang().getMessage("scoreboard.objectives");
    }

    public static String SCOREBOARD_MORE_ITEMS(int count) {
        return lang().getMessage("scoreboard.moreItems", count);
    }

    public static String SCOREBOARD_AGREEMENT() {
        return lang().getMessage("scoreboard.agreement");
    }

    public static String SCOREBOARD_ENDS_IN(String timeLeft) {
        return lang().getMessage("scoreboard.endsIn", timeLeft);
    }

    public static String SCOREBOARD_FINISHED() {
        return lang().getMessage("scoreboard.finished");
    }

    public static String MENU_SKIN_SHUFFLE() {
        return lang().getMessage("menu.skinShuffle");
    }

    public static String MENU_SKIN_SHUFFLE_INTERVAL(int h, int m, int s) {
        return lang().getMessage("menu.skinShuffle.interval", h, m, s);
    }

    public static String MENU_GRADUAL_BORDER() {
        return lang().getMessage("menu.gradualBorder");
    }

    public static String MENU_GRADUAL_BORDER_DESC() {
        return lang().getMessage("menu.gradualBorder.desc");
    }

    public static String MENU_GRADUAL_BORDER_NONE() {
        return lang().getMessage("menu.gradualBorder.none");
    }

    public static String MENU_GRADUAL_BORDER_GRADUAL() {
        return lang().getMessage("menu.gradualBorder.gradual");
    }

    public static String MENU_GRADUAL_BORDER_INSTANT() {
        return lang().getMessage("menu.gradualBorder.instant");
    }

    public static String MENU_GRADUAL_BORDER_THRESHOLD() {
        return lang().getMessage("menu.gradualBorder.threshold");
    }

    public static String MENU_THRESHOLD_START_TIME() {
        return lang().getMessage("menu.thresholdStartTime");
    }

    public static String MENU_THRESHOLD_START_TIME_CURRENT(int h, int m, int s) {
        return lang().getMessage("menu.thresholdStartTime.current", h, m, s);
    }

    public static String MENU_THRESHOLD_START_TIME_CLICK() {
        return lang().getMessage("menu.thresholdStartTime.clickToConfigure");
    }

    public static String MENU_THRESHOLD_END_TIME() {
        return lang().getMessage("menu.thresholdEndTime");
    }

    public static String MENU_THRESHOLD_END_TIME_CURRENT(int h, int m, int s) {
        return lang().getMessage("menu.thresholdEndTime.current", h, m, s);
    }

    public static String MENU_THRESHOLD_END_TIME_CLICK() {
        return lang().getMessage("menu.thresholdEndTime.clickToConfigure");
    }

    public static String MENU_PLAYER_BUFFS() {
        return lang().getMessage("menu.playerBuffs");
    }

    public static String MENU_PLAYER_BUFFS_RESISTANCE() {
        return lang().getMessage("menu.playerBuffs.resistance");
    }

    public static String MENU_PLAYER_BUFFS_EXTRA_HEARTS(int hearts) {
        return lang().getMessage("menu.playerBuffs.extraHearts", hearts);
    }

    public static String MENU_PLAYER_BUFFS_MAX_HEALTH(int maxHealth) {
        return lang().getMessage("menu.playerBuffs.maxHealth", maxHealth);
    }

    public static String MENU_PLAYER_BUFFS_EXTRA_HEARTS_CONFIG() {
        return lang().getMessage("menu.playerBuffs.extraHearts.config");
    }

    public static String MENU_PLAYER_BUFFS_EXTRA_HEARTS_DESC(int hearts) {
        return lang().getMessage("menu.playerBuffs.extraHearts.desc", hearts);
    }

    public static String MENU_PLAYER_BUFFS_MAX_HEALTH_CONFIG() {
        return lang().getMessage("menu.playerBuffs.maxHealth.config");
    }

    public static String MENU_PLAYER_BUFFS_MAX_HEALTH_DESC(int maxHealth) {
        return lang().getMessage("menu.playerBuffs.maxHealth.desc", maxHealth);
    }

    public static String MENU_TEAMS() {
        return lang().getMessage("menu.teams");
    }

    public static String MENU_TEAMS_DESC() {
        return lang().getMessage("menu.teams.desc");
    }

    public static String MENU_TEAMS_MODE(String mode) {
        return lang().getMessage("menu.teams.mode", mode);
    }

    public static String MENU_TEAMS_TITLE() {
        return lang().getMessage("menu.teams.title");
    }

    public static String MENU_TEAMS_CREATE_TEAM() {
        return lang().getMessage("menu.teams.createTeam");
    }

    public static String MENU_TEAMS_CREATE_TEAM_DESC() {
        return lang().getMessage("menu.teams.createTeam.desc");
    }

    public static String MENU_TEAMS_DELETE_TEAM() {
        return lang().getMessage("menu.teams.deleteTeam");
    }

    public static String MENU_TEAMS_DELETE_TEAM_DESC() {
        return lang().getMessage("menu.teams.deleteTeam.desc");
    }

    public static String MENU_TEAMS_ADD_PLAYER() {
        return lang().getMessage("menu.teams.addPlayer");
    }

    public static String MENU_TEAMS_ADD_PLAYER_DESC() {
        return lang().getMessage("menu.teams.addPlayer.desc");
    }

    public static String MENU_TEAMS_REMOVE_PLAYER() {
        return lang().getMessage("menu.teams.removePlayer");
    }

    public static String MENU_TEAMS_NO_TEAMS() {
        return lang().getMessage("menu.teams.noTeams");
    }

    public static String MENU_TEAMS_MEMBERS(int count) {
        return lang().getMessage("menu.teams.members", count);
    }

    public static String MENU_TEAMS_CLICK_TO_MANAGE() {
        return lang().getMessage("menu.teams.clickToManage");
    }

    public static String MENU_START_GAME() {
        return lang().getMessage("menu.startGame");
    }

    public static String MENU_START_GAME_DESC() {
        return lang().getMessage("menu.startGame.desc");
    }

    public static String MENU_PAUSE_GAME() {
        return lang().getMessage("menu.pauseGame");
    }

    public static String MENU_PAUSE_GAME_DESC() {
        return lang().getMessage("menu.pauseGame.desc");
    }

    public static String MENU_CANCEL_GAME() {
        return lang().getMessage("menu.cancelGame");
    }

    public static String MENU_CANCEL_GAME_DESC() {
        return lang().getMessage("menu.cancelGame.desc");
    }

    public static String MENU_LOAD_CONFIG() {
        return lang().getMessage("menu.loadConfig");
    }

    public static String MENU_LOAD_CONFIG_DESC() {
        return lang().getMessage("menu.loadConfig.desc");
    }

    public static String MENU_LOAD_CONFIG_SUCCESS() {
        return lang().getMessage("menu.loadConfig.success");
    }

    public static String MENU_GAME_NOT_RUNNING() {
        return lang().getMessage("menu.gameNotRunning");
    }

    public static String MENU_GAME_PAUSED() {
        return lang().getMessage("menu.gamePaused");
    }

    public static String MENU_GAME_STARTED_MSG() {
        return lang().getMessage("menu.gameStarted");
    }

    public static String MENU_GAME_CANCELLED_MSG() {
        return lang().getMessage("menu.gameCancelled");
    }

    public static String MENU_TIME_HOURS_TITLE() {
        return lang().getMessage("menu.time.hoursTitle");
    }

    public static String MENU_TIME_MINUTES_TITLE() {
        return lang().getMessage("menu.time.minutesTitle");
    }

    public static String MENU_TIME_SECONDS_TITLE() {
        return lang().getMessage("menu.time.secondsTitle");
    }

    public static String INFO_STATUS() {
        return lang().getMessage("info.status");
    }

    public static String INFO_ENABLED() {
        return lang().getMessage("info.enabled");
    }

    public static String INFO_DISABLED() {
        return lang().getMessage("info.disabled");
    }

    public static String INFO_CLICK_TO_TOGGLE(String action) {
        return lang().getMessage("info.clickToToggle", action);
    }

    public static String INFO_BACK() {
        return lang().getMessage("info.back");
    }

    public static String INFO_BACK_DESC() {
        return lang().getMessage("info.backDesc");
    }

    public static String INTERACT_CANNOT_TEAM(String playerName) {
        return lang().getMessage("interact.cannotTeam", playerName);
    }

    public static String INTERACT_TEAMS_COMBINED_EXCEED(int maxSize) {
        return lang().getMessage("interact.teamsCombinedExceed", maxSize);
    }

    public static String INTERACT_TEAMS_MERGED(String teamName) {
        return lang().getMessage("interact.teamsMerged", teamName);
    }

    public static String INTERACT_TEAM_MERGED_WITH(String playerName) {
        return lang().getMessage("interact.teamMergedWith", playerName);
    }

    public static String INTERACT_YOUR_TEAM_FULL() {
        return lang().getMessage("interact.yourTeamFull");
    }

    public static String INTERACT_JOINED_TEAM(String playerName) {
        return lang().getMessage("interact.joinedTeam", playerName);
    }

    public static String INTERACT_THEIR_TEAM_FULL(String playerName) {
        return lang().getMessage("interact.theirTeamFull", playerName);
    }

    public static String INTERACT_TEAM_FORMED_WITH(String playerName) {
        return lang().getMessage("interact.teamFormedWith", playerName);
    }

    public static String AFK_ONLY_PLAYERS() {
        return lang().getMessage("afk.onlyPlayers");
    }

    public static String AFK_NOW_AFK() {
        return lang().getMessage("afk.nowAFK");
    }

    public static String AFK_NO_LONGER_AFK() {
        return lang().getMessage("afk.noLongerAFK");
    }

    public static String NO_PERMISSION() {
        return lang().getMessage("command.noPermission");
    }

    public static String MAIN_PLAYERS_ONLY_MENU() {
        return lang().getMessage("main.playersOnly.menu");
    }

    public static String SETTINGS_END_PORTAL_SET(String status) {
        return lang().getMessage("settings.endPortal.set", status);
    }

    public static String SETTINGS_SHULKER_SET(String status) {
        return lang().getMessage("settings.shulker.set", status);
    }

    public static String SETTINGS_LOCATOR_BAR_SET(String status) {
        return lang().getMessage("settings.locatorBar.set", status);
    }

    public static String INFO_RECIPES_ITEM(String name, String status) {
        return lang().getMessage("info.recipes.item", name, status);
    }

    public static String PLAYER_SETLIVES_USAGE() {
        return lang().getMessage("player.setlives.usage");
    }

    public static String PLAYER_SETHEALTH_USAGE() {
        return lang().getMessage("player.sethealth.usage");
    }

    public static String PLAYER_REVIVE_USAGE() {
        return lang().getMessage("player.revive.usage");
    }

    public static String TEAM_LEAVE_IN_PROGRESS() {
        return lang().getMessage("team.leave.inProgress");
    }

    public static String BUFFS_ACTIVATED_BORDER() {
        return lang().getMessage("buffs.activated.border");
    }

    public static String BUFFS_ACTIVATED_TITLE() {
        return lang().getMessage("buffs.activated.title");
    }

    public static String BUFFS_ACTIVATED_HEARTS(int hearts) {
        return lang().getMessage("buffs.activated.hearts", hearts);
    }

    public static String BUFFS_ACTIVATED_RESISTANCE() {
        return lang().getMessage("buffs.activated.resistance");
    }

    public static String BUFFS_ACTIVATED_TITLE_SCREEN() {
        return lang().getMessage("buffs.activated.titleScreen");
    }

    public static String BUFFS_ACTIVATED_SUBTITLE(int hearts) {
        return lang().getMessage("buffs.activated.subtitle", hearts);
    }

    public static String SCOREBOARD_MEMBER_DEAD(String name) {
        return lang().getMessage("scoreboard.member.dead", name);
    }

    public static String SCOREBOARD_OBJECTIVE_CHECKED() {
        return lang().getMessage("scoreboard.objective.checked");
    }

    public static String SCOREBOARD_OBJECTIVE_UNCHECKED() {
        return lang().getMessage("scoreboard.objective.unchecked");
    }

    public static String DRAW_WINNER_LABEL() {
        return lang().getMessage("draw.winner.label");
    }
}
