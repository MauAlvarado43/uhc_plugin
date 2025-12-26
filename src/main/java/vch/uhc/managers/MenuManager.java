package vch.uhc.managers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import vch.uhc.UHC;
import vch.uhc.misc.BaseItem;
import vch.uhc.misc.Settings;

public class MenuManager {

    private String getMainMenuTitle() {
        return vch.uhc.misc.Messages.MENU_MAIN_TITLE();
    }
    
    private String getTimeMenuTitle() {
        return vch.uhc.misc.Messages.MENU_TIME_TITLE();
    }
    
    private String getRecipesMenuTitle() {
        return vch.uhc.misc.Messages.MENU_RECIPES_TITLE();
    }

    private static final int MENU_SIZE = 54;
    private final Map<UUID, String> playerMenuContext = new HashMap<>();

    public void openMainMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, MENU_SIZE, getMainMenuTitle());
        
        Settings settings = UHC.getPlugin().getSettings();

        menu.setItem(0, createMenuItem(
            Material.NETHERITE_SWORD,
            vch.uhc.misc.Messages.MENU_GAME_MODE(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_GAME_MODE_CURRENT(settings.getGameMode().name()),
                "",
                vch.uhc.misc.Messages.MENU_GAME_MODE_CLICK(),
                vch.uhc.misc.Messages.MENU_GAME_MODE_PVD(),
                vch.uhc.misc.Messages.MENU_GAME_MODE_PVP(),
                vch.uhc.misc.Messages.MENU_GAME_MODE_RESOURCE_RUSH()
            )
        ));

        menu.setItem(1, createMenuItem(
            Material.WHITE_BANNER,
            vch.uhc.misc.Messages.MENU_TEAM_MODE(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_TEAM_MODE_CURRENT(settings.getTeamMode().name()),
                "",
                vch.uhc.misc.Messages.MENU_TEAM_MODE_CLICK(),
                vch.uhc.misc.Messages.MENU_TEAM_MODE_AUTO(),
                vch.uhc.misc.Messages.MENU_TEAM_MODE_MANUAL(),
                vch.uhc.misc.Messages.MENU_TEAM_MODE_IN_GAME()
            )
        ));

        menu.setItem(2, createMenuItem(
            Material.IRON_HELMET,
            vch.uhc.misc.Messages.MENU_TEAM_SIZE(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_TEAM_SIZE_CURRENT(settings.getTeamSize()),
                "",
                vch.uhc.misc.Messages.MENU_TEAM_SIZE_LEFT(),
                vch.uhc.misc.Messages.MENU_TEAM_SIZE_RIGHT()
            )
        ));

        menu.setItem(3, createMenuItem(
            Material.TOTEM_OF_UNDYING,
            vch.uhc.misc.Messages.MENU_PLAYER_LIVES(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_PLAYER_LIVES_CURRENT(settings.getPlayerLives()),
                "",
                vch.uhc.misc.Messages.MENU_PLAYER_LIVES_LEFT(),
                vch.uhc.misc.Messages.MENU_PLAYER_LIVES_RIGHT()
            )
        ));

        menu.setItem(4, createMenuItem(
            Material.ARMOR_STAND,
            "§dMezcla de Skins",
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_STATUS(settings.isSkinShuffleEnabled() ? 
                    vch.uhc.misc.Messages.MENU_ENABLED() : vch.uhc.misc.Messages.MENU_DISABLED()),
                "§7Intervalo: §f" + settings.getSkinShuffleMinutes() + "m " + 
                    settings.getSkinShuffleSeconds() + "s",
                "",
                vch.uhc.misc.Messages.MENU_CLICK_TO_CONFIGURE()
            )
        ));

        menu.setItem(9, createMenuItem(
            Material.GRASS_BLOCK,
            vch.uhc.misc.Messages.MENU_MAX_WORLD_SIZE(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_MAX_WORLD_SIZE_CURRENT(settings.getMaxWorldSize()),
                "",
                vch.uhc.misc.Messages.MENU_MAX_WORLD_SIZE_LEFT(),
                vch.uhc.misc.Messages.MENU_MAX_WORLD_SIZE_RIGHT()
            )
        ));

        menu.setItem(10, createMenuItem(
            Material.BEDROCK,
            vch.uhc.misc.Messages.MENU_MIN_WORLD_SIZE(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_MIN_WORLD_SIZE_CURRENT(settings.getMinWorldSize()),
                "",
                vch.uhc.misc.Messages.MENU_MIN_WORLD_SIZE_LEFT(),
                vch.uhc.misc.Messages.MENU_MIN_WORLD_SIZE_RIGHT()
            )
        ));

        menu.setItem(11, createMenuItem(
            Material.RED_STAINED_GLASS,
            "§6Borde Gradual",
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_STATUS(settings.isGradualBorderEnabled() ? 
                    vch.uhc.misc.Messages.MENU_ENABLED() : vch.uhc.misc.Messages.MENU_DISABLED()),
                "",
                "§7El borde se reduce:",
                settings.isGradualBorderEnabled() ? 
                    "§e▶ Gradualmente" : "§e▶ Instantáneamente",
                "",
                vch.uhc.misc.Messages.MENU_CLICK_TO_TOGGLE(settings.isGradualBorderEnabled() ? 
                    vch.uhc.misc.Messages.MENU_DISABLE() : vch.uhc.misc.Messages.MENU_ENABLE())
            )
        ));

        menu.setItem(18, createMenuItem(
            Material.CLOCK,
            vch.uhc.misc.Messages.MENU_GAME_TIME(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_GAME_TIME_CURRENT(
                    settings.getGameHours(),
                    settings.getGameMinutes(),
                    settings.getGameSeconds()),
                "",
                vch.uhc.misc.Messages.MENU_GAME_TIME_CLICK()
            )
        ));

        menu.setItem(19, createMenuItem(
            Material.PAPER,
            vch.uhc.misc.Messages.MENU_AGREEMENT_TIME(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_AGREEMENT_TIME_CURRENT(
                    settings.getAgreementHours(),
                    settings.getAgreementMinutes(),
                    settings.getAgreementSeconds()),
                "",
                vch.uhc.misc.Messages.MENU_AGREEMENT_TIME_CLICK()
            )
        ));

        menu.setItem(20, createMenuItem(
            Material.LAVA_BUCKET,
            vch.uhc.misc.Messages.MENU_MIN_BORDER_TIME(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_MIN_BORDER_TIME_CURRENT(
                    settings.getMinWorldBorderHours(),
                    settings.getMinWorldBorderMinutes(),
                    settings.getMinWorldBorderSeconds()),
                "",
                vch.uhc.misc.Messages.MENU_MIN_BORDER_TIME_CLICK()
            )
        ));

        menu.setItem(21, createMenuItem(
            Material.NAME_TAG,
            vch.uhc.misc.Messages.MENU_MAX_TEAM_TIME(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_MAX_TEAM_TIME_CURRENT(
                    settings.getMaxTeamInGameHours(),
                    settings.getMaxTeamInGameMinutes(), 
                    settings.getMaxTeamInGameSeconds()),
                "",
                vch.uhc.misc.Messages.MENU_CLICK_TO_CONFIGURE()
            )
        ));

        menu.setItem(22, createMenuItem(
            Material.END_PORTAL_FRAME,
            vch.uhc.misc.Messages.MENU_END_PORTAL_TIME(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_CURRENT_SIMPLE() + 
                    settings.getEndPortalHours() + "h " + 
                    settings.getEndPortalMinutes() + "m " + 
                    settings.getEndPortalSeconds() + "s",
                "",
                vch.uhc.misc.Messages.MENU_CLICK_TO_CONFIGURE()
            )
        ));

        menu.setItem(23, createMenuItem(
            Material.SHULKER_BOX,
            vch.uhc.misc.Messages.MENU_SHULKER_SPAWN(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_STATUS(settings.isShulkerEnabled() ? 
                    vch.uhc.misc.Messages.MENU_ENABLED() : vch.uhc.misc.Messages.MENU_DISABLED()),
                vch.uhc.misc.Messages.MENU_TIME_SIMPLE() + 
                    settings.getShulkerHours() + "h " + 
                    settings.getShulkerMinutes() + "m " + 
                    settings.getShulkerSeconds() + "s",
                "",
                vch.uhc.misc.Messages.MENU_CLICK_TO_CONFIGURE()
            )
        ));

        menu.setItem(24, createMenuItem(
            Material.RECOVERY_COMPASS,
            vch.uhc.misc.Messages.MENU_LOCATOR_BAR_TIME(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_LOCATOR_BAR_DESC(),
                vch.uhc.misc.Messages.MENU_CURRENT_SIMPLE() + 
                    settings.getLocatorBarHours() + "h " + 
                    settings.getLocatorBarMinutes() + "m " + 
                    settings.getLocatorBarSeconds() + "s",
                vch.uhc.misc.Messages.MENU_STATUS(settings.isLocatorBarEnabled() ? 
                    vch.uhc.misc.Messages.MENU_ENABLED() : vch.uhc.misc.Messages.MENU_DISABLED()),
                "",
                vch.uhc.misc.Messages.MENU_CLICK_TO_CONFIGURE()
            )
        ));

        menu.setItem(27, createMenuItem(
            Material.ENCHANTED_GOLDEN_APPLE,
            "§dMejoras de Jugadores",
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_STATUS(settings.isBuffsEnabled() ? 
                    vch.uhc.misc.Messages.MENU_ENABLED() : vch.uhc.misc.Messages.MENU_DISABLED()),
                "§7Tiempo: §f" + 
                    settings.getBuffsHours() + "h " + 
                    settings.getBuffsMinutes() + "m " + 
                    settings.getBuffsSeconds() + "s",
                "§7Corazones extra: §c+" + (int)settings.getExtraHearts(),
                "§7Resistencia: §bII (permanente)",
                "",
                vch.uhc.misc.Messages.MENU_CLICK_TO_CONFIGURE()
            )
        ));

        menu.setItem(28, createMenuItem(
            Material.CRAFTING_TABLE,
            vch.uhc.misc.Messages.MENU_CUSTOM_RECIPES(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_CLICK_TO_VIEW(),
                vch.uhc.misc.Messages.MENU_CUSTOM_RECIPES_DESC()
            )
        ));

        menu.setItem(29, createMenuItem(
            Material.WRITABLE_BOOK,
            vch.uhc.misc.Messages.MENU_VIEW_STATS(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_VIEW_STATS_DESC(),
                vch.uhc.misc.Messages.MENU_CURRENT_GAME_STATS()
            )
        ));

        menu.setItem(36, createMenuItem(
            Material.LIME_WOOL,
            "§a§lIniciar UHC",
            Arrays.asList(
                "§7Iniciar el juego UHC",
                "§7con la configuración actual"
            )
        ));

        menu.setItem(37, createMenuItem(
            Material.YELLOW_WOOL,
            "§e§lPausar UHC",
            Arrays.asList(
                "§7Pausar el juego en progreso"
            )
        ));

        menu.setItem(38, createMenuItem(
            Material.RED_WOOL,
            "§c§lCancelar UHC",
            Arrays.asList(
                "§7Detener el juego completamente"
            )
        ));

        menu.setItem(45, createMenuItem(
            Material.EMERALD,
            vch.uhc.misc.Messages.MENU_SAVE_CONFIG(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_SAVE_ALL_CHANGES()
            )
        ));

        menu.setItem(46, createMenuItem(
            Material.KNOWLEDGE_BOOK,
            "§aCargar Configuración",
            Arrays.asList(
                "§7Cargar la configuración guardada",
                "§7desde el archivo settings.json"
            )
        ));

        menu.setItem(47, createMenuItem(
            Material.BARRIER,
            vch.uhc.misc.Messages.MENU_CLOSE(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_CLOSE_MENU()
            )
        ));

        playerMenuContext.remove(player.getUniqueId());
        player.openInventory(menu);
    }

    public void openTimeMenu(Player player, String timeType) {
        Inventory menu = Bukkit.createInventory(null, 27, getTimeMenuTitle() + timeType);
        
        Settings settings = UHC.getPlugin().getSettings();
        int hours = 0, minutes = 0, seconds = 0;
        boolean hasEnabled = false;
        boolean enabled = false;

        switch (timeType) {
            case "Juego":
                hours = settings.getGameHours();
                minutes = settings.getGameMinutes();
                seconds = settings.getGameSeconds();
                break;
            case "Acuerdo":
                hours = settings.getAgreementHours();
                minutes = settings.getAgreementMinutes();
                seconds = settings.getAgreementSeconds();
                break;
            case "Min Border":
                hours = settings.getMinWorldBorderHours();
                minutes = settings.getMinWorldBorderMinutes();
                seconds = settings.getMinWorldBorderSeconds();
                break;
            case "Max Equipos":
                hours = settings.getMaxTeamInGameHours();
                minutes = settings.getMaxTeamInGameMinutes();
                seconds = settings.getMaxTeamInGameSeconds();
                break;
            case "Portal End":
                hours = settings.getEndPortalHours();
                minutes = settings.getEndPortalMinutes();
                seconds = settings.getEndPortalSeconds();
                break;
            case "Locator Bar":
                hours = settings.getLocatorBarHours();
                minutes = settings.getLocatorBarMinutes();
                seconds = settings.getLocatorBarSeconds();
                hasEnabled = true;
                enabled = settings.isLocatorBarEnabled();
                break;
            case "Buffs":
                hours = settings.getBuffsHours();
                minutes = settings.getBuffsMinutes();
                seconds = settings.getBuffsSeconds();
                hasEnabled = true;
                enabled = settings.isBuffsEnabled();
                break;
            case "Shulker":
                hours = settings.getShulkerHours();
                minutes = settings.getShulkerMinutes();
                seconds = settings.getShulkerSeconds();
                hasEnabled = true;
                enabled = settings.isShulkerEnabled();
                break;
            case "Skin Shuffle":
                hours = 0;
                minutes = settings.getSkinShuffleMinutes();
                seconds = settings.getSkinShuffleSeconds();
                hasEnabled = true;
                enabled = settings.isSkinShuffleEnabled();
                break;
        }

        menu.setItem(11, createMenuItem(
            Material.REDSTONE,
            ChatColor.YELLOW + "Horas: " + ChatColor.WHITE + hours,
            Arrays.asList(
                ChatColor.WHITE + "Click Izquierdo: +1",
                ChatColor.WHITE + "Click Derecho: -1"
            )
        ));

        menu.setItem(13, createMenuItem(
            Material.GLOWSTONE_DUST,
            ChatColor.YELLOW + "Minutos: " + ChatColor.WHITE + minutes,
            Arrays.asList(
                ChatColor.WHITE + "Click Izquierdo: +1",
                ChatColor.WHITE + "Click Derecho: -1"
            )
        ));

        menu.setItem(15, createMenuItem(
            Material.GUNPOWDER,
            ChatColor.YELLOW + "Segundos: " + ChatColor.WHITE + seconds,
            Arrays.asList(
                ChatColor.WHITE + "Click Izquierdo: +1",
                ChatColor.WHITE + "Click Derecho: -1"
            )
        ));

        if (hasEnabled) {
            menu.setItem(22, createMenuItem(
                enabled ? Material.LIME_DYE : Material.GRAY_DYE,
                (enabled ? vch.uhc.misc.Messages.MENU_ENABLED() : vch.uhc.misc.Messages.MENU_DISABLED()),
                Arrays.asList(
                    vch.uhc.misc.Messages.MENU_CLICK_TO_TOGGLE(enabled ? 
                    vch.uhc.misc.Messages.MENU_DISABLE() : vch.uhc.misc.Messages.MENU_ENABLE())
                )
            ));
        }

        menu.setItem(18, createMenuItem(
            Material.ARROW,
            vch.uhc.misc.Messages.MENU_BACK(),
            Arrays.asList(
                vch.uhc.misc.Messages.MENU_BACK_TO_MAIN()
            )
        ));

        playerMenuContext.put(player.getUniqueId(), "TIME:" + timeType);
        player.openInventory(menu);
    }

    public void openRecipesMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, getRecipesMenuTitle());
        
        Settings settings = UHC.getPlugin().getSettings();
        List<BaseItem> items = settings.getItems();

        int slot = 10;
        for (BaseItem item : items) {
            menu.setItem(slot, createMenuItem(
                item.getItemStack().getType(),
                ChatColor.AQUA + item.getName(),
                Arrays.asList(
                    ChatColor.GRAY + "Estado: " + (item.isEnabled() ? 
                        ChatColor.GREEN + "Habilitado" : ChatColor.RED + "Deshabilitado"),
                    "",
                    ChatColor.WHITE + "Click para " + (item.isEnabled() ? "deshabilitar" : "habilitar")
                )
            ));
            slot++;
            if (slot % 9 == 8) slot += 2;
        }

        menu.setItem(49, createMenuItem(
            Material.ARROW,
            ChatColor.YELLOW + "Volver",
            Arrays.asList(
                ChatColor.GRAY + "Volver al menú principal"
            )
        ));

        playerMenuContext.put(player.getUniqueId(), "RECIPES");
        player.openInventory(menu);
    }

    private ItemStack createMenuItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        
        return item;
    }

    public void handleMenuClick(Player player, int slot, boolean leftClick, String menuTitle) {
        Settings settings = UHC.getPlugin().getSettings();
        String context = playerMenuContext.get(player.getUniqueId());

        if (menuTitle.equals(getRecipesMenuTitle())) {
            if (slot == 49) {
                openMainMenu(player);
                return;
            }

            List<BaseItem> items = settings.getItems();
            int itemIndex = -1;
            int checkSlot = 10;
            for (int i = 0; i < items.size(); i++) {
                if (checkSlot == slot) {
                    itemIndex = i;
                    break;
                }
                checkSlot++;
                if (checkSlot % 9 == 8) checkSlot += 2;
            }

            if (itemIndex >= 0 && itemIndex < items.size()) {
                BaseItem item = items.get(itemIndex);
                if (item.isEnabled()) {
                    item.disable();
                } else {
                    item.enable();
                }
                openRecipesMenu(player);
            }
            return;
        }

        if (context != null && context.startsWith("TIME:")) {
            String timeType = context.substring(5);
            handleTimeMenuClick(player, slot, leftClick, timeType);
            return;
        }

        if (menuTitle.equals(getMainMenuTitle())) {
            handleMainMenuClick(player, slot, leftClick, settings);
        }
    }

    private void handleMainMenuClick(Player player, int slot, boolean leftClick, Settings settings) {
        switch (slot) {
            case 0:
                cycleGameMode(settings);
                break;
            case 1:
                cycleTeamMode(settings);
                break;
            case 2:
                if (leftClick) {
                    settings.setTeamSize(settings.getTeamSize() + 1);
                } else {
                    settings.setTeamSize(Math.max(1, settings.getTeamSize() - 1));
                }
                break;
            case 3:
                if (leftClick) {
                    settings.setPlayerLives(settings.getPlayerLives() + 1);
                } else {
                    settings.setPlayerLives(Math.max(1, settings.getPlayerLives() - 1));
                }
                break;
            case 4:
                openTimeMenu(player, "Skin Shuffle");
                return;
            
            case 9:
                if (leftClick) {
                    settings.setMaxWorldSize(settings.getMaxWorldSize() + 100);
                } else {
                    settings.setMaxWorldSize(Math.max(100, settings.getMaxWorldSize() - 100));
                }
                break;
            case 10:
                if (leftClick) {
                    settings.setMinWorldSize(settings.getMinWorldSize() + 100);
                } else {
                    settings.setMinWorldSize(Math.max(100, settings.getMinWorldSize() - 100));
                }
                break;
            case 11:
                settings.setGradualBorderEnabled(!settings.isGradualBorderEnabled());
                break;
            
            case 18:
                openTimeMenu(player, "Juego");
                return;
            case 19:
                openTimeMenu(player, "Acuerdo");
                return;
            case 20:
                openTimeMenu(player, "Min Border");
                return;
            case 21:
                openTimeMenu(player, "Max Equipos");
                return;
            case 22:
                openTimeMenu(player, "Portal End");
                return;
            case 23:
                openTimeMenu(player, "Shulker");
                return;
            case 24:
                openTimeMenu(player, "Locator Bar");
                return;
            
            case 27:
                openTimeMenu(player, "Buffs");
                return;
            case 28:
                openRecipesMenu(player);
                return;
            case 29:
                player.closeInventory();
                player.sendMessage(UHC.getPlugin().getStatsManager().getStatsReport());
                return;
            
            case 36:
                UHC.getPlugin().getUHCManager().start();
                player.sendMessage("§a¡UHC iniciado!");
                player.closeInventory();
                return;
            case 37:
                if (settings.getGameStatus() == Settings.GameStatus.IN_PROGRESS) {
                    UHC.getPlugin().getUHCManager().pause();
                    player.sendMessage("§eUHC pausado.");
                } else {
                    player.sendMessage("§cEl juego no está en progreso.");
                }
                player.closeInventory();
                return;
            case 38:
                UHC.getPlugin().getUHCManager().cancel();
                player.sendMessage("§cUHC cancelado.");
                player.closeInventory();
                return;
            
            case 45:
                settings.save();
                player.sendMessage(vch.uhc.misc.Messages.MENU_CONFIG_SAVED());
                player.closeInventory();
                break;
            case 46:
                settings.load();
                player.sendMessage("§a¡Configuración cargada exitosamente!");
                player.closeInventory();
                openMainMenu(player);
                return;
            case 47:
                player.closeInventory();
                return;
        }

        openMainMenu(player);
    }

    private void handleTimeMenuClick(Player player, int slot, boolean leftClick, String timeType) {
        Settings settings = UHC.getPlugin().getSettings();

        switch (slot) {
            case 11:
                adjustTime(settings, timeType, "hours", leftClick);
                break;
            case 13:
                adjustTime(settings, timeType, "minutes", leftClick);
                break;
            case 15:
                adjustTime(settings, timeType, "seconds", leftClick);
                break;
            case 22:
                toggleEnabled(settings, timeType);
                break;
            case 18:
                openMainMenu(player);
                return;
        }

        openTimeMenu(player, timeType);
    }

    private void adjustTime(Settings settings, String timeType, String unit, boolean increase) {
        int delta = increase ? 1 : -1;

        switch (timeType) {
            case "Juego":
                if (unit.equals("hours")) settings.setGameHours(Math.max(0, settings.getGameHours() + delta));
                else if (unit.equals("minutes")) settings.setGameMinutes(Math.max(0, settings.getGameMinutes() + delta));
                else settings.setGameSeconds(Math.max(0, settings.getGameSeconds() + delta));
                break;
            case "Acuerdo":
                if (unit.equals("hours")) settings.setAgreementHours(Math.max(0, settings.getAgreementHours() + delta));
                else if (unit.equals("minutes")) settings.setAgreementMinutes(Math.max(0, settings.getAgreementMinutes() + delta));
                else settings.setAgreementSeconds(Math.max(0, settings.getAgreementSeconds() + delta));
                break;
            case "Min Border":
                if (unit.equals("hours")) settings.setMinWorldBorderHours(Math.max(0, settings.getMinWorldBorderHours() + delta));
                else if (unit.equals("minutes")) settings.setMinWorldBorderMinutes(Math.max(0, settings.getMinWorldBorderMinutes() + delta));
                else settings.setMinWorldBorderSeconds(Math.max(0, settings.getMinWorldBorderSeconds() + delta));
                break;
            case "Max Equipos":
                if (unit.equals("hours")) settings.setMaxTeamInGameHours(Math.max(0, settings.getMaxTeamInGameHours() + delta));
                else if (unit.equals("minutes")) settings.setMaxTeamInGameMinutes(Math.max(0, settings.getMaxTeamInGameMinutes() + delta));
                else settings.setMaxTeamInGameSeconds(Math.max(0, settings.getMaxTeamInGameSeconds() + delta));
                break;
            case "Portal End":
                if (unit.equals("hours")) settings.setEndPortalHours(Math.max(0, settings.getEndPortalHours() + delta));
                else if (unit.equals("minutes")) settings.setEndPortalMinutes(Math.max(0, settings.getEndPortalMinutes() + delta));
                else settings.setEndPortalSeconds(Math.max(0, settings.getEndPortalSeconds() + delta));
                break;
            case "Locator Bar":
                if (unit.equals("hours")) settings.setLocatorBarHours(Math.max(0, settings.getLocatorBarHours() + delta));
                else if (unit.equals("minutes")) settings.setLocatorBarMinutes(Math.max(0, settings.getLocatorBarMinutes() + delta));
                else settings.setLocatorBarSeconds(Math.max(0, settings.getLocatorBarSeconds() + delta));
                break;
            case "Shulker":
                if (unit.equals("hours")) settings.setShulkerHours(Math.max(0, settings.getShulkerHours() + delta));
                else if (unit.equals("minutes")) settings.setShulkerMinutes(Math.max(0, settings.getShulkerMinutes() + delta));
                else settings.setShulkerSeconds(Math.max(0, settings.getShulkerSeconds() + delta));
                break;
            case "Buffs":
                if (unit.equals("hours")) settings.setBuffsHours(Math.max(0, settings.getBuffsHours() + delta));
                else if (unit.equals("minutes")) settings.setBuffsMinutes(Math.max(0, settings.getBuffsMinutes() + delta));
                else settings.setBuffsSeconds(Math.max(0, settings.getBuffsSeconds() + delta));
                break;
            case "Skin Shuffle":
                if (unit.equals("hours")) ;
                else if (unit.equals("minutes")) settings.setSkinShuffleMinutes(Math.max(0, settings.getSkinShuffleMinutes() + delta));
                else settings.setSkinShuffleSeconds(Math.max(0, settings.getSkinShuffleSeconds() + delta));
                break;
        }
    }

    private void toggleEnabled(Settings settings, String timeType) {
        switch (timeType) {
            case "Locator Bar":
                settings.setLocatorBarEnabled(!settings.isLocatorBarEnabled());
                break;
            case "Shulker":
                settings.setShulkerEnabled(!settings.isShulkerEnabled());
                break;
            case "Buffs":
                settings.setBuffsEnabled(!settings.isBuffsEnabled());
                break;
            case "Skin Shuffle":
                settings.setSkinShuffleEnabled(!settings.isSkinShuffleEnabled());
                break;
        }
    }

    private void cycleGameMode(Settings settings) {
        Settings.GameMode[] modes = Settings.GameMode.values();
        Settings.GameMode current = settings.getGameMode();
        
        int nextIndex = (current.ordinal() + 1) % modes.length;
        settings.setGameMode(modes[nextIndex]);
    }

    private void cycleTeamMode(Settings settings) {
        Settings.TeamMode[] modes = Settings.TeamMode.values();
        Settings.TeamMode current = settings.getTeamMode();
        
        int nextIndex = (current.ordinal() + 1) % modes.length;
        settings.setTeamMode(modes[nextIndex]);
    }

    public void cleanupPlayer(UUID playerId) {
        playerMenuContext.remove(playerId);
    }
}
