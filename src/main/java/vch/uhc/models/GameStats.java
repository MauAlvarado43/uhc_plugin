package vch.uhc.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameStats {

    private final long gameStartTime;
    private long gameEndTime;
    private String winner;
    private final Map<UUID, PlayerStats> playerStats;

    public GameStats() {
        this.gameStartTime = System.currentTimeMillis();
        this.playerStats = new HashMap<>();
    }

    public void recordPlayerStat(UUID uuid, String playerName, int kills, int deaths, long survivalTime) {
        playerStats.put(uuid, new PlayerStats(playerName, kills, deaths, survivalTime));
    }

    public long getGameStartTime() {
        return gameStartTime;
    }

    public long getGameEndTime() {
        return gameEndTime;
    }

    public void setGameEndTime(long gameEndTime) {
        this.gameEndTime = gameEndTime;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Map<UUID, PlayerStats> getPlayerStats() {
        return playerStats;
    }

    public long getTotalGameDuration() {
        return gameEndTime - gameStartTime;
    }

    public String getFormattedDuration() {
        long duration = getTotalGameDuration() / 1000;
        long hours = duration / 3600;
        long minutes = (duration % 3600) / 60;
        long seconds = duration % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static class PlayerStats {
        private final String playerName;
        private final int kills;
        private final int deaths;
        private final long survivalTime;

        public PlayerStats(String playerName, int kills, int deaths, long survivalTime) {
            this.playerName = playerName;
            this.kills = kills;
            this.deaths = deaths;
            this.survivalTime = survivalTime;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getKills() {
            return kills;
        }

        public int getDeaths() {
            return deaths;
        }

        public long getSurvivalTime() {
            return survivalTime;
        }
    }

}
