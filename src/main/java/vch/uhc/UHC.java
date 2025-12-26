package vch.uhc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.neznamy.tab.api.TabAPI;
import vch.uhc.commands.AFKCommandHandler;
import vch.uhc.commands.MainCommandHandler;
import vch.uhc.expansions.PlayerExpansion;
import vch.uhc.expansions.PlayerHealthExpansion;
import vch.uhc.expansions.PlayerLivesExpansion;
import vch.uhc.expansions.TeamExpansion;
import vch.uhc.listeners.AdvancementListener;
import vch.uhc.listeners.AFKListener;
import vch.uhc.listeners.ChatListener;
import vch.uhc.listeners.EntityDeathListener;
import vch.uhc.listeners.FoodListener;
import vch.uhc.listeners.MenuListener;
import vch.uhc.listeners.PlayerDamageListener;
import vch.uhc.listeners.PlayerDeathListener;
import vch.uhc.listeners.PlayerJoinListener;
import vch.uhc.managers.AFKManager;
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

public class UHC extends JavaPlugin {

    private TabAPI tabAPI;

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

        new ChatListener().register();
        new EntityDeathListener().register();
        new FoodListener().register();
        new PlayerDeathListener().register();
        new PlayerJoinListener().register();
        new AFKListener().register();
        new PlayerDamageListener().register();
        new MenuListener().register();
        new AdvancementListener().register();

        getCommand("uhc").setTabCompleter(new CommandCompleter());
        getCommand("uhc").setExecutor(new MainCommandHandler());
        getCommand("afk").setExecutor(new AFKCommandHandler());

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
        String version = this.getDescription().getVersion();
        console.sendMessage("\n"
                + ChatColor.GOLD + "==========================" + ChatColor.RESET + "\n"
                + ChatColor.GREEN + "    UHC " + version + ChatColor.RESET + "\n"
                + ChatColor.AQUA
                + "        /\\\n"
                + "       /  \\\n"
                + "      /" + ChatColor.WHITE + " /\\ " + ChatColor.AQUA + "\\\n"
                + "     /" + ChatColor.WHITE + "_/__\\_" + ChatColor.AQUA + "\\\n"
                + "     \\      /\n"
                + "      \\    /\n"
                + "       \\  /\n"
                + "        \\/" + ChatColor.RESET + "\n"
                + ChatColor.GOLD + "==========================" + ChatColor.RESET + "\n"
        );

    }

    public void onDisable() {
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

}