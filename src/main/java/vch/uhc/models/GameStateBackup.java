package vch.uhc.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;

import vch.uhc.misc.enums.GameState;

/**
 * Represents a complete backup of the UHC game state.
 * Used to save/restore game progress when pausing or restarting server.
 */
public class GameStateBackup {
    
    // Game timing
    private int elapsedHours;
    private int elapsedMinutes;
    private int elapsedSeconds;
    
    // Agreement state
    private int agreementElapsedSeconds;
    private boolean agreementActive;
    
    // Buffs state
    private int buffsElapsedSeconds;
    private boolean buffsApplied;
    
    // Skin shuffle state
    private int skinShuffleElapsedSeconds;
    
    // Game state
    private GameState gameState;
    private int currentWorldSize;
    private boolean isCheckingProximity;
    private boolean teamsFormed;
    
    // Players state
    private List<PlayerStateData> players;
    
    // Teams state
    private List<TeamStateData> teams;
    
    // Game stats
    private Long gameStartTime;
    private Map<String, PlayerStatsData> playerStats;
    
    public GameStateBackup() {
        this.players = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.playerStats = new HashMap<>();
    }
    
    // Getters and setters
    public int getElapsedHours() { return elapsedHours; }
    public void setElapsedHours(int elapsedHours) { this.elapsedHours = elapsedHours; }
    
    public int getElapsedMinutes() { return elapsedMinutes; }
    public void setElapsedMinutes(int elapsedMinutes) { this.elapsedMinutes = elapsedMinutes; }
    
    public int getElapsedSeconds() { return elapsedSeconds; }
    public void setElapsedSeconds(int elapsedSeconds) { this.elapsedSeconds = elapsedSeconds; }
    
    public int getAgreementElapsedSeconds() { return agreementElapsedSeconds; }
    public void setAgreementElapsedSeconds(int agreementElapsedSeconds) { this.agreementElapsedSeconds = agreementElapsedSeconds; }
    
    public boolean isAgreementActive() { return agreementActive; }
    public void setAgreementActive(boolean agreementActive) { this.agreementActive = agreementActive; }
    
    public int getBuffsElapsedSeconds() { return buffsElapsedSeconds; }
    public void setBuffsElapsedSeconds(int buffsElapsedSeconds) { this.buffsElapsedSeconds = buffsElapsedSeconds; }
    
    public boolean isBuffsApplied() { return buffsApplied; }
    public void setBuffsApplied(boolean buffsApplied) { this.buffsApplied = buffsApplied; }
    
    public int getSkinShuffleElapsedSeconds() { return skinShuffleElapsedSeconds; }
    public void setSkinShuffleElapsedSeconds(int skinShuffleElapsedSeconds) { this.skinShuffleElapsedSeconds = skinShuffleElapsedSeconds; }
    
    public GameState getGameState() { return gameState; }
    public void setGameState(GameState gameState) { this.gameState = gameState; }
    
    public int getCurrentWorldSize() { return currentWorldSize; }
    public void setCurrentWorldSize(int currentWorldSize) { this.currentWorldSize = currentWorldSize; }
    
    public boolean isCheckingProximity() { return isCheckingProximity; }
    public void setCheckingProximity(boolean checkingProximity) { this.isCheckingProximity = checkingProximity; }
    
    public boolean isTeamsFormed() { return teamsFormed; }
    public void setTeamsFormed(boolean teamsFormed) { this.teamsFormed = teamsFormed; }
    
    public List<PlayerStateData> getPlayers() { return players; }
    public void setPlayers(List<PlayerStateData> players) { this.players = players; }
    
    public List<TeamStateData> getTeams() { return teams; }
    public void setTeams(List<TeamStateData> teams) { this.teams = teams; }
    
    public Long getGameStartTime() { return gameStartTime; }
    public void setGameStartTime(Long gameStartTime) { this.gameStartTime = gameStartTime; }
    
    public Map<String, PlayerStatsData> getPlayerStats() { return playerStats; }
    public void setPlayerStats(Map<String, PlayerStatsData> playerStats) { this.playerStats = playerStats; }
    
    /**
     * Player state data for serialization
     */
    public static class PlayerStateData {
        private String uuid;
        private String name;
        private int lives;
        private LocationData spawn;
        private boolean isPlaying;
        private int kills;
        private int deaths;
        private boolean ironman;
        private String teamName;
        
