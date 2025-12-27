package vch.uhc.items;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import vch.uhc.misc.BaseItem;

public class SuperGoldenApple extends BaseItem {

    public SuperGoldenApple() {
        super(new ItemStack(Material.GOLDEN_APPLE), new String[]{"GGG", "GAG", "GGG"}, Map.of(
                'G', Material.GOLD_INGOT,
                'A', Material.GOLDEN_APPLE
        ), vch.uhc.misc.Messages.ITEM_SUPER_GOLDEN_APPLE());
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
