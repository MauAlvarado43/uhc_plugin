package vch.uhc.managers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import vch.uhc.UHC;
import vch.uhc.misc.BaseItem;
import vch.uhc.misc.Settings;
import vch.uhc.models.UHCTeam;
import vch.uhc.models.UHCPlayer;
import vch.uhc.misc.enums.GameMode;
import vch.uhc.misc.enums.GameState;
import vch.uhc.misc.enums.TeamMode;

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
        Inventory menu = Bukkit.createInventory(null, MENU_SIZE, Component.text(getMainMenuTitle()));

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
                vch.uhc.misc.Messages.MENU_SKIN_SHUFFLE(),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_STATUS(settings.isSkinShuffleEnabled()
                                ? vch.uhc.misc.Messages.MENU_ENABLED() : vch.uhc.misc.Messages.MENU_DISABLED()),
                        vch.uhc.misc.Messages.MENU_SKIN_SHUFFLE_INTERVAL(settings.getSkinShuffleMinutes(), settings.getSkinShuffleSeconds()),
                        "",
                        vch.uhc.misc.Messages.MENU_CLICK_TO_CONFIGURE()
                )
        ));

        menu.setItem(5, createMenuItem(
                Material.WHITE_BANNER,
                vch.uhc.misc.Messages.MENU_TEAMS(),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_TEAMS_DESC(),
                        vch.uhc.misc.Messages.MENU_TEAMS_MODE(settings.getTeamMode().name()),
                        "",
                        vch.uhc.misc.Messages.MENU_TEAMS_CLICK_TO_MANAGE()
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
                vch.uhc.misc.Messages.MENU_GRADUAL_BORDER(),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_STATUS(settings.isGradualBorderEnabled()
                                ? vch.uhc.misc.Messages.MENU_ENABLED() : vch.uhc.misc.Messages.MENU_DISABLED()),
                        "",
                        vch.uhc.misc.Messages.MENU_GRADUAL_BORDER_DESC(),
                        settings.isGradualBorderEnabled()
                        ? vch.uhc.misc.Messages.MENU_GRADUAL_BORDER_GRADUAL() : vch.uhc.misc.Messages.MENU_GRADUAL_BORDER_INSTANT(),
                        "",
                        vch.uhc.misc.Messages.MENU_CLICK_TO_TOGGLE(settings.isGradualBorderEnabled()
                                ? vch.uhc.misc.Messages.MENU_DISABLE() : vch.uhc.misc.Messages.MENU_ENABLE())
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
                        vch.uhc.misc.Messages.MENU_CURRENT_SIMPLE()
                        + settings.getEndPortalHours() + "h "
                        + settings.getEndPortalMinutes() + "m "
                        + settings.getEndPortalSeconds() + "s",
                        "",
                        vch.uhc.misc.Messages.MENU_CLICK_TO_CONFIGURE()
                )
        ));

        menu.setItem(23, createMenuItem(
                Material.SHULKER_BOX,
                vch.uhc.misc.Messages.MENU_SHULKER_SPAWN(),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_STATUS(settings.isShulkerEnabled()
                                ? vch.uhc.misc.Messages.MENU_ENABLED() : vch.uhc.misc.Messages.MENU_DISABLED()),
                        vch.uhc.misc.Messages.MENU_TIME_SIMPLE()
                        + settings.getShulkerHours() + "h "
                        + settings.getShulkerMinutes() + "m "
                        + settings.getShulkerSeconds() + "s",
                        "",
                        vch.uhc.misc.Messages.MENU_CLICK_TO_CONFIGURE()
                )
        ));

        menu.setItem(24, createMenuItem(
                Material.RECOVERY_COMPASS,
                vch.uhc.misc.Messages.MENU_LOCATOR_BAR_TIME(),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_LOCATOR_BAR_DESC(),
                        vch.uhc.misc.Messages.MENU_CURRENT_SIMPLE()
                        + settings.getLocatorBarHours() + "h "
                        + settings.getLocatorBarMinutes() + "m "
                        + settings.getLocatorBarSeconds() + "s",
                        vch.uhc.misc.Messages.MENU_STATUS(settings.isLocatorBarEnabled()
                                ? vch.uhc.misc.Messages.MENU_ENABLED() : vch.uhc.misc.Messages.MENU_DISABLED()),
                        "",
                        vch.uhc.misc.Messages.MENU_CLICK_TO_CONFIGURE()
                )
        ));

        menu.setItem(27, createMenuItem(
                Material.ENCHANTED_GOLDEN_APPLE,
                vch.uhc.misc.Messages.MENU_PLAYER_BUFFS(),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_STATUS(settings.isBuffsEnabled()
                                ? vch.uhc.misc.Messages.MENU_ENABLED() : vch.uhc.misc.Messages.MENU_DISABLED()),
                        vch.uhc.misc.Messages.MENU_TIME(
                                settings.getBuffsHours(),
                                settings.getBuffsMinutes(),
                                settings.getBuffsSeconds()),
                        vch.uhc.misc.Messages.MENU_PLAYER_BUFFS_EXTRA_HEARTS((int) settings.getExtraHearts()),
                        vch.uhc.misc.Messages.MENU_PLAYER_BUFFS_MAX_HEALTH((int) settings.getMaxHealth()),
                        vch.uhc.misc.Messages.MENU_PLAYER_BUFFS_RESISTANCE(),
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
                vch.uhc.misc.Messages.MENU_START_GAME(),
                Arrays.asList(vch.uhc.misc.Messages.MENU_START_GAME_DESC().split("\n"))
        ));

        menu.setItem(37, createMenuItem(
                Material.YELLOW_WOOL,
                vch.uhc.misc.Messages.MENU_PAUSE_GAME(),
                Arrays.asList(vch.uhc.misc.Messages.MENU_PAUSE_GAME_DESC().split("\n"))
        ));

        menu.setItem(38, createMenuItem(
                Material.RED_WOOL,
                vch.uhc.misc.Messages.MENU_CANCEL_GAME(),
                Arrays.asList(vch.uhc.misc.Messages.MENU_CANCEL_GAME_DESC().split("\n"))
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
                vch.uhc.misc.Messages.MENU_LOAD_CONFIG(),
                Arrays.asList(vch.uhc.misc.Messages.MENU_LOAD_CONFIG_DESC().split("\n"))
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
        Inventory menu = Bukkit.createInventory(null, 27, Component.text(getTimeMenuTitle() + timeType));

        Settings settings = UHC.getPlugin().getSettings();
        int hours = 0, minutes = 0, seconds = 0;
        boolean hasEnabled = false;
        boolean enabled = false;

        switch (timeType) {
            case "Game" -> {
                hours = settings.getGameHours();
                minutes = settings.getGameMinutes();
                seconds = settings.getGameSeconds();
            }
            case "Agreement" -> {
                hours = settings.getAgreementHours();
                minutes = settings.getAgreementMinutes();
                seconds = settings.getAgreementSeconds();
            }
            case "Min Border" -> {
                hours = settings.getMinWorldBorderHours();
                minutes = settings.getMinWorldBorderMinutes();
                seconds = settings.getMinWorldBorderSeconds();
            }
            case "Max Equipos" -> {
                hours = settings.getMaxTeamInGameHours();
                minutes = settings.getMaxTeamInGameMinutes();
                seconds = settings.getMaxTeamInGameSeconds();
            }
            case "Portal End" -> {
                hours = settings.getEndPortalHours();
                minutes = settings.getEndPortalMinutes();
                seconds = settings.getEndPortalSeconds();
            }
            case "Locator Bar" -> {
                hours = settings.getLocatorBarHours();
                minutes = settings.getLocatorBarMinutes();
                seconds = settings.getLocatorBarSeconds();
                hasEnabled = true;
                enabled = settings.isLocatorBarEnabled();
            }
            case "Buffs" -> {
                hours = settings.getBuffsHours();
                minutes = settings.getBuffsMinutes();
                seconds = settings.getBuffsSeconds();
                hasEnabled = true;
                enabled = settings.isBuffsEnabled();
            }
            case "Shulker" -> {
                hours = settings.getShulkerHours();
                minutes = settings.getShulkerMinutes();
                seconds = settings.getShulkerSeconds();
                hasEnabled = true;
                enabled = settings.isShulkerEnabled();
            }
            case "Skin Shuffle" -> {
                hours = 0;
                minutes = settings.getSkinShuffleMinutes();
                seconds = settings.getSkinShuffleSeconds();
                hasEnabled = true;
                enabled = settings.isSkinShuffleEnabled();
            }
        }

        menu.setItem(11, createMenuItem(
                Material.REDSTONE,
                vch.uhc.misc.Messages.MENU_TIME_HOURS(hours),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_LEFT(),
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_RIGHT()
                )
        ));

        menu.setItem(13, createMenuItem(
                Material.GLOWSTONE_DUST,
                vch.uhc.misc.Messages.MENU_TIME_MINUTES(minutes),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_LEFT(),
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_RIGHT()
                )
        ));

        menu.setItem(15, createMenuItem(
                Material.GUNPOWDER,
                vch.uhc.misc.Messages.MENU_TIME_SECONDS(seconds),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_LEFT(),
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_RIGHT()
                )
        ));

        if (hasEnabled) {
            menu.setItem(22, createMenuItem(
                    enabled ? Material.LIME_DYE : Material.GRAY_DYE,
                    (enabled ? vch.uhc.misc.Messages.MENU_ENABLED() : vch.uhc.misc.Messages.MENU_DISABLED()),
                    Arrays.asList(
                            vch.uhc.misc.Messages.MENU_CLICK_TO_TOGGLE(enabled
                                    ? vch.uhc.misc.Messages.MENU_DISABLE() : vch.uhc.misc.Messages.MENU_ENABLE())
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
        Inventory menu = Bukkit.createInventory(null, 54, Component.text(getRecipesMenuTitle()));

        Settings settings = UHC.getPlugin().getSettings();
        List<BaseItem> items = settings.getItems();

        int slot = 10;
        for (BaseItem item : items) {
            menu.setItem(slot, createMenuItem(
                    item.getItemStack().getType(),
                    "\u00a7b" + item.getName(),
                    Arrays.asList(
                            vch.uhc.misc.Messages.INFO_STATUS() + (item.isEnabled()
                            ? vch.uhc.misc.Messages.INFO_ENABLED() : vch.uhc.misc.Messages.INFO_DISABLED()),
                            "",
                            vch.uhc.misc.Messages.INFO_CLICK_TO_TOGGLE(item.isEnabled() ? vch.uhc.misc.Messages.MENU_DISABLE() : vch.uhc.misc.Messages.MENU_ENABLE())
                    )
            ));
            slot++;
            if (slot % 9 == 8) {
                slot += 2;
            }
        }

        menu.setItem(49, createMenuItem(
                Material.ARROW,
                vch.uhc.misc.Messages.INFO_BACK(),
                Arrays.asList(
                        vch.uhc.misc.Messages.INFO_BACK_DESC()
                )
        ));

        playerMenuContext.put(player.getUniqueId(), "RECIPES");
        player.openInventory(menu);
    }

    public void openBuffsMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, Component.text(vch.uhc.misc.Messages.MENU_PLAYER_BUFFS()));
        
        Settings settings = UHC.getPlugin().getSettings();
        
        menu.setItem(10, createMenuItem(
                Material.REDSTONE,
                vch.uhc.misc.Messages.MENU_TIME_HOURS(settings.getBuffsHours()),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_LEFT(),
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_RIGHT()
                )
        ));
        
        menu.setItem(12, createMenuItem(
                Material.GLOWSTONE_DUST,
                vch.uhc.misc.Messages.MENU_TIME_MINUTES(settings.getBuffsMinutes()),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_LEFT(),
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_RIGHT()
                )
        ));
        
        menu.setItem(14, createMenuItem(
                Material.GUNPOWDER,
                vch.uhc.misc.Messages.MENU_TIME_SECONDS(settings.getBuffsSeconds()),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_LEFT(),
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_RIGHT()
                )
        ));
        
        menu.setItem(16, createMenuItem(
                settings.isBuffsEnabled() ? Material.LIME_DYE : Material.GRAY_DYE,
                settings.isBuffsEnabled() ? vch.uhc.misc.Messages.MENU_ENABLED() : vch.uhc.misc.Messages.MENU_DISABLED(),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_CLICK_TO_TOGGLE(settings.isBuffsEnabled()
                                ? vch.uhc.misc.Messages.MENU_DISABLE() : vch.uhc.misc.Messages.MENU_ENABLE())
                )
        ));
        
        menu.setItem(21, createMenuItem(
                Material.GOLDEN_APPLE,
                vch.uhc.misc.Messages.MENU_PLAYER_BUFFS_EXTRA_HEARTS_CONFIG(),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_PLAYER_BUFFS_EXTRA_HEARTS_DESC((int) settings.getExtraHearts()),
                        "",
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_LEFT(),
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_RIGHT()
                )
        ));
        
        menu.setItem(23, createMenuItem(
                Material.ENCHANTED_GOLDEN_APPLE,
                vch.uhc.misc.Messages.MENU_PLAYER_BUFFS_MAX_HEALTH_CONFIG(),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_PLAYER_BUFFS_MAX_HEALTH_DESC((int) settings.getMaxHealth()),
                        "",
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_LEFT(),
                        vch.uhc.misc.Messages.MENU_TEAM_SIZE_RIGHT()
                )
        ));
        
        menu.setItem(49, createMenuItem(
                Material.ARROW,
                vch.uhc.misc.Messages.MENU_BACK(),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_BACK_TO_MAIN()
                )
        ));
        
        playerMenuContext.put(player.getUniqueId(), "BUFFS");
        player.openInventory(menu);
    }

    public void openTeamsMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 54, Component.text(vch.uhc.misc.Messages.MENU_TEAMS_TITLE()));
        
        List<UHCTeam> teams = UHC.getPlugin().getTeamManager().getTeams();
        
        // Create Team button
        menu.setItem(49, createMenuItem(
                Material.LIME_DYE,
                vch.uhc.misc.Messages.MENU_TEAMS_CREATE_TEAM(),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_TEAMS_CREATE_TEAM_DESC()
                )
        ));
        
        // Back button
        menu.setItem(45, createMenuItem(
                Material.ARROW,
                vch.uhc.misc.Messages.MENU_BACK(),
                Arrays.asList(
                        vch.uhc.misc.Messages.MENU_BACK_TO_MAIN()
                )
        ));
        
        // Display teams
        if (teams.isEmpty()) {
            menu.setItem(22, createMenuItem(
                    Material.BARRIER,
                    vch.uhc.misc.Messages.MENU_TEAMS_NO_TEAMS(),
                    Arrays.asList()
            ));
        } else {
            int slot = 10;
            for (UHCTeam team : teams) {
                if (slot >= 44 && slot != 49) break;
                
                List<String> lore = new java.util.ArrayList<>();
                lore.add(vch.uhc.misc.Messages.MENU_TEAMS_MEMBERS(team.getMembers().size()));
                lore.add("");
                
                for (vch.uhc.models.UHCPlayer member : team.getMembers()) {
                    String prefix = member == team.getLeader() ? "§6★ " : "§f  • ";
                    lore.add(prefix + member.getName());
                }
                
                lore.add("");
                lore.add(vch.uhc.misc.Messages.MENU_TEAMS_CLICK_TO_MANAGE());
                
                menu.setItem(slot, createMenuItem(
                        Material.PLAYER_HEAD,
                        "§b" + team.getName(),
                        lore
                ));
                
                slot++;
                if (slot % 9 == 8) slot += 2;
            }
        }
        
        playerMenuContext.put(player.getUniqueId(), "TEAMS");
        player.openInventory(menu);
    }

    private ItemStack createMenuItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.displayName(LegacyComponentSerializer.legacySection().deserialize(name));
            meta.lore(lore.stream()
                    .map(line -> LegacyComponentSerializer.legacySection().deserialize(line))
                    .collect(java.util.stream.Collectors.toList()));
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
                if (checkSlot % 9 == 8) {
                    checkSlot += 2;
                }
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

        if (context != null && context.equals("BUFFS")) {
            handleBuffsMenuClick(player, slot, leftClick, settings);
            return;
        }

        if (context != null && context.equals("TEAMS")) {
            handleTeamsMenuClick(player, slot);
            return;
        }

        if (context != null && context.startsWith("TEAM_MANAGE:")) {
            String teamName = context.substring("TEAM_MANAGE:".length());
            handleTeamManagementClick(player, slot, teamName);
            return;
        }

        if (context != null && context.startsWith("PLAYER_SELECT:")) {
            String teamName = context.substring("PLAYER_SELECT:".length());
            handlePlayerSelectionClick(player, slot, teamName);
            return;
        }

        if (menuTitle.equals(getMainMenuTitle())) {
            handleMainMenuClick(player, slot, leftClick, settings);
        }
    }

    private void handleMainMenuClick(Player player, int slot, boolean leftClick, Settings settings) {
        switch (slot) {
            case 0 ->
                cycleGameMode(settings);
            case 1 ->
                cycleTeamMode(settings);
            case 2 -> {
                if (leftClick) {
                    settings.setTeamSize(settings.getTeamSize() + 1);
                } else {
                    settings.setTeamSize(Math.max(1, settings.getTeamSize() - 1));
                }
            }
            case 3 -> {
                if (leftClick) {
                    settings.setPlayerLives(settings.getPlayerLives() + 1);
                } else {
                    settings.setPlayerLives(Math.max(1, settings.getPlayerLives() - 1));
                }
            }
            case 4 -> {
                openTimeMenu(player, "Skin Shuffle");
                return;
            }
            case 5 -> {
                openTeamsMenu(player);
                return;
            }
            case 9 -> {
                if (leftClick) {
                    settings.setMaxWorldSize(settings.getMaxWorldSize() + 100);
                } else {
                    settings.setMaxWorldSize(Math.max(100, settings.getMaxWorldSize() - 100));
                }
            }
            case 10 -> {
                if (leftClick) {
                    settings.setMinWorldSize(settings.getMinWorldSize() + 100);
                } else {
                    settings.setMinWorldSize(Math.max(100, settings.getMinWorldSize() - 100));
                }
            }
            case 11 ->
                settings.setGradualBorderEnabled(!settings.isGradualBorderEnabled());
            case 18 -> {
                openTimeMenu(player, "Game");
                return;
            }
            case 19 -> {
                openTimeMenu(player, "Agreement");
                return;
            }
            case 20 -> {
                openTimeMenu(player, "Min Border");
                return;
            }
            case 21 -> {
                openTimeMenu(player, "Max Equipos");
                return;
            }
            case 22 -> {
                openTimeMenu(player, "Portal End");
                return;
            }
            case 23 -> {
                openTimeMenu(player, "Shulker");
                return;
            }
            case 24 -> {
                openTimeMenu(player, "Locator Bar");
                return;
            }
            case 27 -> {
                openBuffsMenu(player);
                return;
            }
            case 28 -> {
                openRecipesMenu(player);
                return;
            }
            case 29 -> {
                player.closeInventory();
                player.sendMessage(UHC.getPlugin().getStatsManager().getStatsReport());
                return;
            }
            case 36 -> {
                UHC.getPlugin().getUHCManager().start();
                player.sendMessage(vch.uhc.misc.Messages.MENU_GAME_STARTED_MSG());
                player.closeInventory();
                return;
            }
            case 37 -> {
                if (settings.getGameState() == GameState.IN_PROGRESS) {
                    UHC.getPlugin().getUHCManager().pause();
                    player.sendMessage(vch.uhc.misc.Messages.MENU_GAME_PAUSED());
                } else {
                    player.sendMessage(vch.uhc.misc.Messages.MENU_GAME_NOT_RUNNING());
                }
                player.closeInventory();
                return;
            }
            case 38 -> {
                UHC.getPlugin().getUHCManager().cancel();
                player.sendMessage(vch.uhc.misc.Messages.MENU_GAME_CANCELLED_MSG());
                player.closeInventory();
                return;
            }
            case 45 -> {
                settings.save();
                player.sendMessage(vch.uhc.misc.Messages.MENU_CONFIG_SAVED());
                player.closeInventory();
            }
            case 46 -> {
                settings.load();
                player.sendMessage(vch.uhc.misc.Messages.MENU_LOAD_CONFIG_SUCCESS());
                player.closeInventory();
                openMainMenu(player);
                return;
            }
            case 47 -> {
                player.closeInventory();
                return;
            }
            default -> {
            }
        }

        openMainMenu(player);
    }

    private void handleTimeMenuClick(Player player, int slot, boolean leftClick, String timeType) {
        Settings settings = UHC.getPlugin().getSettings();

        switch (slot) {
            case 11 ->
                adjustTime(settings, timeType, "hours", leftClick);
            case 13 ->
                adjustTime(settings, timeType, "minutes", leftClick);
            case 15 ->
                adjustTime(settings, timeType, "seconds", leftClick);
            case 22 ->
                toggleEnabled(settings, timeType);
            case 18 -> {
                openMainMenu(player);
                return;
            }
            default -> {
            }
        }

        openTimeMenu(player, timeType);
    }

    private void adjustTime(Settings settings, String timeType, String unit, boolean increase) {
        int delta = increase ? 1 : -1;

        switch (timeType) {
            case "Game" -> {
                switch (unit) {
                    case "hours" -> settings.setGameHours(Math.max(0, settings.getGameHours() + delta));
                    case "minutes" -> settings.setGameMinutes(Math.max(0, settings.getGameMinutes() + delta));
                    default -> settings.setGameSeconds(Math.max(0, settings.getGameSeconds() + delta));
                }
            }
            case "Agreement" -> {
                switch (unit) {
                    case "hours" -> settings.setAgreementHours(Math.max(0, settings.getAgreementHours() + delta));
                    case "minutes" -> settings.setAgreementMinutes(Math.max(0, settings.getAgreementMinutes() + delta));
                    default -> settings.setAgreementSeconds(Math.max(0, settings.getAgreementSeconds() + delta));
                }
            }
            case "Min Border" -> {
                switch (unit) {
                    case "hours" -> settings.setMinWorldBorderHours(Math.max(0, settings.getMinWorldBorderHours() + delta));
                    case "minutes" -> settings.setMinWorldBorderMinutes(Math.max(0, settings.getMinWorldBorderMinutes() + delta));
                    default -> settings.setMinWorldBorderSeconds(Math.max(0, settings.getMinWorldBorderSeconds() + delta));
                }
            }
            case "Max Equipos" -> {
                switch (unit) {
                    case "hours" -> settings.setMaxTeamInGameHours(Math.max(0, settings.getMaxTeamInGameHours() + delta));
                    case "minutes" -> settings.setMaxTeamInGameMinutes(Math.max(0, settings.getMaxTeamInGameMinutes() + delta));
                    default -> settings.setMaxTeamInGameSeconds(Math.max(0, settings.getMaxTeamInGameSeconds() + delta));
                }
            }
            case "Portal End" -> {
                switch (unit) {
                    case "hours" -> settings.setEndPortalHours(Math.max(0, settings.getEndPortalHours() + delta));
                    case "minutes" -> settings.setEndPortalMinutes(Math.max(0, settings.getEndPortalMinutes() + delta));
                    default -> settings.setEndPortalSeconds(Math.max(0, settings.getEndPortalSeconds() + delta));
                }
            }
            case "Locator Bar" -> {
                switch (unit) {
                    case "hours" -> settings.setLocatorBarHours(Math.max(0, settings.getLocatorBarHours() + delta));
                    case "minutes" -> settings.setLocatorBarMinutes(Math.max(0, settings.getLocatorBarMinutes() + delta));
                    default -> settings.setLocatorBarSeconds(Math.max(0, settings.getLocatorBarSeconds() + delta));
                }
            }
            case "Shulker" -> {
                switch (unit) {
                    case "hours" -> settings.setShulkerHours(Math.max(0, settings.getShulkerHours() + delta));
                    case "minutes" -> settings.setShulkerMinutes(Math.max(0, settings.getShulkerMinutes() + delta));
                    default -> settings.setShulkerSeconds(Math.max(0, settings.getShulkerSeconds() + delta));
                }
            }
            case "Buffs" -> {
                switch (unit) {
                    case "hours" -> settings.setBuffsHours(Math.max(0, settings.getBuffsHours() + delta));
                    case "minutes" -> settings.setBuffsMinutes(Math.max(0, settings.getBuffsMinutes() + delta));
                    default -> settings.setBuffsSeconds(Math.max(0, settings.getBuffsSeconds() + delta));
                }
            }
            case "Skin Shuffle" -> {
                switch (unit) {
                    case "minutes" -> settings.setSkinShuffleMinutes(Math.max(0, settings.getSkinShuffleMinutes() + delta));
                    case "seconds" -> settings.setSkinShuffleSeconds(Math.max(0, settings.getSkinShuffleSeconds() + delta));
                }
            }
        }
    }

    private void toggleEnabled(Settings settings, String timeType) {
        switch (timeType) {
            case "Locator Bar" ->
                settings.setLocatorBarEnabled(!settings.isLocatorBarEnabled());
            case "Shulker" ->
                settings.setShulkerEnabled(!settings.isShulkerEnabled());
            case "Buffs" ->
                settings.setBuffsEnabled(!settings.isBuffsEnabled());
            case "Skin Shuffle" ->
                settings.setSkinShuffleEnabled(!settings.isSkinShuffleEnabled());
        }
    }

    private void cycleGameMode(Settings settings) {
        GameMode[] modes = GameMode.values();
        GameMode current = settings.getGameMode();

        int nextIndex = (current.ordinal() + 1) % modes.length;
        settings.setGameMode(modes[nextIndex]);
    }

    private void cycleTeamMode(Settings settings) {
        TeamMode[] modes = TeamMode.values();
        TeamMode current = settings.getTeamMode();

        int nextIndex = (current.ordinal() + 1) % modes.length;
        settings.setTeamMode(modes[nextIndex]);
    }

    private void handleBuffsMenuClick(Player player, int slot, boolean leftClick, Settings settings) {
        switch (slot) {
            case 10 -> {
                // Hours
                int delta = leftClick ? 1 : -1;
                settings.setBuffsHours(Math.max(0, settings.getBuffsHours() + delta));
                openBuffsMenu(player);
            }
            case 12 -> {
                // Minutes
                int delta = leftClick ? 1 : -1;
                settings.setBuffsMinutes(Math.max(0, settings.getBuffsMinutes() + delta));
                openBuffsMenu(player);
            }
            case 14 -> {
                // Seconds
                int delta = leftClick ? 1 : -1;
                settings.setBuffsSeconds(Math.max(0, settings.getBuffsSeconds() + delta));
                openBuffsMenu(player);
            }
            case 16 -> {
                // Toggle enabled/disabled
                settings.setBuffsEnabled(!settings.isBuffsEnabled());
                openBuffsMenu(player);
            }
            case 21 -> {
                // Extra Hearts
                int delta = leftClick ? 1 : -1;
                settings.setExtraHearts(Math.max(1, settings.getExtraHearts() + delta));
                openBuffsMenu(player);
            }
            case 23 -> {
                // Max Health
                int delta = leftClick ? 2 : -2;
                settings.setMaxHealth(Math.max(20, settings.getMaxHealth() + delta));
                openBuffsMenu(player);
            }
            case 49 -> {
                // Back button
                openMainMenu(player);
            }
        }
    }

    private void handleTeamsMenuClick(Player player, int slot) {
        if (slot == 45) {
            // Back button
            openMainMenu(player);
        } else if (slot == 49) {
            // Create team button - auto create with Team i name and random leader
            List<UHCTeam> teams = UHC.getPlugin().getTeamManager().getTeams();
            String teamName = "Team " + (teams.size() + 1);
            
            // Get players without team
            List<UHCPlayer> playersWithoutTeam = UHC.getPlugin().getPlayerManager().getPlayers().stream()
                    .filter(p -> p.getTeam() == null)
                    .collect(java.util.stream.Collectors.toList());
            
            if (playersWithoutTeam.isEmpty()) {
                player.sendMessage(vch.uhc.misc.Messages.TEAM_NO_PLAYERS_AVAILABLE());
                return;
            }
            
            // Select random player as leader
            UHCPlayer randomLeader = playersWithoutTeam.get(new java.util.Random().nextInt(playersWithoutTeam.size()));
            UHC.getPlugin().getTeamManager().createTeam(randomLeader, teamName);
            player.sendMessage(vch.uhc.misc.Messages.TEAM_CREATED_WITH_LEADER(teamName, randomLeader.getName()));
            openTeamsMenu(player);
        } else if (slot >= 10 && slot < 44) {
            // Click on a team - open team management menu
            List<UHCTeam> teams = UHC.getPlugin().getTeamManager().getTeams();
            int teamIndex = calculateTeamIndex(slot);
            if (teamIndex >= 0 && teamIndex < teams.size()) {
                openTeamManagementMenu(player, teams.get(teamIndex));
            }
        }
    }

    private int calculateTeamIndex(int slot) {
        int row = slot / 9;
        int col = slot % 9;
        
        if (col < 1 || col > 7) return -1;
        if (row < 1 || row > 4) return -1;
        
        int index = (row - 1) * 7 + (col - 1);
        return index;
    }

    public void openTeamManagementMenu(Player player, UHCTeam team) {
        Inventory menu = Bukkit.createInventory(null, 54, Component.text("§6UHC - " + team.getName()));
        
        // Team info
        menu.setItem(4, createMenuItem(
                Material.PLAYER_HEAD,
                "§b" + team.getName(),
                Arrays.asList(
                        "§7Miembros: §f" + team.getMembers().size(),
                        "§7Líder: §f" + team.getLeader().getName()
                )
        ));
        
        // Add player button
        menu.setItem(20, createMenuItem(
                Material.LIME_DYE,
                "§aAgregar Jugador",
                Arrays.asList(
                        "§7Click para seleccionar jugador"
                )
        ));
        
        // Delete team button
        menu.setItem(24, createMenuItem(
                Material.RED_DYE,
                "§cEliminar Equipo",
                Arrays.asList(
                        "§7Click para eliminar este equipo"
                )
        ));
        
        // Display members
        int slot = 28;
        for (UHCPlayer member : team.getMembers()) {
            if (slot >= 35) break;
            menu.setItem(slot, createMenuItem(
                    Material.PLAYER_HEAD,
                    (member == team.getLeader() ? "§6★ " : "§f") + member.getName(),
                    Arrays.asList(
                            member == team.getLeader() ? "§7Líder del equipo" : "§7Click para remover"
                    )
            ));
            slot++;
        }
        
        // Back button
        menu.setItem(45, createMenuItem(
                Material.ARROW,
                vch.uhc.misc.Messages.MENU_BACK(),
                Arrays.asList(
                        "§7Volver al menú de equipos"
                )
        ));
        
        playerMenuContext.put(player.getUniqueId(), "TEAM_MANAGE:" + team.getName());
        player.openInventory(menu);
    }

    public void openPlayerSelectionMenu(Player player, UHCTeam team) {
        Inventory menu = Bukkit.createInventory(null, 54, Component.text("§6Seleccionar Jugador"));
        
        List<UHCPlayer> availablePlayers = UHC.getPlugin().getPlayerManager().getPlayers().stream()
                .filter(p -> p.getTeam() == null)
                .collect(java.util.stream.Collectors.toList());
        
        int slot = 10;
        for (UHCPlayer uhcPlayer : availablePlayers) {
            if (slot >= 44) break;
            menu.setItem(slot, createMenuItem(
                    Material.PLAYER_HEAD,
                    "§f" + uhcPlayer.getName(),
                    Arrays.asList(
                            "§7Click para agregar al equipo"
                    )
            ));
            slot++;
            if (slot % 9 == 8) slot += 2;
        }
        
        // Back button
        menu.setItem(45, createMenuItem(
                Material.ARROW,
                vch.uhc.misc.Messages.MENU_BACK(),
                Arrays.asList(
                        "§7Volver a gestión del equipo"
                )
        ));
        
        playerMenuContext.put(player.getUniqueId(), "PLAYER_SELECT:" + team.getName());
        player.openInventory(menu);
    }

    private void handleTeamManagementClick(Player player, int slot, String teamName) {
        UHCTeam team = UHC.getPlugin().getTeamManager().getTeams().stream()
                .filter(t -> t.getName().equals(teamName))
                .findFirst()
                .orElse(null);
        
        if (team == null) {
            openTeamsMenu(player);
            return;
        }
        
        if (slot == 45) {
            // Back to teams menu
            openTeamsMenu(player);
        } else if (slot == 20) {
            // Add player
            openPlayerSelectionMenu(player, team);
        } else if (slot == 24) {
            // Delete team
            UHC.getPlugin().getTeamManager().deleteTeam(team);
            player.sendMessage("§cEquipo eliminado: " + teamName);
            openTeamsMenu(player);
        } else if (slot >= 28 && slot < 35) {
            // Remove member
            int memberIndex = slot - 28;
            if (memberIndex < team.getMembers().size()) {
                UHCPlayer member = team.getMembers().get(memberIndex);
                if (member != team.getLeader()) {
                    UHC.getPlugin().getTeamManager().removePlayer(team, member);
                    player.sendMessage("§eJugador removido: " + member.getName());
                    openTeamManagementMenu(player, team);
                }
            }
        }
    }

    private void handlePlayerSelectionClick(Player player, int slot, String teamName) {
        UHCTeam team = UHC.getPlugin().getTeamManager().getTeams().stream()
                .filter(t -> t.getName().equals(teamName))
                .findFirst()
                .orElse(null);
        
        if (team == null) {
            openTeamsMenu(player);
            return;
        }
        
        if (slot == 45) {
            // Back to team management
            openTeamManagementMenu(player, team);
        } else if (slot >= 10 && slot < 44) {
            // Select player
            List<UHCPlayer> availablePlayers = UHC.getPlugin().getPlayerManager().getPlayers().stream()
                    .filter(p -> p.getTeam() == null)
                    .collect(java.util.stream.Collectors.toList());
            
            int playerIndex = calculatePlayerSelectionIndex(slot);
            if (playerIndex >= 0 && playerIndex < availablePlayers.size()) {
                UHCPlayer selectedPlayer = availablePlayers.get(playerIndex);
                UHC.getPlugin().getTeamManager().addPlayer(team, selectedPlayer);
                player.sendMessage("§aJugador agregado: " + selectedPlayer.getName());
                openTeamManagementMenu(player, team);
            }
        }
    }

    private int calculatePlayerSelectionIndex(int slot) {
        int row = slot / 9;
        int col = slot % 9;
        
        if (col < 1 || col > 7) return -1;
        if (row < 1 || row > 4) return -1;
        
        return (row - 1) * 7 + (col - 1);
    }

    public void cleanupPlayer(UUID playerId) {
        playerMenuContext.remove(playerId);
    }
}
