package vch.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.Messages;
import vch.uhc.misc.enums.GameState;

public class PvPListener extends BaseListener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        if (UHC.getPlugin().getSettings().getGameState() != GameState.IN_PROGRESS) {
            return;
        }


        if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) {
            return;
        }


        if (UHC.getPlugin().getUHCManager().isAgreementActive()) {
            e.setCancelled(true);
            
            Player damager = (Player) e.getDamager();
            damager.sendMessage(Messages.PVP_DISABLED_AGREEMENT());
            
            int remainingSeconds = UHC.getPlugin().getUHCManager().getAgreementRemainingSeconds();
            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;
            
            damager.sendMessage(Messages.PVP_TIME_REMAINING(minutes, seconds));
        }
    }

}
