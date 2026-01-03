package vch.uhc.managers.game;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRules;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import vch.uhc.UHC;
import vch.uhc.managers.player.PlayerManager;
import vch.uhc.managers.player.TeamManager;
import vch.uhc.misc.Messages;
import vch.uhc.misc.Settings;
import vch.uhc.misc.constants.GameConstants;
import vch.uhc.misc.enums.GameState;
import vch.uhc.misc.enums.TeamMode;
import vch.uhc.models.GameStats;
import vch.uhc.models.UHCPlayer;
import vch.uhc.models.UHCTeam;

/**
 * Orchestrates UHC game sessions. Coordinates between various sub-managers
 * (World, Timer, Scoreboard).
 */
public class UHCManager {

    private final UHC plugin;
    private final Settings settings;
    private final PlayerManager playerManager;
    private final TeamManager teamManager;

    private int currentWorldSize;
    private boolean teamsFormed = false;
    private GameStats gameStats;

    public UHCManager() {
        this.plugin = UHC.getPlugin();
        this.settings = plugin.getSettings();
        this.playerManager = plugin.getPlayerManager();
        this.teamManager = plugin.getTeamManager();
    }

    /**
     * Starts the UHC game, initializing all systems, rules and players.
     */
    public void start() {
        settings.setGameState(GameState.IN_PROGRESS);
        plugin.getGameModeManager().initializeGameMode();
        currentWorldSize = settings.getMaxWorldSize();
        teamsFormed = false;
        gameStats = new GameStats();

        settings.getItems().forEach(item -> {
            if (Bukkit.getRecipe(item.getKey()) == null && item.isEnabled()) {
                item.register();
            }
        });

        prepareGame();
        prepareTeams();
        preparePlayers();

        plugin.getGameTimerManager().start();
        plugin.getScoreboardManager().updateAllScoreboards();
    }

    /**
     * Pauses the game by changing state and stopping timers.
     */
    public void pause() {
        if (settings.getGameState() == GameState.IN_PROGRESS) {
            settings.setGameState(GameState.PAUSED);
            plugin.getGameTimerManager().stop();
            broadcast(Messages.MAIN_GAME_PAUSED());
        }
    }

    /**
     * Resumes a paused game.
     */
    public void resume() {
        if (settings.getGameState() == GameState.PAUSED) {
            settings.setGameState(GameState.IN_PROGRESS);
            plugin.getGameTimerManager().start();
            broadcast(Messages.MAIN_GAME_RESUMED());
        }
    }

    /**
     * Cancels the current game and resets state.
     */
    public void cancel() {
        settings.setGameState(GameState.NONE);
        plugin.getGameTimerManager().stop();
        plugin.getSkinManager().restoreAllSkins();
        plugin.getBackupManager().clearBackup();
        broadcast(Messages.MAIN_GAME_CANCELLED());
    }

    /**
     * Re-loads many game components and forces a language refresh.
     */
    public void reload() {
        settings.load();
        plugin.getLanguageManager().loadLanguage(plugin.getLanguageManager().getCurrentLanguage());
    }

    private void prepareGame() {
        World world = Bukkit.getWorld("world");
        if (world != null) {
            world.setStorm(false);
            world.setThundering(false);
            world.setWeatherDuration(GameConstants.MAX_WEATHER_DURATION);
            world.setTime(GameConstants.DAY_TIME);
            world.setGameRule(GameRules.ADVANCE_TIME, true);
            world.setGameRule(GameRules.ADVANCE_WEATHER, true);
            world.setGameRule(GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER, 0);
            world.setGameRule(GameRules.NATURAL_HEALTH_REGENERATION, false);
            world.setGameRule(GameRules.SPAWN_MOBS, true);
            world.setDifficulty(Difficulty.HARD);
            plugin.getWorldManager().setBorderCenter(0, 0);
            plugin.getWorldManager().setBorderSize(settings.getMaxWorldSize() * 2.0);
        }
    }

    /**
     * Periodically called to check win conditions.
     */
    public void checkGameState() {
        if (settings.getGameState() != GameState.IN_PROGRESS) {
            return;
        }

        // Skip check if game just started
        if (plugin.getGameTimerManager().getTotalElapsedSeconds() < 5) {
            return;
        }

        checkWinCondition();
    }

