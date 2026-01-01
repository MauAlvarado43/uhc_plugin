package vch.uhc;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import vch.uhc.commands.MainCommandHandler;
import vch.uhc.expansions.PlayerExpansion;
import vch.uhc.expansions.PlayerHealthExpansion;
import vch.uhc.expansions.PlayerLivesExpansion;
import vch.uhc.expansions.TeamExpansion;
import vch.uhc.listeners.AFKListener;
import vch.uhc.listeners.AdvancementListener;
import vch.uhc.listeners.ChatListener;
import vch.uhc.listeners.EntityDeathListener;
import vch.uhc.listeners.FoodListener;
import vch.uhc.listeners.MenuListener;
import vch.uhc.listeners.PlayerDamageListener;
import vch.uhc.listeners.PlayerDeathListener;
import vch.uhc.listeners.PlayerJoinListener;
import vch.uhc.managers.AFKManager;
import vch.uhc.managers.BackupManager;
import vch.uhc.managers.CombatTracker;
import vch.uhc.managers.GameModeManager;
import vch.uhc.managers.MenuManager;
import vch.uhc.managers.PlayerManager;
import vch.uhc.managers.SkinManager;
import vch.uhc.managers.StatsManager;
import vch.uhc.managers.TeamManager;
import vch.uhc.managers.UHCManager;
import vch.uhc.misc.CommandCompleter;
import vch.uhc.misc.LanguageManager;
import vch.uhc.misc.Settings;
import vch.uhc.misc.enums.GameState;

public class UHC extends JavaPlugin {

    private Settings settings;
    private LanguageManager languageManager;

    private PlayerManager playerManager;
    private TeamManager teamManager;
    private UHCManager uhcManager;
    private SkinManager skinManager;
    private StatsManager statsManager;
    private AFKManager afkManager;
    private GameModeManager gameModeManager;
    private MenuManager menuManager;
    private CombatTracker combatTracker;
    private BackupManager backupManager;

    @Override
    public void onEnable() {

        languageManager = new LanguageManager();
        settings = new Settings();

        playerManager = new PlayerManager();
        teamManager = new TeamManager();
        uhcManager = new UHCManager();
        skinManager = new SkinManager();
        statsManager = new StatsManager();
        afkManager = new AFKManager();
        gameModeManager = new GameModeManager();
        menuManager = new MenuManager();
        combatTracker = new CombatTracker();
        backupManager = new BackupManager();

        new ChatListener().register();
        new EntityDeathListener().register();
        new FoodListener().register();
        new PlayerDeathListener().register();
        new PlayerJoinListener().register();
        new AFKListener().register();
        new PlayerDamageListener().register();
        new MenuListener().register();
        new AdvancementListener().register();

        Objects.requireNonNull(getCommand("uhc")).setTabCompleter(new CommandCompleter());
        Objects.requireNonNull(getCommand("uhc")).setExecutor(new MainCommandHandler());

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TeamExpansion().register();
            new PlayerExpansion().register();
            new PlayerLivesExpansion().register();
            new PlayerHealthExpansion().register();
            System.out.println("Expansions registered!");
        } else {
            getLogger().warning("PlaceholderAPI not found, team system may not work properly.");
        }

        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String version = this.getPluginMeta().getVersion();
        console.sendMessage("""

                \u00a76==========================\u00a7r
                \u00a7a    UHC %s\u00a7r
                \u00a7b        /\\
                       /  \\
                      /\u00a7f /\\ \u00a7b\\
                     /\u00a7f_/__\\_\u00a7b\\
                     \\      /
                      \\    /
                       \\  /
                        \\/\u00a7r
                """.formatted(version)
                + "\u00a76==========================" + "\u00a7r" + "\n"
        );
        
        // Auto-load game state if backup exists
        if (backupManager.hasBackup()) {
            getLogger().info("Found existing game state backup. Loading...");
            if (backupManager.loadGameState()) {
                getLogger().info("Game state restored successfully");
            } else {
                getLogger().warning("Failed to restore game state from backup");
            }
        }

    }

    @Override
    public void onDisable() {
        // Save game state on shutdown if game is in progress or paused
        if (settings != null && uhcManager != null && backupManager != null) {
            GameState state = settings.getGameState();
            if (state == GameState.IN_PROGRESS || state == GameState.PAUSED) {
                backupManager.saveGameState();
                getLogger().info("Game state saved due to server shutdown");
            }
        }
    }

    public static UHC getPlugin() {
        return JavaPlugin.getPlugin(UHC.class);
    }

    public Settings getSettings() {
        return settings;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public UHCManager getUHCManager() {
        return uhcManager;
    }

    public SkinManager getSkinManager() {
        return skinManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public AFKManager getAFKManager() {
        return afkManager;
    }

    public GameModeManager getGameModeManager() {
        return gameModeManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public CombatTracker getCombatTracker() {
        return combatTracker;
    }

    public BackupManager getBackupManager() {
        return backupManager;
    }

}
