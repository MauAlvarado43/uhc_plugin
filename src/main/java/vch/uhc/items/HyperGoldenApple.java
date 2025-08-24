package vch.uhc.items;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import vch.uhc.misc.BaseItem;

public class HyperGoldenApple extends BaseItem {

    public HyperGoldenApple() {
        super(new ItemStack(Material.GOLDEN_APPLE), new String[]{"GGG", "GAG", "GGG"}, Map.of(
                'G', Material.GOLD_INGOT,
                'A', Material.GOLDEN_APPLE
        ), ChatColor.RED + "Hyper Golden Apple");
    }

    @Override
    public boolean onConsume(PlayerItemConsumeEvent e) {

        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        if(item.getItemMeta() == null || !item.getItemMeta().getPersistentDataContainer().has(getKey(), PersistentDataType.STRING))
            return false;

        if (!isEnabled())
            return false;

        AttributeInstance attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double currentMaxHealth = attribute.getBaseValue();

        if (!(currentMaxHealth + 4.0D > 60.0D))
            attribute.setBaseValue(currentMaxHealth + 4.0D);

        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 130, 1));
        player.getInventory().getItemInMainHand().setAmount(item.getAmount() - 1);
        
        return true;

    }

}