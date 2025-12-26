package vch.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.Settings;

public class PlayerDeathListener extends BaseListener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        if (UHC.getPlugin().getSettings().getGameStatus() != Settings.GameStatus.IN_PROGRESS) {
            return;
        }

        Player victim = e.getEntity();
        Player killer = victim.getKiller();

        vch.uhc.models.Player uhcVictim = UHC.getPlugin().getPlayerManager().getPlayerByUUID(victim.getUniqueId());
        
        if (uhcVictim != null) {
            uhcVictim.removeLife();
            
            if (!uhcVictim.isAlive()) {
                victim.setGameMode(org.bukkit.GameMode.SPECTATOR);
            } else {
                
                org.bukkit.Bukkit.getScheduler().runTaskLater(UHC.getPlugin(), () -> {
                    if (uhcVictim.getSpawn() != null) {
                        org.bukkit.Location safeSpawn = UHC.getPlugin().getUHCManager().getSafeRespawnLocation(uhcVictim.getSpawn());
                        
                        if (safeSpawn != null) {
                            if (!safeSpawn.equals(uhcVictim.getSpawn())) {
                                uhcVictim.setSpawn(safeSpawn);
                            }
                            
                            victim.spigot().respawn();
                            victim.teleport(safeSpawn);
                            victim.setHealth(victim.getMaxHealth());
                            victim.setFoodLevel(20);
                            victim.setSaturation(20);
                            victim.sendMessage("§e¡Has reaparecido! Vidas restantes: §c" + uhcVictim.getLives());
                        }
                    }
                }, 1L);
            }
            
            if (killer != null) {
                vch.uhc.models.Player uhcKiller = UHC.getPlugin().getPlayerManager().getPlayerByUUID(killer.getUniqueId());
                UHC.getPlugin().getStatsManager().recordKill(uhcKiller, uhcVictim);
            } else {
                UHC.getPlugin().getStatsManager().recordKill(null, uhcVictim);
            }
        }
    }

}
