package vch.uhc.listeners.game;

import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import vch.uhc.UHC;
import vch.uhc.misc.BaseListener;
import vch.uhc.misc.enums.GameState;

public class EntityDeathListener extends BaseListener {

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {

        if (UHC.getPlugin().getSettings().getGameState() != GameState.IN_PROGRESS) {
            return;
        }

        if (e.getEntityType() == EntityType.GHAST) {
            ItemStack gold_ingot = new ItemStack(Material.GOLD_INGOT);
            e.getDrops().clear();
            e.getDrops().add(gold_ingot);
        }
        
        if (e.getEntityType() == EntityType.ENDER_DRAGON) {
            EnderDragon dragon = (EnderDragon) e.getEntity();
            Player killer = dragon.getKiller();
            
            if (killer != null) {
                UHC.getPlugin().getGameModeManager().onDragonKilled(dragon, killer);
            }
        }
    }

}
