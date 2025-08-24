package vch.uhc.items;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import vch.uhc.misc.BaseItem;


public class GlisteringMelonSlice extends BaseItem {

    public GlisteringMelonSlice() {
        super(new ItemStack(Material.GLISTERING_MELON_SLICE), new String[]{"GGG", "GMG", "GGG"}, Map.of(
                'G', Material.GOLD_INGOT,
                'M', Material.MELON_SLICE
        ), ChatColor.YELLOW + "Glistering Melon Slice");
    }

    @Override
    public void register() {
        super.register();
        NamespacedKey key = NamespacedKey.minecraft("glistering_melon_slice");
        Bukkit.removeRecipe(key);
    }

}