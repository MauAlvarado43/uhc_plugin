package vch.uhc.listeners.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import vch.uhc.UHC;
import vch.uhc.managers.player.SkinManager;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.enums.GameState;

public class SkinRevealListener extends BaseListener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerHitPlayer(EntityDamageByEntityEvent event) {
        if (UHC.getPlugin().getSettings().getGameState() != GameState.IN_PROGRESS) {
            return;
        }

        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }

        Player attacked = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        SkinManager skinManager = UHC.getPlugin().getSkinManager();
        if (skinManager == null) {
            return;
        }

        if (!skinManager.areSkinsShuffled()) {
            return;
        }

        skinManager.revealSkin(attacked.getUniqueId(), attacker.getUniqueId());
    }
}