        public PlayerStateData() {}
        
        public PlayerStateData(UUID uuid, String name, int lives, Location spawn, boolean isPlaying, 
                              int kills, int deaths, boolean ironman, String teamName) {
            this.uuid = uuid.toString();
            this.name = name;
            this.lives = lives;
            this.spawn = spawn != null ? new LocationData(spawn) : null;
            this.isPlaying = isPlaying;
            this.kills = kills;
            this.deaths = deaths;
            this.ironman = ironman;
            this.teamName = teamName;
        }
        
        public String getUuid() { return uuid; }
        public void setUuid(String uuid) { this.uuid = uuid; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public int getLives() { return lives; }
        public void setLives(int lives) { this.lives = lives; }
        
        public LocationData getSpawn() { return spawn; }
        public void setSpawn(LocationData spawn) { this.spawn = spawn; }
        
        public boolean isPlaying() { return isPlaying; }
        public void setPlaying(boolean playing) { isPlaying = playing; }
        
        public int getKills() { return kills; }
        public void setKills(int kills) { this.kills = kills; }
        
        public int getDeaths() { return deaths; }
        public void setDeaths(int deaths) { this.deaths = deaths; }
        
        public boolean isIronman() { return ironman; }
        public void setIronman(boolean ironman) { this.ironman = ironman; }
        
        public String getTeamName() { return teamName; }
        public void setTeamName(String teamName) { this.teamName = teamName; }
    }
    
    /**
     * Team state data for serialization
     */
    public static class TeamStateData {
        private String name;
        private List<String> memberUUIDs;
        
        public TeamStateData() {
            this.memberUUIDs = new ArrayList<>();
        }
        
        public TeamStateData(String name, List<UUID> memberUUIDs) {
            this.name = name;
            this.memberUUIDs = new ArrayList<>();
            for (UUID uuid : memberUUIDs) {
                this.memberUUIDs.add(uuid.toString());
            }
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public List<String> getMemberUUIDs() { return memberUUIDs; }
        public void setMemberUUIDs(List<String> memberUUIDs) { this.memberUUIDs = memberUUIDs; }
    }
    
    /**
     * Location data for serialization
     */
    public static class LocationData {
        private String world;
        private double x;
        private double y;
        private double z;
        private float yaw;
        private float pitch;
        
        public LocationData() {}
        
        public LocationData(Location loc) {
            this.world = loc.getWorld().getName();
            this.x = loc.getX();
            this.y = loc.getY();
            this.z = loc.getZ();
            this.yaw = loc.getYaw();
            this.pitch = loc.getPitch();
        }
        
        public Location toLocation() {
            return new Location(
                org.bukkit.Bukkit.getWorld(world),
                x, y, z, yaw, pitch
            );
        }
        
        public String getWorld() { return world; }
        public void setWorld(String world) { this.world = world; }
        
        public double getX() { return x; }
        public void setX(double x) { this.x = x; }
        
        public double getY() { return y; }
        public void setY(double y) { this.y = y; }
        
        public double getZ() { return z; }
        public void setZ(double z) { this.z = z; }
        
        public float getYaw() { return yaw; }
        public void setYaw(float yaw) { this.yaw = yaw; }
        
        public float getPitch() { return pitch; }
        public void setPitch(float pitch) { this.pitch = pitch; }
    }
    
    /**
     * Player stats data for serialization
     */
    public static class PlayerStatsData {
        private String playerName;
        private int kills;
        private int deaths;
        private long survivalTime;
        
        public PlayerStatsData() {}
        
        public PlayerStatsData(String playerName, int kills, int deaths, long survivalTime) {
            this.playerName = playerName;
            this.kills = kills;
            this.deaths = deaths;
            this.survivalTime = survivalTime;
        }
        
        public String getPlayerName() { return playerName; }
        public void setPlayerName(String playerName) { this.playerName = playerName; }
        
        public int getKills() { return kills; }
        public void setKills(int kills) { this.kills = kills; }
        
        public int getDeaths() { return deaths; }
        public void setDeaths(int deaths) { this.deaths = deaths; }
        
        public long getSurvivalTime() { return survivalTime; }
        public void setSurvivalTime(long survivalTime) { this.survivalTime = survivalTime; }
    }
}
