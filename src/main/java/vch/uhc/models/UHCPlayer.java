package vch.uhc.models;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Represents a UHC player with game-specific stats and state.
 * Wraps Bukkit player functionality with UHC-specific data.
 */
public class UHCPlayer {

    private final UUID uuid;
    private final String name;
    private final String randomName;
    private UHCTeam team;
    private int lives;
    private Location spawn;
    private boolean isPlaying;
    private int kills;
    private int deaths;
    private boolean ironman;

    /**
     * Creates a new UHC player instance.
     * 
     * @param uuid the player's unique identifier
     * @param name the player's display name
     */
    public UHCPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.lives = 1;
        this.randomName = generateRandomName();
        this.isPlaying = true;
        this.kills = 0;
        this.deaths = 0;
        this.ironman = false;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public UHCTeam getTeam() {
        return team;
    }

    public void setTeam(UHCTeam team) {
        this.team = team;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void addLife() {
        this.lives++;
    }

    public void removeLife() {
        this.lives--;
    }

    public boolean isAlive() {
        return this.lives > 0;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public String getRandomName() {
        return randomName;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        this.kills++;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        this.deaths++;
    }

    public boolean isIronman() {
        return ironman;
    }

    public void setIronman(boolean ironman) {
        this.ironman = ironman;
    }

    /**
     * Generates a randomized obfuscated name for anonymization.
     * Uses Minecraft color codes for obfuscation effect.
     * 
     * @return obfuscated random name string
     */
    private String generateRandomName() {

        String randomChars = "ABCDEFGHIJKLMNOPQ";
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append("\u00a7k").append(randomChars.charAt((int) (Math.random() * randomChars.length())));
        }

        return sb.toString();

    }

}
