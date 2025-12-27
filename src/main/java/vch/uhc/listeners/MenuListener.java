package vch.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;

public class MenuListener extends BaseListener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        String title = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(e.getView().title());
        
        if (title.startsWith("§6UHC") || title.contains("UHC")) {
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
