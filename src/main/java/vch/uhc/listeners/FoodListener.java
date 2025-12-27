package vch.uhc.listeners;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import vch.uhc.UHC;
import vch.uhc.misc.BaseItem;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.enums.GameState;

public class FoodListener extends BaseListener {

    @EventHandler
    public void onEating(PlayerItemConsumeEvent e) {

        if (UHC.getPlugin().getSettings().getGameState() != GameState.IN_PROGRESS) {
            return;
        }

        ArrayList<BaseItem> items = UHC.getPlugin().getSettings().getItems();
        for (BaseItem item : items) {
            if (item.onConsume(e)) {
                e.setCancelled(true);
                break;
            }
        }
        
    }

}
