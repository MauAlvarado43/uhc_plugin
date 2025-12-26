package vch.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;

public class AFKListener extends BaseListener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        
        if (UHC.getPlugin().getAFKManager().isAFK(player.getUniqueId())) {
            if (e.getFrom().getX() != e.getTo().getX() ||
                e.getFrom().getY() != e.getTo().getY() ||
                e.getFrom().getZ() != e.getTo().getZ()) {
                e.setCancelled(true);
            }
        }
    }
}
