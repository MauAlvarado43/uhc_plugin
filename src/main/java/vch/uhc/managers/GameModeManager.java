package vch.uhc.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameRules;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import vch.uhc.UHC;
import vch.uhc.misc.Settings;
import vch.uhc.misc.enums.GameMode;
import vch.uhc.misc.enums.GameState;
import vch.uhc.models.UHCTeam;
import vch.uhc.models.UHCPlayer;

public class GameModeManager {

    private final Settings settings;
    private final TeamManager teamManager;
    private BossBar dragonHealthBar;
    private List<Material> resourceRushItems;
    private UHCTeam winningTeam;
    private BukkitTask endPortalTask;
    private BukkitTask shulkerTask;
    private BukkitTask locatorBarTask;
    private Location endPortalLocation;

    public GameModeManager() {
        this.settings = UHC.getPlugin().getSettings();
        this.teamManager = UHC.getPlugin().getTeamManager();
    }

    public void initializeGameMode() {
        GameMode mode = settings.getGameMode();
        switch (mode) {
            case PVD -> initializePvD();
            case PVP -> initializePvP();
            case RESOURCE_RUSH -> initializeResourceRush();
        }
    }

    private void initializePvD() {
        dragonHealthBar = Bukkit.createBossBar(
            vch.uhc.misc.Messages.GAMEMODE_ENDER_DRAGON(),
            BarColor.PURPLE,
            BarStyle.SEGMENTED_10
        );
        
        Bukkit.getOnlinePlayers().forEach(dragonHealthBar::addPlayer);
        
        createEndPortalStructure();
        
        scheduleEndPortalActivation();
        
        if (settings.isShulkerEnabled()) {
            scheduleShulkerSpawn();
        }
        
        if (settings.isLocatorBarEnabled()) {
            scheduleLocatorBar();
        }
        
        broadcast(vch.uhc.misc.Messages.GAMEMODE_PVD_HEADER());
        broadcast(vch.uhc.misc.Messages.GAMEMODE_PVD_OBJECTIVE());
        broadcast(vch.uhc.misc.Messages.GAMEMODE_PVD_PVP_ENABLED());
        
        int totalSeconds = settings.getEndPortalHours() * 3600 + 
                          settings.getEndPortalMinutes() * 60 + 
                          settings.getEndPortalSeconds();
        int minutes = totalSeconds / 60;
        broadcast(vch.uhc.misc.Messages.GAMEMODE_PVD_END_PORTAL(minutes));
        
        if (settings.isShulkerEnabled()) {
            int shulkerTotalSeconds = settings.getShulkerHours() * 3600 + 
                                     settings.getShulkerMinutes() * 60 + 
                                     settings.getShulkerSeconds();
            int shulkerMinutes = shulkerTotalSeconds / 60;
            broadcast(vch.uhc.misc.Messages.SHULKER_WILL_SPAWN(shulkerMinutes));
        }
        
        if (settings.isLocatorBarEnabled()) {
            int locatorTotalSeconds = settings.getLocatorBarHours() * 3600 + 
                                     settings.getLocatorBarMinutes() * 60 + 
                                     settings.getLocatorBarSeconds();
            int locatorMinutes = locatorTotalSeconds / 60;
            broadcast(vch.uhc.misc.Messages.LOCATOR_BAR_WILL_ACTIVATE(locatorMinutes));
        }
    }

    private void initializePvP() {
        if (settings.isLocatorBarEnabled()) {
            scheduleLocatorBar();
        }
        
        broadcast(vch.uhc.misc.Messages.GAMEMODE_PVP_HEADER());
        broadcast(vch.uhc.misc.Messages.GAMEMODE_PVP_OBJECTIVE());
        broadcast(vch.uhc.misc.Messages.GAMEMODE_PVP_PREPARE());        
        if (settings.isLocatorBarEnabled()) {
            int locatorTotalSeconds = settings.getLocatorBarHours() * 3600 + 
                                     settings.getLocatorBarMinutes() * 60 + 
                                     settings.getLocatorBarSeconds();
            int locatorMinutes = locatorTotalSeconds / 60;
            broadcast(vch.uhc.misc.Messages.LOCATOR_BAR_WILL_ACTIVATE(locatorMinutes));
        }    }

