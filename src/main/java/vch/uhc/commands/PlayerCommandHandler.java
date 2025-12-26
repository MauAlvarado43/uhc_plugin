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
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "Uso: /uhc players setLives <jugador> <vidas>");
                    return false;
                }
                String playerName = args[2];
                try {
                    int lives = Integer.parseInt(args[3]);
                    Player player = Bukkit.getPlayer(playerName);
                    if (player == null) {
                        sender.sendMessage(ChatColor.RED + "Jugador " + playerName + " no encontrado.");
                        return false;
                    }
                    vch.uhc.models.Player uhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId());
                    if (uhcPlayer == null) {
                        sender.sendMessage(ChatColor.RED + "Jugador " + playerName + " no está en el UHC.");
                        return false;
                    }
                    uhcPlayer.setLives(lives);
                    sender.sendMessage(ChatColor.GREEN + "Vidas de " + playerName + " establecidas a " + lives);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Número de vidas inválido: " + args[3]);
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Error al establecer vidas para " + playerName + ": " + e.getMessage());
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