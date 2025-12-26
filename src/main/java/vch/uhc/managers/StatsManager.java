package vch.uhc.managers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import vch.uhc.UHC;
import vch.uhc.models.Player;

public class StatsManager {

    private final PlayerManager playerManager;

    public StatsManager() {
        this.playerManager = UHC.getPlugin().getPlayerManager();
    }

    public Player getTopKiller() {
        return playerManager.getPlayers().stream()
            .max(Comparator.comparingInt(Player::getKills))
            .orElse(null);
    }

    public List<Player> getTopKillers(int limit) {
        return playerManager.getPlayers().stream()
            .sorted(Comparator.comparingInt(Player::getKills).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }

    public List<Player> getIronmanPlayers() {
        return playerManager.getPlayers().stream()
            .filter(Player::isIronman)
            .collect(Collectors.toList());
    }

    public boolean isIronmanWinner(Player player) {
        return player.isIronman() && player.isAlive();
    }

    public String getStatsReport() {
        StringBuilder report = new StringBuilder();
        report.append(vch.uhc.misc.Messages.STATS_HEADER()).append("\n");
        
        Player topKiller = getTopKiller();
        if (topKiller != null) {
            report.append(vch.uhc.misc.Messages.STATS_TOP_KILLER(topKiller.getName(), topKiller.getKills())).append("\n");
        }

        List<Player> ironmen = getIronmanPlayers();
        if (!ironmen.isEmpty()) {
            report.append(vch.uhc.misc.Messages.STATS_IRONMAN(ironmen.size())).append("\n");
            ironmen.forEach(p -> 
                report.append(vch.uhc.misc.Messages.STATS_IRONMAN_ENTRY(p.getName(), p.isAlive())).append("\n")
            );
        }

        return report.toString();
    }

    public String getKillsLeaderboard(int limit) {
        StringBuilder leaderboard = new StringBuilder();
        leaderboard.append(vch.uhc.misc.Messages.STATS_TOP_KILLERS_HEADER()).append("\n");
        
        List<Player> topPlayers = getTopKillers(limit);
        int position = 1;
        for (Player player : topPlayers) {
            leaderboard.append(vch.uhc.misc.Messages.STATS_TOP_KILLERS_ENTRY(position, player.getName(), player.getKills())).append("\n");
            position++;
        }

        return leaderboard.toString();
    }

    public void recordKill(Player killer, Player victim) {
        if (killer != null) {
            killer.addKill();
        }
        if (victim != null) {
            victim.addDeath();
        }
    }

    public void resetStats() {
        playerManager.getPlayers().forEach(player -> {
            player.setIronman(false);
        });
    }

    public void markAsIronman(Player player) {
        playerManager.getPlayers().forEach(p -> p.setIronman(false));
        player.setIronman(true);
    }
}
