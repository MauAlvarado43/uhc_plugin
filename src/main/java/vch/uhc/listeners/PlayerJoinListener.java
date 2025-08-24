package vch.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;

public class PlayerJoinListener extends BaseListener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        
        Player p = e.getPlayer();
        if(UHC.getPlugin().getPlayerManager().getPlayerByUUID(p.getUniqueId()) != null)
            return;

        vch.uhc.models.Player uhcPlayer = new vch.uhc.models.Player(p.getUniqueId(), p.getName());
        UHC.getPlugin().getPlayerManager().addPlayer(uhcPlayer);
        
    }

}