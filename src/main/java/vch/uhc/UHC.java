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
import vch.uhc.listeners.chat.ChatListener;
import vch.uhc.listeners.game.EntityDeathListener;
import vch.uhc.listeners.game.PvPListener;
import vch.uhc.listeners.player.AFKListener;
import vch.uhc.listeners.player.AdvancementListener;
import vch.uhc.listeners.player.FoodListener;
import vch.uhc.listeners.player.PlayerDamageListener;
import vch.uhc.listeners.player.PlayerDeathListener;
import vch.uhc.listeners.player.PlayerInteractListener;
import vch.uhc.listeners.player.PlayerJoinListener;
import vch.uhc.listeners.player.SkinRevealListener;
import vch.uhc.listeners.ui.MenuListener;
import vch.uhc.managers.game.BackupManager;
import vch.uhc.managers.game.GameModeManager;
import vch.uhc.managers.game.GameTimerManager;
import vch.uhc.managers.game.UHCManager;
import vch.uhc.managers.game.WorldManager;
import vch.uhc.managers.player.AFKManager;
import vch.uhc.managers.player.CombatTracker;
import vch.uhc.managers.player.PlayerManager;
import vch.uhc.managers.player.SkinManager;
import vch.uhc.managers.player.StatsManager;
import vch.uhc.managers.player.TeamManager;
import vch.uhc.managers.ui.MenuManager;
import vch.uhc.managers.ui.ScoreboardManager;
import vch.uhc.misc.CommandCompleter;
import vch.uhc.misc.LanguageManager;
import vch.uhc.misc.Settings;
import vch.uhc.misc.enums.GameState;

public class UHC extends JavaPlugin {

    private Settings settings;
    private LanguageManager languageManager;

    private PlayerManager playerManager;
    private TeamManager teamManager;
    private ScoreboardManager scoreboardManager;
    private WorldManager worldManager;
    private GameTimerManager gameTimerManager;
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
        settings.load();
        languageManager.setLanguage(settings.getLanguage());

        playerManager = new PlayerManager();
        teamManager = new TeamManager();
        scoreboardManager = new ScoreboardManager();
        worldManager = new WorldManager();
        gameTimerManager = new GameTimerManager();
        uhcManager = new UHCManager();
        skinManager = new SkinManager();
        statsManager = new StatsManager();
        afkManager = new AFKManager();
        gameModeManager = new GameModeManager();
        menuManager = new MenuManager();
        combatTracker = new CombatTracker();
        backupManager = new BackupManager();

        // Register Player Listeners
        new PlayerJoinListener().register();
        new PlayerDeathListener().register();
        new PlayerDamageListener().register();
        new PlayerInteractListener().register();
        new FoodListener().register();
        new AdvancementListener().register();
        new AFKListener().register();
        new SkinRevealListener().register();

        // Register Game Listeners
        new EntityDeathListener().register();
        new PvPListener().register();

        // Register UI Listeners
        new MenuListener().register();

        // Register Chat Listeners
        new ChatListener().register();

        Objects.requireNonNull(getCommand("uhc")).setTabCompleter(new CommandCompleter());
        Objects.requireNonNull(getCommand("uhc")).setExecutor(new MainCommandHandler());

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TeamExpansion().register();
            new PlayerExpansion().register();
            new PlayerLivesExpansion().register();
            new PlayerHealthExpansion().register();
            getLogger().info("Expansions registered!");
        } else {
            getLogger().warning("PlaceholderAPI not found, team system may not work properly.");
        }

        ConsoleCommandSender console = Bukkit.getConsoleSender();
        String version = this.getPluginMeta().getVersion();
        String brand = settings != null ? settings.getBrandName() : "UHC";

        console.sendMessage("\n"
                + "§6  _    _ _    _  _____   §e _   _                   _ \n"
                + "§6 | |  | | |  | |/ ____|  §e| \\ | |                 | |\n"
                + "§6 | |  | | |__| | |       §e|  \\| | ___  _ __   __ _| |\n"
                + "§6 | |  | |  __  | |       §e| . ` |/ _ \\| '_ \\ / _` | |\n"
                + "§6 | |__| | |  | | |____   §e| |\\  | (_) | |_) | (_| | |\n"
                + "§6  \\____/|_|  |_|\\_____|  §e|_| \\_|\\___/| .__/ \\__,_|_|\n"
                + "§e                             | |              \n"
                + "§e                             |_|              \n"
                + "§f  » §bVersion: §f" + version + "\n"
                + "§f  » §bEvent: §f" + brand + "\n"
                + "§f  » §bStatus: §aREADY §f(Managers Initialized)\n"
                + "§6  ------------------------------------------"
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

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public GameTimerManager getGameTimerManager() {
        return gameTimerManager;
    }
}