    /**
     * Handles in-game teaming logic based on proximity.
     */
    public void checkInGameTeamsState() {
        if (settings.getTeamMode() != TeamMode.IN_GAME || !plugin.getGameTimerManager().isCheckingProximity() || settings.getTeamSize() <= 1) {
            return;
        }

        // Proximity teaming logic (simplified for this decomposition phase, keeping original logic blocks)
        List<UHCPlayer> players = plugin.getPlayerManager().getPlayers();
        for (int i = 0; i < players.size(); i++) {
            UHCPlayer p1 = players.get(i);
            Player b1 = Bukkit.getPlayer(p1.getUuid());
            if (b1 == null || !p1.isAlive()) {
                continue;
            }

            for (int j = i + 1; j < players.size(); j++) {
                UHCPlayer p2 = players.get(j);
                Player b2 = Bukkit.getPlayer(p2.getUuid());
                if (b2 == null || !p2.isAlive()) {
                    continue;
                }

                if (teamManager.canPlayersJoinTeam(p1, p2)) {
                    if (b1.getLocation().distance(b2.getLocation()) <= 10) {
                        handleProximityTeam(p1, p2, b1, b2);
                    }
                }
            }
        }
    }

    private void handleProximityTeam(UHCPlayer p1, UHCPlayer p2, Player b1, Player b2) {
        if (p1.getTeam() == null && p2.getTeam() == null) {
            UHCTeam team = teamManager.createTeam(p1, "Team " + (teamManager.getTeams().size() + 1));
            teamManager.addPlayer(team, p2);
            notifyTeamFormation(team);
        } else if (p1.getTeam() != null && p2.getTeam() == null) {
            if (p1.getTeam().getMembers().size() < settings.getTeamSize()) {
                teamManager.addPlayer(p1.getTeam(), p2);
                b2.sendMessage(Messages.TEAM_JOINED(p1.getTeam().getName()));
            }
        } else if (p1.getTeam() == null && p2.getTeam() != null) {
            if (p2.getTeam().getMembers().size() < settings.getTeamSize()) {
                teamManager.addPlayer(p2.getTeam(), p1);
                b1.sendMessage(Messages.TEAM_JOINED(p2.getTeam().getName()));
            }
        }
    }

    private void notifyTeamFormation(UHCTeam team) {
        String msg = Messages.TEAM_FORMED(team.getName());
        team.getMembers().forEach(m -> {
            Player p = Bukkit.getPlayer(m.getUuid());
            if (p != null) {
                p.sendMessage(msg);
            }
        });
    }

    private void prepareTeams() {
        if (settings.getTeamMode() != TeamMode.AUTO) {
            if (settings.getTeamMode() == TeamMode.MANUAL && !teamManager.getTeams().isEmpty()) {
                teamsFormed = true;
            }
            return;
        }

        List<UHCPlayer> uhcPlayers = new ArrayList<>(playerManager.getPlayers());
        Collections.shuffle(uhcPlayers);

        int teamSize = settings.getTeamSize();
        int teamCount = (int) Math.ceil(uhcPlayers.size() / (double) teamSize);

        for (int i = 0; i < uhcPlayers.size(); i++) {
            UHCPlayer player = uhcPlayers.get(i);
            int teamIndex = i % teamCount;

            if (i < teamCount) {
                teamManager.createTeam(player, "Team " + (teamIndex + 1));
            } else {
                UHCTeam team = teamManager.getTeams().get(teamIndex);
                if (team.getMembers().size() < teamSize) {
                    teamManager.addPlayer(team, player);
                }
            }
        }
        teamsFormed = true;
    }

    private void preparePlayers() {
        List<Location> spawns = plugin.getWorldManager().getSpawns();
        List<UHCTeam> teams = teamManager.getTeams();

        for (int i = 0; i < teams.size(); i++) {
            UHCTeam team = teams.get(i);
            Location loc = spawns.get(i % spawns.size()).clone();
            for (UHCPlayer player : team.getMembers()) {
                setupPlayer(player, loc);
            }
        }

        // Handle players without team
        playerManager.getPlayers().stream()
                .filter(p -> p.getTeam() == null)
                .forEach(p -> setupPlayer(p, spawns.get((int) (Math.random() * spawns.size()))));

        teamsFormed = true;
    }

