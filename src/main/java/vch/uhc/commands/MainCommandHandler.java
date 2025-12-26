package vch.uhc.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vch.uhc.UHC;

public class MainCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        switch (command.getName()) {
            case "uhc":
                return onUCHCommand(sender, args);
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand.");
                break;
        }

        return true;
        
    }

    public boolean onUCHCommand(CommandSender sender, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please specify a subcommand.");
            return false;
        }

        switch (args[0]) {
            case "start":
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
                    return false;
                }
                UHC.getPlugin().getUHCManager().start();
                sender.sendMessage(ChatColor.GREEN + "UHC started.");
                break;
            case "cancel":
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
                    return false;
                }
                UHC.getPlugin().getUHCManager().cancel();
                sender.sendMessage(ChatColor.RED + "UHC canceled.");
                break;
            case "reload":
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
                    return false;
                }
                sender.sendMessage(ChatColor.YELLOW + "Recargando plugin UHC...");
                UHC.getPlugin().getUHCManager().reload();
                sender.sendMessage(ChatColor.GREEN + "Plugin UHC recargado exitosamente.");
                break;
            case "info":
                sender.sendMessage(
                    ChatColor.YELLOW + "UHC Information:"
                    + ChatColor.GOLD + "\n- Game Status: " + UHC.getPlugin().getSettings().getGameStatus()
                    + ChatColor.GOLD + "\n- Team mode: " + UHC.getPlugin().getSettings().getTeamMode()
                    + ChatColor.GOLD + "\n- Team size: " + UHC.getPlugin().getSettings().getTeamSize()
                    + ChatColor.GOLD + "\n- Player lives: " + UHC.getPlugin().getSettings().getPlayerLives()
                    + ChatColor.GOLD + "\n- Max World Size: " + UHC.getPlugin().getSettings().getMaxWorldSize()
                    + ChatColor.GOLD + "\n- Min World Size: " + UHC.getPlugin().getSettings().getMinWorldSize()
                    + ChatColor.GOLD + "\n- Game Time: " + UHC.getPlugin().getSettings().getGameHours() + ":" + UHC.getPlugin().getSettings().getGameMinutes() + ":" + UHC.getPlugin().getSettings().getGameSeconds()
                    + ChatColor.GOLD + "\n- Agreement Time: " + UHC.getPlugin().getSettings().getAgreementHours() + ":" + UHC.getPlugin().getSettings().getAgreementMinutes() + ":" + UHC.getPlugin().getSettings().getAgreementSeconds()
                    + ChatColor.GOLD + "\n- Min World Border Time: " + UHC.getPlugin().getSettings().getMinWorldBorderHours() + ":" + UHC.getPlugin().getSettings().getMinWorldBorderMinutes() + ":" + UHC.getPlugin().getSettings().getMinWorldBorderSeconds()
                    + ChatColor.GOLD + "\n- Max In Game Teams Time Limit: " + UHC.getPlugin().getSettings().getMaxTeamInGameHours() + ":" + UHC.getPlugin().getSettings().getMaxTeamInGameMinutes() + ":" + UHC.getPlugin().getSettings().getMaxTeamInGameSeconds()
                    + ChatColor.GOLD + "\n- Teams: " + UHC.getPlugin().getTeamManager().getTeams().size()
                    + ChatColor.GOLD + "\n- Players: " + UHC.getPlugin().getPlayerManager().getPlayers().size()
                    + ChatColor.GOLD + "\n- Recipes: " 
                    + ChatColor.AQUA + "\n  " + UHC.getPlugin().getSettings().getItems().stream()
                        .map(item -> ChatColor.AQUA + "- " + item.getName() + ChatColor.AQUA + " (" + item.isEnabled() + ")")
                        .reduce((a, b) -> a + "\n  " + b).orElse("None")
                );
                break;
            case "join":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId()).setPlaying(true);
                    sender.sendMessage(ChatColor.GREEN + "You have joined the UHC.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Only players can join the UHC.");
                }
                break;
            case "leave":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId()).setPlaying(false);
                    sender.sendMessage(ChatColor.RED + "You have left the UHC.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Only players can leave the UHC.");
                }
                break;
            case "settings":
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
                    return false;
                }
                SettingsCommandHandler.onSettingsCommand(sender, args);
                break;
            case "menu":
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
                    return false;
                }
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    UHC.getPlugin().getMenuManager().openMainMenu(player);
                } else {
                    sender.sendMessage(ChatColor.RED + "Only players can open the menu.");
                }
                break;
            case "stats":
                sender.sendMessage(UHC.getPlugin().getStatsManager().getStatsReport());
                break;
            case "players":
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
                    return false;
                }
                PlayerCommandHandler.onPlayerCommand(sender, args);
                break;
            case "team":
                TeamCommandHandler.onTeamCommand(sender, args);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand.");
                break;
        }

        return true;

    }
    
}