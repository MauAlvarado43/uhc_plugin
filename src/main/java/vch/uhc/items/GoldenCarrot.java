package vch.uhc.items;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import vch.uhc.misc.BaseItem;

public class GoldenCarrot extends BaseItem {

  public GoldenCarrot() {
        super(new ItemStack(Material.GOLDEN_CARROT), new String[]{"GGG", "GCG", "GGG"}, Map.of(
                'G', Material.GOLD_INGOT,
                'C', Material.CARROT
        ), vch.uhc.misc.Messages.ITEM_GOLDEN_CARROT());
    }

    @Override
    public void register() {
        super.register();
        NamespacedKey key = NamespacedKey.minecraft("golden_carrot");
        Bukkit.removeRecipe(key);
    }

  
}