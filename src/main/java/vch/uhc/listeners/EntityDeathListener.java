package vch.uhc.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.Settings;

public class EntityDeathListener extends BaseListener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {

        if (UHC.getPlugin().getSettings().getGameStatus() != Settings.GameStatus.IN_PROGRESS) {
            return;
        }

        if (e.getEntityType() == EntityType.GHAST) {
            ItemStack gold_ingot = new ItemStack(Material.GOLD_INGOT);
            e.getDrops().clear();
            e.getDrops().add(gold_ingot);
        }
    }

}
