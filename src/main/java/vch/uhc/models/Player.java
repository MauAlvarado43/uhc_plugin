package vch.uhc.models;

import java.util.UUID;

import org.bukkit.Location;

import net.md_5.bungee.api.ChatColor;

public class Player {

    final private UUID uuid;
    final private String name;
    final private String randomName;
    private Team team;
    private int lives;
    private Location spawn;
    private boolean isPlaying;
    private int kills;
    private int deaths;
    private boolean ironman;

    public Player(UUID uuid, String name) {
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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
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

    public org.bukkit.entity.Player getBukkitPlayer() {
        return org.bukkit.Bukkit.getPlayer(this.uuid);
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

    private String generateRandomName() {

        String randomChars = "ABCDEFGHIJKLMNOPQ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++)
            sb.append(ChatColor.MAGIC).append(randomChars.charAt((int) (Math.random() * randomChars.length())));

        return sb.toString();

    }

}