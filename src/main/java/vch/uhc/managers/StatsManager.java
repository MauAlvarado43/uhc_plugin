package vch.uhc.managers;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import vch.uhc.UHC;
import vch.uhc.models.UHCPlayer;

/**
 * Manages player statistics and rankings.
 * Provides access to top performers and stat queries.
 */
public class StatsManager {

    private final PlayerManager playerManager;

    public StatsManager() {
        this.playerManager = UHC.getPlugin().getPlayerManager();
    }

    public UHCPlayer getTopKiller() {
        return playerManager.getPlayers().stream()
            .max(Comparator.comparingInt(UHCPlayer::getKills))
            .orElse(null);
    }

    public List<UHCPlayer> getTopKillers(int limit) {
        return playerManager.getPlayers().stream()
            .sorted(Comparator.comparingInt(UHCPlayer::getKills).reversed())
            .limit(limit)
            .collect(Collectors.toList());
    }

    public List<UHCPlayer> getIronmanPlayers() {
        return playerManager.getPlayers().stream()
            .filter(UHCPlayer::isIronman)
            .collect(Collectors.toList());
    }

    public boolean isIronmanWinner(UHCPlayer player) {
        return player.isIronman() && player.isAlive();
    }

    public String getStatsReport() {
        StringBuilder report = new StringBuilder();
        report.append(vch.uhc.misc.Messages.STATS_HEADER()).append("\n");
        
        UHCPlayer topKiller = getTopKiller();
        if (topKiller != null) {
            report.append(vch.uhc.misc.Messages.STATS_TOP_KILLER(topKiller.getName(), topKiller.getKills())).append("\n");
        }

        List<UHCPlayer> ironmen = getIronmanPlayers();
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
        
        List<UHCPlayer> topPlayers = getTopKillers(limit);
        int position = 1;
        for (UHCPlayer player : topPlayers) {
            leaderboard.append(vch.uhc.misc.Messages.STATS_TOP_KILLERS_ENTRY(position, player.getName(), player.getKills())).append("\n");
            position++;
        }

        return leaderboard.toString();
    }

    public void recordKill(UHCPlayer killer, UHCPlayer victim) {
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

    public void markAsIronman(UHCPlayer player) {
        playerManager.getPlayers().forEach(p -> p.setIronman(false));
        player.setIronman(true);
    }
}
