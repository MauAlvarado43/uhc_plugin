package vch.uhc.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import vch.uhc.UHC;
import vch.uhc.misc.Settings;

public class AdvancementListener implements Listener {

    @EventHandler
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {

        if (UHC.getPlugin().getSettings().getGameStatus() != Settings.GameStatus.IN_PROGRESS) {
            return;
        }

        event.getPlayer().getWorld().getPlayers().forEach(p -> {
            if (p != event.getPlayer()) {
            }
        });

    }

}
