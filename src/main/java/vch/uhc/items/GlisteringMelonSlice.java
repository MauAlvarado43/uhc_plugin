package vch.uhc.items;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import vch.uhc.misc.BaseItem;


public class GlisteringMelonSlice extends BaseItem {

    public GlisteringMelonSlice() {
        super(new ItemStack(Material.GLISTERING_MELON_SLICE), new String[]{"GGG", "GMG", "GGG"}, Map.of(
                'G', Material.GOLD_INGOT,
                'M', Material.MELON_SLICE
        ), vch.uhc.misc.Messages.ITEM_GLISTERING_MELON_SLICE());
    }

    @Override
    public void register() {
        super.register();
        NamespacedKey key = NamespacedKey.minecraft("glistering_melon_slice");
        Bukkit.removeRecipe(key);
    }

}