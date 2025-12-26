package vch.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.Settings;

public class PlayerDamageListener extends BaseListener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (UHC.getPlugin().getSettings().getGameStatus() != Settings.GameStatus.IN_PROGRESS) {
            return;
        }

        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player bukkitPlayer = (Player) e.getEntity();
        vch.uhc.models.Player uhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(bukkitPlayer.getUniqueId());

        if (uhcPlayer != null) {
            UHC.getPlugin().getStatsManager().markAsIronman(uhcPlayer);
            bukkitPlayer.setSaturation(20.0f);
        }
    }
}
