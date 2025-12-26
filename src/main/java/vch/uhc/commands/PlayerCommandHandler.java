package vch.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vch.uhc.UHC;

public class PlayerCommandHandler {

    public static boolean onPlayerCommand(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Please specify a player subcommand.");
            return false;
        }

        switch (args[1]) {
            case "list":
                sender.sendMessage(
                        ChatColor.YELLOW + "UHC Players:"
                        + ChatColor.AQUA + "\n  " + UHC.getPlugin().getPlayerManager().getPlayers().stream()
                                .map(item -> ChatColor.AQUA + "- " + ChatColor.GOLD + item.getName() + ChatColor.AQUA + " (" + item.getLives() + " lives)")
                                .reduce((a, b) -> a + "\n  " + b).orElse("None")
                );
                break;
            case "setLives":
                String playerName = args[2];
                try {
                    int lives = Integer.parseInt(args[3]);
                    Player player = Bukkit.getPlayer(playerName);
                    UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId()).setLives(lives);
                    sender.sendMessage(ChatColor.GREEN + "Set lives for " + playerName + " to " + lives);
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Error setting lives for " + playerName);
                }
                break;
            case "setHealth":
                String playerHealthName = args[2];
                try {
                    int health = Integer.parseInt(args[3]);
                    Player player = Bukkit.getPlayer(playerHealthName);
                    AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
                    attribute.setBaseValue(health);
                    sender.sendMessage(ChatColor.GREEN + "Set health for " + playerHealthName + " to " + health);
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Error setting health for " + playerHealthName);
                }
                break;
            case "revive":
                String playerReviveName = args[2];
                try {
                    Player player = Bukkit.getPlayer(playerReviveName);
                    UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId()).setLives(1);
                    player.teleport(UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId()).getSpawn());
                    sender.sendMessage(ChatColor.GREEN + "Revived " + playerReviveName);
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Error reviving " + playerReviveName);
                }
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand.");
                break;
        }

        return true;

    }
    
}