package vch.uhc.listeners;

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

        
    }

}
