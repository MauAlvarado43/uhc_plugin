package vch.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.Messages;

public class MenuListener extends BaseListener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        String title = e.getView().getTitle();
        
        if (title.startsWith(Messages.MENU_MAIN_TITLE().substring(0, 6))) {
            e.setCancelled(true);
            
            if (!(e.getWhoClicked() instanceof Player)) {
                return;
            }

            Player player = (Player) e.getWhoClicked();
            int slot = e.getSlot();
            boolean leftClick = e.isLeftClick();

            UHC.getPlugin().getMenuManager().handleMenuClick(player, slot, leftClick, title);
        }
    }
}
