package vch.uhc.items;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import vch.uhc.UHC;
import vch.uhc.misc.BaseItem;

public class HyperGoldenApple extends BaseItem {

    public HyperGoldenApple() {
        super(new ItemStack(Material.GOLDEN_APPLE), new String[]{"GGG", "GAG", "GGG"}, Map.of(
                'G', Material.GOLD_INGOT,
                'A', Material.GOLDEN_APPLE
        ), vch.uhc.misc.Messages.ITEM_HYPER_GOLDEN_APPLE());
    }

    @Override
    public void register() {
        BaseItem superGoldenApple = UHC.getPlugin().getSettings().getItems().stream()
                .filter(item -> item instanceof SuperGoldenApple)
                .findFirst().orElse(null);

        if (superGoldenApple != null) {
            ShapedRecipe customRecipe = new ShapedRecipe(getKey(), getItemStack());
            customRecipe.shape("GGG", "GAG", "GGG");
            customRecipe.setIngredient('G', Material.GOLD_INGOT);
            customRecipe.setIngredient('A', new RecipeChoice.ExactChoice(superGoldenApple.getItemStack()));
            setRecipe(customRecipe);
        }
        
        super.register();
    }

    @Override
    public boolean onConsume(PlayerItemConsumeEvent e) {

        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        if(item.getItemMeta() == null || !item.getItemMeta().getPersistentDataContainer().has(getKey(), PersistentDataType.STRING))
            return false;

        if (!isEnabled())
            return false;

        AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) {
            double currentMaxHealth = attribute.getBaseValue();
            double maxHealthLimit = UHC.getPlugin().getSettings().getMaxHealth();
            
            double extraBuffHealth = 0;
            if (UHC.getPlugin().getSettings().isBuffsEnabled()) {
                extraBuffHealth = UHC.getPlugin().getSettings().getExtraHearts() * 2.0;
            }
            double totalLimit = maxHealthLimit + extraBuffHealth;
            
            if (currentMaxHealth + 4.0 > totalLimit) {
                player.sendMessage(vch.uhc.misc.Messages.ITEM_MAX_HEALTH_REACHED());
                return true;
            }
            
            attribute.setBaseValue(currentMaxHealth + 4.0);
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 130, 1));
        player.getInventory().getItemInMainHand().setAmount(item.getAmount() - 1);
        
        return true;

    }

}