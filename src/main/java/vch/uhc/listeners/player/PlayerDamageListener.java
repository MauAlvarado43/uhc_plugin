package vch.uhc.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.enums.GameState;

public class PlayerDamageListener extends BaseListener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onPlayerDamage(EntityDamageEvent e) {
        if (UHC.getPlugin().getSettings().getGameState() != GameState.IN_PROGRESS) {
            return;
        }

        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player bukkitPlayer = (Player) e.getEntity();
        vch.uhc.models.UHCPlayer uhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(bukkitPlayer.getUniqueId());

        if (uhcPlayer != null && uhcPlayer.isPlaying()) {
            UHC.getPlugin().getStatsManager().markAsIronman(uhcPlayer);
        }
    }
}
