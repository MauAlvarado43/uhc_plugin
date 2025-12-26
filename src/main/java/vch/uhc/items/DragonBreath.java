package vch.uhc.items;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import vch.uhc.misc.BaseItem;

public class DragonBreath extends BaseItem {
    public DragonBreath() {
        super(new ItemStack(Material.DRAGON_BREATH), new String[]{"PEP", " B "}, Map.of(
                'P', Material.BLAZE_POWDER,
                'E', Material.ENDER_PEARL,
                'B', Material.GLASS_BOTTLE
        ), vch.uhc.misc.Messages.ITEM_DRAGON_BREATH());
    }
}