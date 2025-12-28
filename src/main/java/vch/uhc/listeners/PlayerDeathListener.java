package vch.uhc.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.Messages;
import vch.uhc.misc.enums.EliminationMode;
import vch.uhc.misc.enums.GameState;
import vch.uhc.models.UHCPlayer;

public class PlayerDeathListener extends BaseListener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        if (UHC.getPlugin().getSettings().getGameState() != GameState.IN_PROGRESS) {
            return;
        }

        Player victim = e.getEntity();
        Player killer = victim.getKiller();

        UHCPlayer uhcVictim = UHC.getPlugin().getPlayerManager().getPlayerByUUID(victim.getUniqueId());

        if (uhcVictim != null) {

            // Create double chest with player's items
            Location deathLoc = victim.getLocation();
            List<ItemStack> drops = e.getDrops();
            
            // Add player head if killed by another player
            if (killer != null && killer instanceof Player) {
                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
                if (skullMeta != null) {
                    skullMeta.setOwningPlayer(victim);
                    playerHead.setItemMeta(skullMeta);
                    drops.add(playerHead);
                }
            }
            
            if (!drops.isEmpty() && deathLoc.getWorld() != null) {
                Block block = deathLoc.getBlock();
                block.setType(Material.CHEST);
                
                // Create second chest adjacent to make it double
                Block adjacentBlock = null;
                Location[] adjacentLocations = {
                    deathLoc.clone().add(1, 0, 0),  // East
                    deathLoc.clone().add(-1, 0, 0), // West
                    deathLoc.clone().add(0, 0, 1),  // South
                    deathLoc.clone().add(0, 0, -1)  // North
                };
                
                for (Location loc : adjacentLocations) {
                    Block testBlock = loc.getBlock();
                    if (testBlock.getType() == Material.AIR || testBlock.getType().isAir()) {
                        adjacentBlock = testBlock;
                        break;
                    }
                }
                
                if (adjacentBlock != null) {
                    adjacentBlock.setType(Material.CHEST);
                }
                
                // Wait a tick for the double chest to form, then add items
                Bukkit.getScheduler().runTaskLater(UHC.getPlugin(), () -> {
                    if (block.getState() instanceof Chest chest) {
                        Inventory chestInv = chest.getInventory();
                        for (ItemStack item : drops) {
                            if (item != null && item.getType() != Material.AIR) {
                                chestInv.addItem(item);
                            }
                        }
                        chest.update();
                    }
                }, 1L);
                
                e.getDrops().clear();
            }

            uhcVictim.removeLife();
            int livesAfterDeath = uhcVictim.getLives();

            boolean wasEliminated = livesAfterDeath <= 0 || (killer != null && killer instanceof Player);

            if (wasEliminated) {

                uhcVictim.setLives(0);
                uhcVictim.setPlaying(false);
                
                // Handle elimination based on configured mode
                if (UHC.getPlugin().getSettings().getEliminationMode() == EliminationMode.KICK) {
                    Bukkit.getScheduler().runTaskLater(UHC.getPlugin(), () -> {
                        victim.sendMessage(Messages.ELIMINATED());
                        victim.kick(net.kyori.adventure.text.Component.text(Messages.ELIMINATED()));
                    }, 20L);
                } else {
                    // Default: SPECTATOR mode
                    Bukkit.getScheduler().runTaskLater(UHC.getPlugin(), () -> {
                        victim.setGameMode(GameMode.SPECTATOR);
                        victim.sendMessage(Messages.ELIMINATED());
                    }, 20L);
                }
                
            } else {

                Bukkit.getScheduler().runTaskLater(UHC.getPlugin(), () -> {
                    if (uhcVictim.getSpawn() != null) {
                        Location safeSpawn = UHC.getPlugin().getUHCManager().getSafeRespawnLocation(uhcVictim.getSpawn());

                        if (safeSpawn != null) {
                            if (!safeSpawn.equals(uhcVictim.getSpawn())) {
                                uhcVictim.setSpawn(safeSpawn);
                            }

                            victim.spigot().respawn();
                            victim.teleport(safeSpawn);
                            AttributeInstance maxHealthAttr = victim.getAttribute(Attribute.MAX_HEALTH);
                            if (maxHealthAttr != null) {
                                victim.setHealth(maxHealthAttr.getValue());
                            }
                            victim.sendMessage(Messages.RESPAWN_MESSAGE(uhcVictim.getLives()));
                        }
                    }
                }, 1L);
            }

            if (killer != null) {
                UHCPlayer uhcKiller = UHC.getPlugin().getPlayerManager().getPlayerByUUID(killer.getUniqueId());
                UHC.getPlugin().getStatsManager().recordKill(uhcKiller, uhcVictim);
            } else {
                UHC.getPlugin().getStatsManager().recordKill(null, uhcVictim);
            }
        }
    }

}
