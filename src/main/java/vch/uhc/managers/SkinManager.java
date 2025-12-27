package vch.uhc.managers;

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
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.property.SkinIdentifier;
import net.skinsrestorer.api.storage.PlayerStorage;
import vch.uhc.UHC;
import vch.uhc.misc.Messages;
import vch.uhc.models.SkinAssignment;

public class SkinManager {

    private final UHC plugin;
    private SkinsRestorer skinsAPI;
    private final Map<UUID, SkinAssignment> skinAssignments = new ConcurrentHashMap<>();
    private final Set<UUID> revealedPlayers = ConcurrentHashMap.newKeySet();
    private final Map<UUID, SkinIdentifier> originalSkinIds = new ConcurrentHashMap<>();

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
            plugin.getLogger().severe(() -> "Failed to initialize SkinsRestorer API: " + e.getMessage());
        }
    }

    public void shuffleAndAssignSkins() {
        if (skinsAPI == null) {
            plugin.getLogger().warning("Cannot shuffle skins: SkinsRestorer not available");
            return;
        }

        Collection<vch.uhc.models.UHCPlayer> uhcPlayers = plugin.getPlayerManager().getPlayers();
        List<Player> onlinePlayers = uhcPlayers.stream()
            .map(p -> Bukkit.getPlayer(p.getUuid()))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        if (onlinePlayers.size() < 2) {
            plugin.getLogger().warning("Need at least 2 players to shuffle skins");
            return;
        }

        clearAssignments();

        PlayerStorage playerStorage = skinsAPI.getPlayerStorage();

        for (Player player : onlinePlayers) {
            Optional<SkinIdentifier> skinId = playerStorage.getSkinIdOfPlayer(player.getUniqueId());
            skinId.ifPresent(id -> originalSkinIds.put(player.getUniqueId(), id));
        }

        List<SkinAssignment> assignments = createShuffledAssignments(onlinePlayers);

        for (SkinAssignment assignment : assignments) {
            skinAssignments.put(assignment.getPlayerUUID(), assignment);
            applySkinToPlayer(assignment);
        }

        Bukkit.getServer().broadcast(Component.text(Messages.SKIN_SHUFFLE_BORDER()));
        Bukkit.getServer().broadcast(Component.text(Messages.SKIN_SHUFFLE_TITLE()));
        Bukkit.getServer().broadcast(Component.text(Messages.SKIN_SHUFFLE_DESCRIPTION()));
        Bukkit.getServer().broadcast(Component.text(Messages.SKIN_SHUFFLE_HINT()));
        Bukkit.getServer().broadcast(Component.text(Messages.SKIN_SHUFFLE_BORDER()));
    }

    private List<SkinAssignment> createShuffledAssignments(List<Player> players) {
        List<SkinAssignment> assignments = new ArrayList<>();
        List<String> availableSkins = players.stream()
            .map(Player::getName)
            .collect(Collectors.toList());

        Random random = new Random();

        for (Player player : players) {
            List<String> validSkins = availableSkins.stream()
                .filter(skin -> !skin.equals(player.getName()))
                .collect(Collectors.toList());

            if (validSkins.isEmpty()) {
                plugin.getLogger().warning(() -> "No valid skins for " + player.getName());
                continue;
            }

            String assignedSkin = validSkins.get(random.nextInt(validSkins.size()));

            availableSkins.remove(assignedSkin);
            availableSkins.add(player.getName());

            assignments.add(new SkinAssignment(
                player.getUniqueId(),
                player.getName(),
                assignedSkin,
                false
            ));
        }

        return assignments;
    }

    private void applySkinToPlayer(SkinAssignment assignment) {
        Player player = Bukkit.getPlayer(assignment.getPlayerUUID());
        if (player == null) return;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PlayerStorage playerStorage = skinsAPI.getPlayerStorage();
            
            Player assignedPlayer = Bukkit.getPlayer(assignment.getAssignedSkin());
            if (assignedPlayer != null) {
                playerStorage.setSkinIdOfPlayer(player.getUniqueId(), 
                    SkinIdentifier.ofPlayer(assignedPlayer.getUniqueId()));
            } else {
                plugin.getLogger().warning(() -> "Could not find player: " + assignment.getAssignedSkin());
            }
            
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    skinsAPI.getSkinApplier(Player.class).applySkin(player);
                } catch (DataRequestException e) {
                    plugin.getLogger().warning(() -> "Failed to apply skin: " + e.getMessage());
                }
            });

            plugin.getLogger().info(() -> "Applied skin '" + assignment.getAssignedSkin() + 
                                   "' to " + assignment.getRealPlayerName());
        });
    }

    public boolean revealSkin(UUID attackedUUID, UUID attackerUUID) {
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
                    assignment.getRealPlayerName()
                )
            )
        );

        return true;
    }

    private void restoreOriginalSkin(Player player) {
        SkinIdentifier originalSkinId = originalSkinIds.get(player.getUniqueId());
        
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            PlayerStorage playerStorage = skinsAPI.getPlayerStorage();
            
            if (originalSkinId != null) {
                playerStorage.setSkinIdOfPlayer(player.getUniqueId(), originalSkinId);
            } else {
                playerStorage.setSkinIdOfPlayer(player.getUniqueId(), 
                    SkinIdentifier.ofPlayer(player.getUniqueId()));
            }

            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    skinsAPI.getSkinApplier(Player.class).applySkin(player);
                } catch (DataRequestException e) {
                    plugin.getLogger().warning(() -> "Failed to restore skin: " + e.getMessage());
                }
            });
        });
    }

    public void restoreAllSkins() {
        if (skinsAPI == null) return;

        for (UUID playerUUID : skinAssignments.keySet()) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                restoreOriginalSkin(player);
            }
        }

        Bukkit.getServer().broadcast(Component.text(Messages.SKIN_RESTORE_SUCCESS()));
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
