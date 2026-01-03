package vch.uhc.managers.game;

import java.time.Duration;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import vch.uhc.UHC;
import vch.uhc.misc.Messages;
import vch.uhc.misc.Settings;
import vch.uhc.misc.enums.GameState;

/**
 * Manages game timers and timed events (agreement, buffs, border, etc.).
 * Handles the main game loop ticker.
 */
public class GameTimerManager {

    private final UHC plugin;
    private final Settings settings;
    private BukkitTask mainTask;

    private int elapsedHours = 0;
    private int elapsedMinutes = 0;
    private int elapsedSeconds = 0;

    private int agreementElapsedSeconds = 0;
    private boolean agreementActive = false;

    private int buffsElapsedSeconds = 0;
    private boolean buffsApplied = false;

    private int skinShuffleElapsedSeconds = 0;
    private boolean isCheckingProximity = false;

    public GameTimerManager() {
        this.plugin = UHC.getPlugin();
        this.settings = plugin.getSettings();
    }

    /**
     * Starts the main game loop task.
     */
    public void start() {
        if (mainTask != null) {
            mainTask.cancel();
        }

        mainTask = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 20L, 20L);
    }

    /**
     * Stops and cancels the main game loop task.
     */
    public void stop() {
        if (mainTask != null) {
            mainTask.cancel();
            mainTask = null;
        }
    }

    /**
     * The main tick method called every second. Manages time progression, event
     * checks, and scoreboard updates.
     */
    private void tick() {
        if (settings.getGameState() != GameState.IN_PROGRESS) {
            return;
        }

        // 1. Advance Game Time
        elapsedSeconds++;
        if (elapsedSeconds >= 60) {
            elapsedSeconds = 0;
            elapsedMinutes++;
            if (elapsedMinutes >= 60) {
                elapsedMinutes = 0;
                elapsedHours++;
            }
        }

        // 2. Continuous Tasks
        plugin.getUHCManager().checkInGameTeamsState();
        plugin.getUHCManager().checkGameState(); // Win conditions etc.

        // 3. Conditional Timed Events
        checkSkinShuffleState();
        checkAgreementState();
        checkBuffsState();
        checkWorldBorderState();

        // 4. Update Scoreboards
        plugin.getScoreboardManager().updateAllScoreboards();
    }

    /**
     * Checks if it's time to shuffle player skins based on current settings.
     */
    private void checkSkinShuffleState() {
        if (!settings.isSkinShuffleEnabled()) {
            return;
        }

        int shuffleInterval = settings.getSkinShuffleHours() * 3600
                + settings.getSkinShuffleMinutes() * 60
                + settings.getSkinShuffleSeconds();

        if (shuffleInterval <= 0) {
            return;
        }

        skinShuffleElapsedSeconds++;
        if (skinShuffleElapsedSeconds >= shuffleInterval) {
            skinShuffleElapsedSeconds = 0;
            plugin.getSkinManager().shuffleAndAssignSkins();
        }
    }

    /**
     * Manages the PvP agreement period. Decides when PvP should be enabled.
     */
    private void checkAgreementState() {
        int totalAgreementSeconds = settings.getAgreementHours() * 3600
                + settings.getAgreementMinutes() * 60
                + settings.getAgreementSeconds();

        if (totalAgreementSeconds <= 0) {
            agreementActive = false;
            return;
        }

        agreementElapsedSeconds++;
        int remainingSeconds = totalAgreementSeconds - agreementElapsedSeconds;

        if (remainingSeconds > 0) {
            agreementActive = true;
            if (remainingSeconds < 11) {
                Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(Messages.PVP_WARNING_SECONDS(remainingSeconds)));
                Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 1.5f));
            }
        } else if (remainingSeconds == 0) {
            agreementActive = false;
            broadcastAgreementEnded();
        } else {
            agreementActive = false;
        }
    }

    /**
     * Broadcasts the end of the PvP agreement period with visual/audio cues.
     */
    private void broadcastAgreementEnded() {
        Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(""));
        Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(Messages.PVP_ACTIVATED_LINE()));
        Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(Messages.PVP_ACTIVATED()));
        Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(Messages.PVP_ACTIVATED_LINE()));
        Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(""));

        Bukkit.getOnlinePlayers().forEach(p -> {
            sendTitle(p, Messages.PVP_ACTIVATED_TITLE(), Messages.PVP_ACTIVATED_SUBTITLE());
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 0.6f, 1.2f);
        });
    }

    /**
     * Checks if the time for global buffs has been reached.
     */
    private void checkBuffsState() {
        if (!settings.isBuffsEnabled()) {
            return;
        }

        int totalBuffsSeconds = settings.getBuffsHours() * 3600
                + settings.getBuffsMinutes() * 60
                + settings.getBuffsSeconds();

        if (totalBuffsSeconds <= 0) {
            return;
        }

        buffsElapsedSeconds++;
        int remainingSeconds = totalBuffsSeconds - buffsElapsedSeconds;

        if (remainingSeconds == 0 && !buffsApplied) {
            applyBuffs();
        }
    }

    /**
     * Applies global health and resistance buffs to all online players.
     */
    private void applyBuffs() {
        buffsApplied = true;
        Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(""));
        Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(Messages.BUFFS_ACTIVATED_BORDER()));
        Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(Messages.BUFFS_ACTIVATED_TITLE()));
        Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(Messages.BUFFS_ACTIVATED_HEARTS((int) settings.getExtraHearts())));
        Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(Messages.BUFFS_ACTIVATED_RESISTANCE()));
        Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(Messages.BUFFS_ACTIVATED_BORDER()));
        Bukkit.broadcast(LegacyComponentSerializer.legacySection().deserialize(""));

        Bukkit.getOnlinePlayers().forEach(p -> {
            AttributeInstance maxHealthAttr = p.getAttribute(Attribute.MAX_HEALTH);
            if (maxHealthAttr != null) {
                double currentMaxHealth = maxHealthAttr.getValue();
                double newMaxHealth = currentMaxHealth + (settings.getExtraHearts() * 2.0);
                maxHealthAttr.setBaseValue(newMaxHealth);
                p.setHealth(newMaxHealth);
            }

            p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, 1, false, true, true));
            sendTitle(p, Messages.BUFFS_ACTIVATED_TITLE_SCREEN(), Messages.BUFFS_ACTIVATED_SUBTITLE((int) settings.getExtraHearts()));
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.7f, 1.0f);
        });
    }

    /**
     * Manages the current world border state based on the selected BorderType.
     */
    private void checkWorldBorderState() {
        int maxWorldSize = settings.getMaxWorldSize();
        int minWorldSize = settings.getMinWorldSize();
        int totalGameSeconds = settings.getGameHours() * 3600 + settings.getGameMinutes() * 60 + settings.getGameSeconds();
        int currentElapsedSeconds = getTotalElapsedSeconds();

        switch (settings.getBorderType()) {
            case GRADUAL -> {
                if (totalGameSeconds <= 0) {
                    return;
                }
                double totalBorderChange = (maxWorldSize - minWorldSize) * 2.0;
                double borderStep = totalBorderChange / totalGameSeconds;
                plugin.getWorldManager().shrinkBorder(borderStep);
            }
            case INSTANT -> {
                if (currentElapsedSeconds >= totalGameSeconds) {
                    plugin.getWorldManager().setBorderSize(minWorldSize * 2.0);
                }
            }
            case THRESHOLD -> {
                int start = settings.getThresholdStartHours() * 3600 + settings.getThresholdStartMinutes() * 60 + settings.getThresholdStartSeconds();
                int end = settings.getThresholdEndHours() * 3600 + settings.getThresholdEndMinutes() * 60 + settings.getThresholdEndSeconds();

                if (currentElapsedSeconds >= start) {
                    if (currentElapsedSeconds >= end) {
                        plugin.getWorldManager().setBorderSize(minWorldSize * 2.0);
                    } else {
                        int duration = end - start;
                        if (duration > 0) {
                            double totalBorderChange = (maxWorldSize - minWorldSize) * 2.0;
                            double borderStep = totalBorderChange / duration;
                            plugin.getWorldManager().shrinkBorder(borderStep);
                        }
                    }
                }
            }
            default -> {
            }
        }
    }

    private void sendTitle(Player player, String title, String subtitle) {
        Title t = Title.title(
                LegacyComponentSerializer.legacySection().deserialize(title),
                LegacyComponentSerializer.legacySection().deserialize(subtitle),
                Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3500), Duration.ofMillis(1000)));
        player.showTitle(t);
    }

    // Getters and Setters
    public int getElapsedHours() {
        return elapsedHours;
    }

    public void setElapsedHours(int h) {
        this.elapsedHours = h;
    }

    public int getElapsedMinutes() {
        return elapsedMinutes;
    }

    public void setElapsedMinutes(int m) {
        this.elapsedMinutes = m;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(int s) {
        this.elapsedSeconds = s;
    }

    public int getTotalElapsedSeconds() {
        return elapsedHours * 3600 + elapsedMinutes * 60 + elapsedSeconds;
    }

    public int getAgreementElapsedSeconds() {
        return agreementElapsedSeconds;
    }

    public void setAgreementElapsedSeconds(int s) {
        this.agreementElapsedSeconds = s;
    }

    public int getAgreementRemainingSeconds() {
        int totalAgreementSeconds = settings.getAgreementHours() * 3600
                + settings.getAgreementMinutes() * 60
                + settings.getAgreementSeconds();
        return Math.max(0, totalAgreementSeconds - agreementElapsedSeconds);
    }

    public boolean isAgreementActive() {
        return agreementActive;
    }

    public void setAgreementActive(boolean a) {
        this.agreementActive = a;
    }

    public int getBuffsElapsedSeconds() {
        return buffsElapsedSeconds;
    }

    public void setBuffsElapsedSeconds(int s) {
        this.buffsElapsedSeconds = s;
    }

    public boolean isBuffsApplied() {
        return buffsApplied;
    }

    public void setBuffsApplied(boolean b) {
        this.buffsApplied = b;
    }

    public int getSkinShuffleElapsedSeconds() {
        return skinShuffleElapsedSeconds;
    }

    public void setSkinShuffleElapsedSeconds(int s) {
        this.skinShuffleElapsedSeconds = s;
    }

    public boolean isCheckingProximity() {
        return isCheckingProximity;
    }

    public void setCheckingProximity(boolean c) {
        this.isCheckingProximity = c;
    }
}
