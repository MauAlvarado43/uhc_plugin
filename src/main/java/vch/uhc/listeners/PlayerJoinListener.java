package vch.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import vch.uhc.misc.BaseListener;
import vch.uhc.UHC;
import vch.uhc.models.UHCPlayer;

/**
 * Handles player join events.
 * Creates UHC player instances for new players.
 */
public class PlayerJoinListener extends BaseListener {

    @org.bukkit.event.EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        
        Player p = e.getPlayer();
        if(UHC.getPlugin().getPlayerManager().getPlayerByUUID(p.getUniqueId()) != null)
            return;

        UHCPlayer uhcPlayer = new UHCPlayer(p.getUniqueId(), p.getName());
        UHC.getPlugin().getPlayerManager().addPlayer(uhcPlayer);
        
    }

}