    private void setupPlayer(UHCPlayer player, Location loc) {
        player.setSpawn(loc);
        player.setLives(settings.getPlayerLives());
        Player bp = Bukkit.getPlayer(player.getUuid());
        if (bp != null) {
            bp.teleport(loc);
            bp.getInventory().addItem(new ItemStack(Material.OAK_BOAT));
        }
    }

    private void checkWinCondition() {
        List<UHCPlayer> alivePlayers = playerManager.getPlayers().stream()
                .filter(p -> p.isPlaying() && p.isAlive())
                .toList();

        if (alivePlayers.isEmpty()) {
            declareDraw();
            return;
        }

        List<UHCTeam> aliveTeams = teamManager.getTeams().stream()
                .filter(t -> t.getMembers().stream().anyMatch(p -> p.isPlaying() && p.isAlive()))
                .toList();

        if (aliveTeams.size() == 1 && alivePlayers.stream().allMatch(p -> p.getTeam() == aliveTeams.get(0))) {
            declareTeamWinner(aliveTeams.get(0));
        } else if (aliveTeams.isEmpty() && alivePlayers.size() == 1) {
            declarePlayerWinner(alivePlayers.get(0));
        }
    }

    private void declarePlayerWinner(UHCPlayer winner) {
        endGame(winner.getName(), Messages.VICTORY_WINNER_SOLO(winner.getName()),
                Messages.VICTORY_PLAYER_TITLE(winner.getName()));
    }

    private void declareTeamWinner(UHCTeam team) {
        endGame(team.getName(), Messages.VICTORY_WINNING_TEAM(team.getName()),
                Messages.VICTORY_TEAM_TITLE(team.getName()));
    }

    private void endGame(String winnerName, String broadcastMsg, String titleMsg) {
        settings.setGameState(GameState.ENDED);
        plugin.getGameTimerManager().stop();
        plugin.getSkinManager().restoreAllSkins();
        gameStats.setGameEndTime(System.currentTimeMillis());
        gameStats.setWinner(winnerName);

        broadcast("");
        broadcast(Messages.VICTORY_HEADER());
        broadcast(broadcastMsg);
        broadcast(Messages.VICTORY_GAME_DURATION(gameStats.getFormattedDuration()));
        broadcast("");

        Bukkit.getOnlinePlayers().forEach(p -> sendTitle(p, titleMsg, Messages.VICTORY_TITLE_WON()));
    }

    private void declareDraw() {
        settings.setGameState(GameState.ENDED);
        plugin.getGameTimerManager().stop();
        plugin.getSkinManager().restoreAllSkins();
        gameStats.setWinner("Draw");

        broadcast(Messages.DRAW_HEADER());
        Bukkit.getOnlinePlayers().forEach(p -> sendTitle(p, Messages.DRAW_TITLE_SCREEN(), Messages.DRAW_NO_WINNER()));
    }

    private void broadcast(String message) {
        if (message == null) {
            return;
        }
        Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    private void sendTitle(Player player, String title, String subtitle) {
        Title t = Title.title(
                LegacyComponentSerializer.legacySection().deserialize(title),
                LegacyComponentSerializer.legacySection().deserialize(subtitle),
                Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3500), Duration.ofMillis(1000)));
        player.showTitle(t);
    }

    // Getters and Setters
    public GameStats getGameStats() {
        return gameStats;
    }

    public void setGameStats(GameStats s) {
        this.gameStats = s;
    }

    public int getCurrentWorldSize() {
        return currentWorldSize;
    }

    public void setCurrentWorldSize(int s) {
        this.currentWorldSize = s;
    }

    /**
     * Checks if teams have already been formed.
     *
     * @return true if teams are locked in.
     */
    public boolean isTeamsFormed() {
        return teamsFormed;
    }

    /**
     * Sets whether teams are considered formed.
     */
    public void setTeamsFormed(boolean f) {
        this.teamsFormed = f;
    }
}
