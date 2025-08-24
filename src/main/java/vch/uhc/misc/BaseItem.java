package vch.uhc.misc;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import vch.uhc.UHC;

public class BaseItem {

  private ItemStack itemStack;
  private ShapedRecipe recipe;
  private boolean enabled = true;
  final private NamespacedKey key;
  final private String name;

  public BaseItem(ItemStack itemStack, String[] shape, Map<Character, Material> ingredients, String name) {

    this.itemStack = itemStack;
    this.key = new NamespacedKey(UHC.getPlugin(), this.getClass().getSimpleName());
    this.name = name;

    createItemMeta(name);
    createRecipe(shape, ingredients);

  }

  private void createItemMeta(String customName) {

    ItemMeta itemMeta = itemStack.getItemMeta();
    assert itemMeta != null;
    
    itemMeta.setDisplayName(customName);
    itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, this.getClass().getSimpleName());
    itemStack.setItemMeta(itemMeta);

  }

  private void createRecipe(String[] shape, Map<Character, Material> ingredients) {

    recipe = new ShapedRecipe(key, itemStack);
    recipe.shape(shape);

    for(Map.Entry<Character, Material> entry : ingredients.entrySet())
      recipe.setIngredient(entry.getKey(), entry.getValue());

  }

  public ItemStack getItemStack() {
    return itemStack;
  }

  public ShapedRecipe getRecipe() {
    return recipe;
  }

  public NamespacedKey getKey() {
    return key;
  }

  public void setItemStack(ItemStack itemStack) {
    this.itemStack = itemStack;
  }

  public void setRecipe(ShapedRecipe recipe) {
    this.recipe = recipe;
  }

  public void enable() {
    enabled = true;
  }

  public void disable() {
    enabled = false;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public String getName() {
    return name;
  }

  public void register() {
    Bukkit.addRecipe(recipe);
  }

  public void unregister() {
    Bukkit.removeRecipe(key);
  }

  public boolean onConsume(PlayerItemConsumeEvent e) {
    return false;
  }
  
}