    private void initializeResourceRush() {
        generateResourceRushList();
        
        broadcast(vch.uhc.misc.Messages.GAMEMODE_RESOURCE_RUSH_HEADER());
        broadcast(vch.uhc.misc.Messages.GAMEMODE_RESOURCE_RUSH_OBJECTIVE());
        broadcast(vch.uhc.misc.Messages.GAMEMODE_RESOURCE_RUSH_ITEMS_LIST());
        
        for (Material material : resourceRushItems) {
            broadcast(vch.uhc.misc.Messages.GAMEMODE_RESOURCE_RUSH_ITEM(material.name()));
        }
    }

    private void generateResourceRushList() {
        List<Material> possibleItems = Arrays.asList(
            Material.DIAMOND,
            Material.EMERALD,
            Material.NETHER_STAR,
            Material.GOLDEN_APPLE,
            Material.ENCHANTED_GOLDEN_APPLE,
            Material.ENDER_PEARL,
            Material.BLAZE_ROD,
            Material.GHAST_TEAR,
            Material.DRAGON_BREATH,
            Material.ELYTRA,
            Material.TOTEM_OF_UNDYING,
            Material.TRIDENT,
            Material.HEART_OF_THE_SEA,
            Material.NETHERITE_INGOT,
            Material.ANCIENT_DEBRIS,
            Material.CRYING_OBSIDIAN,
            Material.RESPAWN_ANCHOR,
            Material.LODESTONE,
            Material.NETHER_WART,
            Material.END_CRYSTAL
        );

        Random random = new Random();
        int itemCount = 8 + random.nextInt(5);
        
        resourceRushItems = new ArrayList<>();
        List<Material> availableItems = new ArrayList<>(possibleItems);
        
        for (int i = 0; i < itemCount && !availableItems.isEmpty(); i++) {
            int index = random.nextInt(availableItems.size());
            resourceRushItems.add(availableItems.remove(index));
        }
    }

    public void checkWinCondition() {
        GameMode mode = settings.getGameMode();
        switch (mode) {
            case PVD -> {}
            case PVP -> checkPvPWinCondition();
            case RESOURCE_RUSH -> checkResourceRushWinCondition();
        }
    }

    private void checkPvPWinCondition() {
        List<UHCTeam> aliveTeams = teamManager.getTeams().stream()
            .filter(team -> team.getMembers().stream().anyMatch(vch.uhc.models.UHCPlayer::isAlive))
            .collect(Collectors.toList());

        if (aliveTeams.size() == 1) {
            endGame(aliveTeams.get(0));
        }
    }

    private void checkResourceRushWinCondition() {
        if (resourceRushItems == null || resourceRushItems.isEmpty()) {
            return;
        }

        for (UHCTeam team : teamManager.getTeams()) {
            if (teamHasAllItems(team)) {
                endGame(team);
                break;
            }
        }
    }

    private boolean teamHasAllItems(UHCTeam team) {
        List<Material> foundItems = new ArrayList<>();
        
        for (vch.uhc.models.UHCPlayer uhcPlayer : team.getMembers()) {
            Player bukkitPlayer = uhcPlayer.getBukkitPlayer();
            if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
                continue;
            }
            
            ItemStack[] inventoryContents = bukkitPlayer.getInventory().getContents();
            if (inventoryContents != null) {
                for (ItemStack item : inventoryContents) {
                    if (item != null && resourceRushItems.contains(item.getType()) && !foundItems.contains(item.getType())) {
                        foundItems.add(item.getType());
                    }
                }
            }
            
            ItemStack[] enderChestContents = bukkitPlayer.getEnderChest().getContents();
            if (enderChestContents != null) {
                for (ItemStack item : enderChestContents) {
                    if (item != null && resourceRushItems.contains(item.getType()) && !foundItems.contains(item.getType())) {
                        foundItems.add(item.getType());
                    }
                }
            }
        }
        
