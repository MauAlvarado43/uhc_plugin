package vch.uhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.Messages;
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
            uhcVictim.removeLife();

            if (!uhcVictim.isAlive()) {
                victim.setGameMode(GameMode.SPECTATOR);
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
                            victim.setFoodLevel(20);
                            victim.setSaturation(20);
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
