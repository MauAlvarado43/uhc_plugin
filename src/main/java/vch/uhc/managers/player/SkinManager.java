package vch.uhc.managers.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import vch.uhc.UHC;
import vch.uhc.misc.Messages;
import vch.uhc.models.SkinAssignment;

public class SkinManager {

    private final UHC plugin;
    private SkinsRestorer skinsAPI;
    private final Map<UUID, SkinAssignment> skinAssignments = new ConcurrentHashMap<>();
    private final Set<UUID> revealedPlayers = ConcurrentHashMap.newKeySet();

    public SkinManager() {
        this.plugin = UHC.getPlugin();
        initializeAPI();
    }

    private void initializeAPI() {
        try {
            if (Bukkit.getPluginManager().getPlugin("SkinsRestorer") != null) {
                this.skinsAPI = SkinsRestorerProvider.get();
                plugin.getLogger().info("SkinsRestorer API initialized successfully!");
            } else {
                plugin.getLogger().warning("SkinsRestorer plugin not found! Skin disguise disabled.");
            }
        } catch (Exception e) {
            plugin.getLogger().warning(() -> "Failed to initialize SkinsRestorer API: " + e.getMessage());
        }
    }

    public void shuffleAndAssignSkins() {
        try {

            if (skinsAPI == null) {
                return;
            }

            Collection<vch.uhc.models.UHCPlayer> uhcPlayers = plugin.getPlayerManager().getPlayers();

            List<Player> onlinePlayers = uhcPlayers.stream()
                    .map(p -> Bukkit.getPlayer(p.getUuid()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (onlinePlayers.size() < 2) {
                return;
            }

            clearAssignments();

            List<SkinAssignment> assignments = createShuffledAssignments(onlinePlayers);

            for (int i = 0; i < assignments.size(); i++) {
                SkinAssignment assignment = assignments.get(i);
                skinAssignments.put(assignment.getPlayerUUID(), assignment);
                
                long delay = i * 2L;
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    applySkinToPlayer(assignment);
                }, delay);
            }

            long totalDelay = assignments.size() * 2L + 10L;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Bukkit.getServer().broadcast(Component.text(Messages.SKIN_SHUFFLE_BORDER()));
                Bukkit.getServer().broadcast(Component.text(Messages.SKIN_SHUFFLE_TITLE()));
                Bukkit.getServer().broadcast(Component.text(Messages.SKIN_SHUFFLE_DESCRIPTION()));
                Bukkit.getServer().broadcast(Component.text(Messages.SKIN_SHUFFLE_HINT()));
                Bukkit.getServer().broadcast(Component.text(Messages.SKIN_SHUFFLE_BORDER()));
            }, totalDelay);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning(e.getMessage());
        }
    }

    private List<SkinAssignment> createShuffledAssignments(List<Player> players) {
        List<SkinAssignment> assignments = new ArrayList<>();
        
        List<String> playerNames = players.stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        
        List<String> shuffledNames = new ArrayList<>(playerNames);
        
        Random random = new Random();
        boolean hasOwnSkin;
        int maxAttempts = 100;
        int attempts = 0;
        
        do {
            hasOwnSkin = false;
            java.util.Collections.shuffle(shuffledNames, random);
            
            for (int i = 0; i < playerNames.size(); i++) {
                if (playerNames.get(i).equals(shuffledNames.get(i))) {
                    hasOwnSkin = true;
                    break;
                }
            }
            attempts++;
        } while (hasOwnSkin && attempts < maxAttempts);
        
        if (hasOwnSkin) {
            for (int i = 0; i < playerNames.size(); i++) {
                if (playerNames.get(i).equals(shuffledNames.get(i))) {
                    int swapIndex = (i + 1) % playerNames.size();
                    String temp = shuffledNames.get(i);
                    shuffledNames.set(i, shuffledNames.get(swapIndex));
                    shuffledNames.set(swapIndex, temp);
                }
            }
        }
        
        for (int i = 0; i < players.size(); i++) {

            Player player = players.get(i);
            String assignedSkin = shuffledNames.get(i);
            
            assignments.add(new SkinAssignment(
                    player.getUniqueId(),
                    player.getName(),
                    assignedSkin,
                    false));

        }

        return assignments;
    }

    private void applySkinToPlayer(SkinAssignment assignment) {
        Player player = Bukkit.getPlayer(assignment.getPlayerUUID());
        if (player == null) {
            return;
        }

        try {
            // Apply skin
            String command = "skin set " + player.getName() + " " + assignment.getAssignedSkin();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            
            // Apply nametag
            player.displayName(Component.text(assignment.getAssignedSkin()));
            player.playerListName(Component.text(assignment.getAssignedSkin()));
        } catch (CommandException e) {
            plugin.getLogger().warning(e.getMessage());
        }
    }

    public boolean revealSkin(UUID attackedUUID, UUID attackerUUID) {
        try {
            if (revealedPlayers.contains(attackedUUID)) {
                return false;
            }

            SkinAssignment assignment = skinAssignments.get(attackedUUID);
            if (assignment == null) {
                return false;
            }

            Player attacked = Bukkit.getPlayer(attackedUUID);
            Player attacker = Bukkit.getPlayer(attackerUUID);

            if (attacked == null || attacker == null) {
                return false;
            }

            revealedPlayers.add(attackedUUID);
            assignment.setRevealed(true);

            restoreOriginalSkin(attacked);

            attacker.sendMessage("");
            attacker.sendMessage(Messages.SKIN_REVEAL_BORDER());
            attacker.sendMessage(Messages.SKIN_REVEAL_TITLE());
            attacker.sendMessage("");
            attacker.sendMessage(Messages.SKIN_REVEAL_SKIN(assignment.getAssignedSkin()));
            attacker.sendMessage(Messages.SKIN_REVEAL_REAL(assignment.getRealPlayerName()));
            attacker.sendMessage("");
            attacker.sendMessage(Messages.SKIN_REVEAL_BORDER());
            attacker.sendMessage("");

            Bukkit.getServer().broadcast(
                    Component.text(
                            Messages.SKIN_REVEAL_BROADCAST(
                                    attacker.getName(),
                                    assignment.getRealPlayerName())));

            return true;
        } catch (Exception e) {
            plugin.getLogger().warning(e.getMessage());
            return false;
        }
    }

    private void restoreOriginalSkin(Player player) {
        
        try {
            // Restore skin
            String command = "skin clear " + player.getName();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            
            // Restore original nametag
            player.displayName(Component.text(player.getName()));
            player.playerListName(Component.text(player.getName()));
        } catch (CommandException e) {
            plugin.getLogger().warning(e.getMessage());
        }
    }

    public void restoreAllSkins() {
        try {
            if (skinsAPI == null)
                return;

            int i = 0;
            for (UUID playerUUID : skinAssignments.keySet()) {
                try {
                    Player player = Bukkit.getPlayer(playerUUID);
                    if (player != null) {
                        long delay = i * 2L;
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            restoreOriginalSkin(player);
                        }, delay);
                        i++;
                    }
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning(e.getMessage());
                }
            }

            long totalDelay = i * 2L + 10L;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Bukkit.getServer().broadcast(Component.text(Messages.SKIN_RESTORE_SUCCESS()));
            }, totalDelay);
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning(e.getMessage());
        }
    }

    public void clearAssignments() {
        skinAssignments.clear();
        revealedPlayers.clear();
    }

    public boolean isRevealed(UUID playerUUID) {
        return revealedPlayers.contains(playerUUID);
    }

    public Optional<SkinAssignment> getAssignment(UUID playerUUID) {
        return Optional.ofNullable(skinAssignments.get(playerUUID));
    }

    public boolean areSkinsShuffled() {
        return !skinAssignments.isEmpty();
    }

    public String getRevealStats() {
        int total = skinAssignments.size();
        int revealed = revealedPlayers.size();
        int hidden = total - revealed;

        return Messages.SKIN_STATS(revealed, hidden);
    }
}