        return foundItems.containsAll(resourceRushItems);
    }

    public void onDragonKilled(EnderDragon dragon, Player killer) {
        if (settings.getGameMode() != GameMode.PVD) {
            return;
        }

        UHCPlayer uhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(killer.getUniqueId());
        if (uhcPlayer != null && uhcPlayer.getTeam() != null) {
            endGame(uhcPlayer.getTeam());
        }
    }

    public void updateDragonHealthBar(EnderDragon dragon) {
        if (dragonHealthBar != null && dragon != null) {
            var maxHealthAttr = dragon.getAttribute(Attribute.MAX_HEALTH);
            if (maxHealthAttr != null) {
                double maxHealth = maxHealthAttr.getValue();
                double healthPercentage = dragon.getHealth() / maxHealth;
                dragonHealthBar.setProgress(Math.max(0.0, Math.min(1.0, healthPercentage)));
            }
        }
    }

    private void endGame(UHCTeam winningTeam) {
        this.winningTeam = winningTeam;
        settings.setGameState(GameState.ENDED);

        broadcast("");
        broadcast(vch.uhc.misc.Messages.GAMEMODE_GAME_ENDED());
        broadcast(vch.uhc.misc.Messages.GAMEMODE_GAME_ENDED_TITLE());
        broadcast(vch.uhc.misc.Messages.GAMEMODE_WINNING_TEAM(winningTeam.getName()));
        broadcast(vch.uhc.misc.Messages.GAMEMODE_MODE_USED(settings.getGameMode().name()));
        broadcast(vch.uhc.misc.Messages.GAMEMODE_GAME_ENDED());
        broadcast("");

        String stats = UHC.getPlugin().getStatsManager().getStatsReport();
        broadcast(stats);
    }

    public List<Material> getResourceRushItems() {
        return resourceRushItems;
    }

    public void cleanup() {
        if (dragonHealthBar != null) {
            dragonHealthBar.removeAll();
            dragonHealthBar = null;
        }
        if (endPortalTask != null) {
            endPortalTask.cancel();
            endPortalTask = null;
        }
        if (shulkerTask != null) {
            shulkerTask.cancel();
            shulkerTask = null;
        }
        if (locatorBarTask != null) {
            locatorBarTask.cancel();
            locatorBarTask = null;
        }
        resourceRushItems = null;
        winningTeam = null;
        endPortalLocation = null;
    }

    private void createEndPortalStructure() {
        World world = Bukkit.getWorld("world");
        if (world == null) {
            return;
        }

        int y = world.getHighestBlockYAt(0, 0);
        endPortalLocation = new Location(world, 0, y, 0);

        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                Block block = world.getBlockAt(endPortalLocation.getBlockX() + x, y - 1, endPortalLocation.getBlockZ() + z);
                block.setType(Material.OBSIDIAN);
            }
        }

        world.getBlockAt(endPortalLocation.getBlockX() - 1, y, endPortalLocation.getBlockZ() - 2).setType(Material.END_PORTAL_FRAME);
        world.getBlockAt(endPortalLocation.getBlockX(), y, endPortalLocation.getBlockZ() - 2).setType(Material.END_PORTAL_FRAME);
        world.getBlockAt(endPortalLocation.getBlockX() + 1, y, endPortalLocation.getBlockZ() - 2).setType(Material.END_PORTAL_FRAME);

        world.getBlockAt(endPortalLocation.getBlockX() - 1, y, endPortalLocation.getBlockZ() + 2).setType(Material.END_PORTAL_FRAME);
        world.getBlockAt(endPortalLocation.getBlockX(), y, endPortalLocation.getBlockZ() + 2).setType(Material.END_PORTAL_FRAME);
        world.getBlockAt(endPortalLocation.getBlockX() + 1, y, endPortalLocation.getBlockZ() + 2).setType(Material.END_PORTAL_FRAME);

        world.getBlockAt(endPortalLocation.getBlockX() - 2, y, endPortalLocation.getBlockZ() - 1).setType(Material.END_PORTAL_FRAME);
        world.getBlockAt(endPortalLocation.getBlockX() - 2, y, endPortalLocation.getBlockZ()).setType(Material.END_PORTAL_FRAME);
        world.getBlockAt(endPortalLocation.getBlockX() - 2, y, endPortalLocation.getBlockZ() + 1).setType(Material.END_PORTAL_FRAME);

        world.getBlockAt(endPortalLocation.getBlockX() + 2, y, endPortalLocation.getBlockZ() - 1).setType(Material.END_PORTAL_FRAME);
        world.getBlockAt(endPortalLocation.getBlockX() + 2, y, endPortalLocation.getBlockZ()).setType(Material.END_PORTAL_FRAME);
        world.getBlockAt(endPortalLocation.getBlockX() + 2, y, endPortalLocation.getBlockZ() + 1).setType(Material.END_PORTAL_FRAME);
    }

    private void scheduleEndPortalActivation() {
        int totalSeconds = settings.getEndPortalHours() * 3600 + 
                          settings.getEndPortalMinutes() * 60 + 
                          settings.getEndPortalSeconds();
        
        long ticks = totalSeconds * 20L;

        endPortalTask = Bukkit.getScheduler().runTaskLater(UHC.getPlugin(), () -> {
            activateEndPortal();
        }, ticks);
    }

    private void activateEndPortal() {
        if (endPortalLocation == null) {
            return;
        }

        World world = endPortalLocation.getWorld();
        int y = endPortalLocation.getBlockY();

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                Block block = world.getBlockAt(endPortalLocation.getBlockX() + x, y, endPortalLocation.getBlockZ() + z);
                block.setType(Material.END_PORTAL);
            }
        }

        broadcast("");
        broadcast(vch.uhc.misc.Messages.GAMEMODE_GAME_ENDED());
        broadcast(vch.uhc.misc.Messages.GAMEMODE_END_PORTAL_ACTIVATED());
        broadcast(vch.uhc.misc.Messages.GAMEMODE_END_PORTAL_LOCATION(y));
        broadcast(vch.uhc.misc.Messages.GAMEMODE_END_PORTAL_GO());
        broadcast(vch.uhc.misc.Messages.GAMEMODE_GAME_ENDED());
        broadcast("");
    }

    private void scheduleShulkerSpawn() {
        int totalSeconds = settings.getShulkerHours() * 3600 + 
                          settings.getShulkerMinutes() * 60 + 
                          settings.getShulkerSeconds();
        
        long ticks = totalSeconds * 20L;

        shulkerTask = Bukkit.getScheduler().runTaskLater(UHC.getPlugin(), () -> {
            giveShulkerBoxes();
        }, ticks);
    }

    private void giveShulkerBoxes() {
        broadcast("");
        broadcast(vch.uhc.misc.Messages.SHULKER_BORDER());
        broadcast("§d§l¡SHULKER BOXES ENTREGADAS!");
        broadcast("§eCada jugador ha recibido una Shulker Box");
        broadcast(vch.uhc.misc.Messages.SHULKER_BORDER());
        broadcast("");

        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            org.bukkit.inventory.ItemStack shulkerBox = new org.bukkit.inventory.ItemStack(
                org.bukkit.Material.SHULKER_BOX, 1
            );
            player.getInventory().addItem(shulkerBox);
            player.sendMessage("§a✓ Has recibido una Shulker Box");
        }
    }

    private void scheduleLocatorBar() {
        int totalSeconds = settings.getLocatorBarHours() * 3600 + 
                          settings.getLocatorBarMinutes() * 60 + 
                          settings.getLocatorBarSeconds();
        
        long ticks = totalSeconds * 20L;

        locatorBarTask = Bukkit.getScheduler().runTaskLater(UHC.getPlugin(), () -> {
            activateLocatorBar();
        }, ticks);
    }

    private void activateLocatorBar() {
        World world = Bukkit.getWorld("world");
        if (world != null) {
            world.setGameRule(GameRules.LOCATOR_BAR, true);
        }
        
        broadcast("");
        broadcast(vch.uhc.misc.Messages.LOCATOR_BAR_BORDER());
        broadcast(vch.uhc.misc.Messages.LOCATOR_BAR_ACTIVATED_TITLE());
        broadcast(vch.uhc.misc.Messages.LOCATOR_BAR_ACTIVATED_DESC());
        broadcast(vch.uhc.misc.Messages.LOCATOR_BAR_HIDDEN_INFO());
        broadcast(vch.uhc.misc.Messages.LOCATOR_BAR_BORDER());
        broadcast("");
    }

    public UHCTeam getWinningTeam() {
        return winningTeam;
    }

    private void broadcast(String message) {
        Component component = LegacyComponentSerializer.legacySection().deserialize(message);
        Bukkit.broadcast(component);
    }
}
