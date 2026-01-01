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

        Player victim = (Player) e.getEntity();
        Player attacker = (Player) e.getDamager();

        if (UHC.getPlugin().getUHCManager().isAgreementActive()) {
            e.setCancelled(true);
            
            attacker.sendMessage(Messages.PVP_DISABLED_AGREEMENT());
            
            int remainingSeconds = UHC.getPlugin().getUHCManager().getAgreementRemainingSeconds();
            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;
            
            attacker.sendMessage(Messages.PVP_TIME_REMAINING(minutes, seconds));
        } else {
            UHC.getPlugin().getCombatTracker().tagPlayer(victim.getUniqueId(), attacker.getUniqueId());
        }
    }

}