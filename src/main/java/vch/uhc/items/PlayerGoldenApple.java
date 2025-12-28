package vch.uhc.items;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import vch.uhc.misc.BaseItem;

public class PlayerGoldenApple extends BaseItem {

    public PlayerGoldenApple() {
        super(new ItemStack(Material.PLAYER_HEAD), new String[]{"GGG", "DHD", "GGG"}, Map.of(
                'G', Material.GOLD_INGOT,
                'D', Material.DIAMOND,
                'H', Material.PLAYER_HEAD
        ), vch.uhc.misc.Messages.ITEM_HEAD_APPLE());
        
        // Modify recipe to accept both PLAYER_HEAD and WITHER_SKELETON_SKULL
        getRecipe().setIngredient('H', new RecipeChoice.MaterialChoice(
            Material.PLAYER_HEAD, 
            Material.WITHER_SKELETON_SKULL
        ));
    }

    @Override
    public boolean onConsume(PlayerItemConsumeEvent e) {

        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        if(item.getItemMeta() == null || !item.getItemMeta().getPersistentDataContainer().has(getKey(), PersistentDataType.STRING))
            return false;

        if (!isEnabled())
            return false;

        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 240, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,100, 4));
        player.getInventory().getItemInMainHand().setAmount(item.getAmount() - 1);
        
        return true;

    }

}