package vch.uhc.managers.ui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import vch.uhc.UHC;
import vch.uhc.managers.game.GameTimerManager;
import vch.uhc.managers.player.PlayerManager;
import vch.uhc.misc.Messages;
import vch.uhc.misc.Settings;
import vch.uhc.misc.enums.GameMode;
import vch.uhc.models.UHCPlayer;
import vch.uhc.models.UHCTeam;

/**
 * Manages scoreboard display for all players. Handles layout and periodic
 * updates.
 */
public class ScoreboardManager {

    private final UHC plugin;
    private final Settings settings;
    private final PlayerManager playerManager;

    public ScoreboardManager() {
        this.plugin = UHC.getPlugin();
        this.settings = plugin.getSettings();
        this.playerManager = plugin.getPlayerManager();
    }

    /**
     * Initializes a new UHC scoreboard for the given player.
     *
     * @param player The player to receive the scoreboard.
     */
    public void createPlayerScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("uhc", Criteria.DUMMY,
                Component.text(Messages.UHC_SCOREBOARD_TITLE()));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);
    }

    /**
     * Triggers an update for all online players' scoreboards.
     */
    public void updateAllScoreboards() {
        Bukkit.getOnlinePlayers().forEach(this::updatePlayerScoreboard);
    }

    /**
     * Updates the content and scores of a player's individual scoreboard. This
     * method is called every tick.
     *
     * @param player The player whose scoreboard will be updated.
     */
    public void updatePlayerScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("uhc");

        if (objective == null) {
            createPlayerScoreboard(player);
            scoreboard = player.getScoreboard();
            objective = scoreboard.getObjective("uhc");
            if (objective == null) {
                return;
            }
        }

        // Reset current scores to redraw everything
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        int score = 15;
        GameTimerManager timerManager = plugin.getGameTimerManager();

        objective.getScore(" ").setScore(score--);
        objective.getScore(Messages.UHC_SCOREBOARD_TIME(
                timerManager.getElapsedHours(),
                timerManager.getElapsedMinutes(),
                timerManager.getElapsedSeconds()))
                .setScore(score--);
        objective.getScore("  ").setScore(score--);

        UHCPlayer uhcPlayer = playerManager.getPlayerByUUID(player.getUniqueId());
        if (uhcPlayer != null) {
            objective.getScore(Messages.SCOREBOARD_YOUR_LIVES(uhcPlayer.getLives())).setScore(score--);
            objective.getScore("    ").setScore(score--);
        }

        if (uhcPlayer != null && uhcPlayer.getTeam() != null) {
            UHCTeam team = uhcPlayer.getTeam();
            String teamName = LegacyComponentSerializer.legacyAmpersand().serialize(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(team.getName()));
            objective.getScore(Messages.SCOREBOARD_TEAM(teamName)).setScore(score--);

            for (UHCPlayer member : team.getMembers()) {
                if (!member.getUuid().equals(player.getUniqueId())) {
                    Player memberPlayer = Bukkit.getPlayer(member.getUuid());
                    if (memberPlayer != null && member.isAlive()) {
                        double health = memberPlayer.getHealth();
                        // Formatting health and name without hardcoded § if possible, or using §f as a standard reset
                        objective.getScore(Messages.SCOREBOARD_HEALTH(health) + " \u00a7f" + member.getName())
                                .setScore(score--);
                    } else if (!member.isAlive()) {
                        objective.getScore(Messages.SCOREBOARD_MEMBER_DEAD(member.getName())).setScore(score--);
                    }
                }
            }
            objective.getScore("   ").setScore(score--);
        }

        if (settings.getGameMode() == GameMode.RESOURCE_RUSH) {
            List<Material> items = plugin.getGameModeManager().getResourceRushItems();
            if (items != null && !items.isEmpty()) {
                objective.getScore(Messages.SCOREBOARD_OBJECTIVES()).setScore(score--);

                int itemsToShow = Math.min(items.size(), 5);
                for (int i = 0; i < itemsToShow; i++) {
                    Material item = items.get(i);
                    boolean hasItem = player.getInventory().contains(item);
                    String status = hasItem ? Messages.SCOREBOARD_OBJECTIVE_CHECKED()
                            : Messages.SCOREBOARD_OBJECTIVE_UNCHECKED();
                    String itemName = item.name().replace("_", " ");
                    objective.getScore(status + "\u00a7f " + itemName).setScore(score--);
                }

                if (items.size() > 5) {
                    objective.getScore(Messages.SCOREBOARD_MORE_ITEMS(items.size() - 5)).setScore(score--);
                }
                objective.getScore("    ").setScore(score--);
            }
        }

        int totalAgreementSeconds = settings.getAgreementHours() * 3600
                + settings.getAgreementMinutes() * 60
                + settings.getAgreementSeconds();

        if (totalAgreementSeconds > 0) {
            objective.getScore(Messages.SCOREBOARD_AGREEMENT()).setScore(score--);
            int remainingSeconds = Math.max(0, totalAgreementSeconds - timerManager.getAgreementElapsedSeconds());

            if (remainingSeconds > 0) {
                int remHours = remainingSeconds / 3600;
                int remMinutes = (remainingSeconds % 3600) / 60;
                int remSeconds = remainingSeconds % 60;
                String timeStr = String.format("%02d:%02d:%02d", remHours, remMinutes, remSeconds);
                objective.getScore(Messages.SCOREBOARD_ENDS_IN(timeStr)).setScore(score--);
            } else {
                objective.getScore(Messages.SCOREBOARD_FINISHED()).setScore(score--);
            }
            objective.getScore("     ").setScore(score--);
        }
    }

    /**
     * Ensures the player has the UHC scoreboard objective registered.
     */
    public void applyScoreboardToPlayer(Player player) {
        if (player.getScoreboard().getObjective("uhc") == null) {
            createPlayerScoreboard(player);
        }
    }
}
