package vch.uhc.managers;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import vch.uhc.UHC;
import vch.uhc.models.GameStateBackup;
import vch.uhc.models.GameStateBackup.PlayerStateData;
import vch.uhc.models.GameStateBackup.PlayerStatsData;
import vch.uhc.models.GameStateBackup.TeamStateData;
import vch.uhc.models.UHCPlayer;
import vch.uhc.models.UHCTeam;

/**
 * Manages game state backups for pausing and server restarts.
 * Separates game state (runtime) from settings (configuration).
 */
public class BackupManager {
    
    private final UHC plugin;
    private final Gson gson;
    private final File backupFile;
    
    public BackupManager() {
        this.plugin = UHC.getPlugin();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.backupFile = new File(plugin.getDataFolder(), "gamestate.json");
    }
    
    /**
     * Saves the current game state to backup file.
     * Should be called when pausing or before server shutdown.
     */
    public void saveGameState() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            
            GameStateBackup backup = createBackup();
            String json = gson.toJson(backup);
            
            try (FileWriter writer = new FileWriter(backupFile)) {
                writer.write(json);
                writer.flush();
            }
            
            plugin.getLogger().info("Game state saved successfully to " + backupFile.getAbsolutePath());
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save game state", e);
        }
    }
    
    /**
     * Loads game state from backup file.
     * Should be called when resuming after pause or server restart.
     * 
     * @return true if state was loaded successfully, false otherwise
     */
    public boolean loadGameState() {
        try {
            if (!backupFile.exists()) {
                plugin.getLogger().info("No game state backup found");
                return false;
            }
            
            String json = new String(java.nio.file.Files.readAllBytes(backupFile.toPath()));
            GameStateBackup backup = gson.fromJson(json, GameStateBackup.class);
            
            restoreBackup(backup);
            
            plugin.getLogger().info("Game state loaded successfully from " + backupFile.getAbsolutePath());
            return true;
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load game state", e);
            return false;
        }
    }
    
    /**
     * Deletes the backup file.
     * Should be called when game ends normally.
     */
    public void clearBackup() {
        if (backupFile.exists()) {
            backupFile.delete();
            plugin.getLogger().info("Game state backup cleared");
        }
    }
    
    /**
     * Checks if a backup file exists.
     */
    public boolean hasBackup() {
        return backupFile.exists();
    }
    
    /**
     * Creates a backup object from current game state.
     */
    private GameStateBackup createBackup() {
        GameStateBackup backup = new GameStateBackup();
        UHCManager uhcManager = plugin.getUHCManager();
        
        // Save timing state
        backup.setElapsedHours(uhcManager.getElapsedHours());
        backup.setElapsedMinutes(uhcManager.getElapsedMinutes());
        backup.setElapsedSeconds(uhcManager.getElapsedSeconds());
        
        // Save agreement state
        backup.setAgreementElapsedSeconds(uhcManager.getAgreementElapsedSeconds());
        backup.setAgreementActive(uhcManager.isAgreementActive());
        
        // Save buffs state
        backup.setBuffsElapsedSeconds(uhcManager.getBuffsElapsedSeconds());
        backup.setBuffsApplied(uhcManager.isBuffsApplied());
        
        // Save skin shuffle state
        backup.setSkinShuffleElapsedSeconds(uhcManager.getSkinShuffleElapsedSeconds());
        
        // Save game state
        backup.setGameState(plugin.getSettings().getGameState());
        backup.setCurrentWorldSize(uhcManager.getCurrentWorldSize());
        backup.setCheckingProximity(uhcManager.isCheckingProximity());
        backup.setTeamsFormed(uhcManager.isTeamsFormed());
        
        // Save players
        List<PlayerStateData> playersData = new ArrayList<>();
        for (UHCPlayer player : plugin.getPlayerManager().getPlayers()) {
            PlayerStateData data = new PlayerStateData(
                player.getUuid(),
                player.getName(),
                player.getLives(),
                player.getSpawn(),
                player.isPlaying(),
                player.getKills(),
                player.getDeaths(),
                player.isIronman(),
                player.getTeam() != null ? player.getTeam().getName() : null
            );
            playersData.add(data);
        }
        backup.setPlayers(playersData);
        
        // Save teams
        List<TeamStateData> teamsData = new ArrayList<>();
        for (UHCTeam team : plugin.getTeamManager().getTeams()) {
            List<UUID> memberUUIDs = new ArrayList<>();
            for (UHCPlayer member : team.getMembers()) {
                memberUUIDs.add(member.getUuid());
            }
            TeamStateData teamData = new TeamStateData(team.getName(), memberUUIDs);
            teamsData.add(teamData);
        }
        backup.setTeams(teamsData);
        
        // Save game stats if exists
        if (uhcManager.getGameStats() != null) {
            backup.setGameStartTime(uhcManager.getGameStats().getGameStartTime());
            
            java.util.Map<String, PlayerStatsData> statsMap = new java.util.HashMap<>();
            uhcManager.getGameStats().getPlayerStats().forEach((uuid, stats) -> {
                PlayerStatsData data = new PlayerStatsData(
                    stats.getPlayerName(),
                    stats.getKills(),
                    stats.getDeaths(),
                    stats.getSurvivalTime()
                );
                statsMap.put(uuid.toString(), data);
            });
            backup.setPlayerStats(statsMap);
        }
        
        return backup;
    }
    
    /**
     * Restores game state from backup object.
     */
    private void restoreBackup(GameStateBackup backup) {
        UHCManager uhcManager = plugin.getUHCManager();
        
        // Restore timing state
        uhcManager.setElapsedHours(backup.getElapsedHours());
        uhcManager.setElapsedMinutes(backup.getElapsedMinutes());
        uhcManager.setElapsedSeconds(backup.getElapsedSeconds());
        
        // Restore agreement state
        uhcManager.setAgreementElapsedSeconds(backup.getAgreementElapsedSeconds());
        uhcManager.setAgreementActive(backup.isAgreementActive());
        
        // Restore buffs state
        uhcManager.setBuffsElapsedSeconds(backup.getBuffsElapsedSeconds());
        uhcManager.setBuffsApplied(backup.isBuffsApplied());
        
        // Restore skin shuffle state
        uhcManager.setSkinShuffleElapsedSeconds(backup.getSkinShuffleElapsedSeconds());
        
        // Restore game state
        plugin.getSettings().setGameState(backup.getGameState());
        uhcManager.setCurrentWorldSize(backup.getCurrentWorldSize());
        uhcManager.setCheckingProximity(backup.isCheckingProximity());
        uhcManager.setTeamsFormed(backup.isTeamsFormed());
        
        // Clear current players and teams
        plugin.getPlayerManager().getPlayers().clear();
        plugin.getTeamManager().getTeams().clear();
        
        // Restore players
        for (PlayerStateData data : backup.getPlayers()) {
            UHCPlayer player = new UHCPlayer(UUID.fromString(data.getUuid()), data.getName());
            player.setLives(data.getLives());
            if (data.getSpawn() != null) {
                player.setSpawn(data.getSpawn().toLocation());
            }
            player.setPlaying(data.isPlaying());
            player.setKills(data.getKills());
            player.setDeaths(data.getDeaths());
            player.setIronman(data.isIronman());
            plugin.getPlayerManager().addPlayer(player);
        }
        
        // Restore teams
        for (TeamStateData teamData : backup.getTeams()) {
            List<UHCPlayer> members = new ArrayList<>();
            for (String uuidStr : teamData.getMemberUUIDs()) {
                UUID uuid = UUID.fromString(uuidStr);
                UHCPlayer player = plugin.getPlayerManager().getPlayerByUUID(uuid);
                if (player != null) {
                    members.add(player);
                }
            }
            
            if (!members.isEmpty()) {
                UHCTeam team = plugin.getTeamManager().createTeam(members.get(0), teamData.getName());
                for (int i = 1; i < members.size(); i++) {
                    plugin.getTeamManager().addPlayer(team, members.get(i));
                }
            }
        }
        
        // Restore game stats if exists
        if (backup.getGameStartTime() != null) {
            vch.uhc.models.GameStats gameStats = new vch.uhc.models.GameStats();
            // Set start time through reflection or recreate properly
            backup.getPlayerStats().forEach((uuidStr, stats) -> {
                gameStats.recordPlayerStat(
                    UUID.fromString(uuidStr),
                    stats.getPlayerName(),
                    stats.getKills(),
                    stats.getDeaths(),
                    stats.getSurvivalTime()
                );
            });
            uhcManager.setGameStats(gameStats);
        }
    }
}
