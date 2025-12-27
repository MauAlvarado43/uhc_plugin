package vch.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vch.uhc.UHC;
import vch.uhc.misc.Messages;

/**
 * Handler for player management subcommands under /uhc players. Includes
 * functionality for listing players, setting lives, health, and reviving
 * players.
 */
public class PlayerCommandHandler {

    /**
     * Executes player-related subcommands.
     *
     * @param sender The command sender
     * @param args The command arguments
     * @return true if handled, false otherwise
     */
    public static boolean onPlayerCommand(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(Messages.PLAYER_SPECIFY_SUBCOMMAND());
            return false;
        }

        switch (args[1].toLowerCase()) {
            case "list" -> {
                // List all UHC players and their current lives
                sender.sendMessage(Messages.PLAYER_LIST_HEADER());
                if (UHC.getPlugin().getPlayerManager().getPlayers().isEmpty()) {
                    sender.sendMessage(Messages.PLAYER_LIST_NONE());
                } else {
                    UHC.getPlugin().getPlayerManager().getPlayers().forEach(item
                            -> sender.sendMessage(Messages.PLAYER_LIST_ITEM(item.getName(), item.getLives()))
                    );
                }
            }
            case "setlives" -> {
                // Set the number of lives for a specific player
                if (args.length < 4) {
                    sender.sendMessage(Messages.PLAYER_SETLIVES_USAGE());
                    return false;
                }
                String playerName = args[2];
                try {
                    int lives = Integer.parseInt(args[3]);
                    Player player = Bukkit.getPlayer(playerName);
                    if (player == null) {
                        sender.sendMessage(Messages.TEAM_PLAYER_NOT_FOUND());
                        return false;
                    }
                    vch.uhc.models.UHCPlayer uhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId());
                    if (uhcPlayer == null) {
                        sender.sendMessage(Messages.PLAYER_LIVES_ERROR(playerName));
                        return false;
                    }
                    uhcPlayer.setLives(lives);
                    sender.sendMessage(Messages.PLAYER_LIVES_SET(playerName, lives));
                } catch (NumberFormatException e) {
                    sender.sendMessage(Messages.SETTINGS_PLAYER_LIVES_INVALID());
                } catch (NullPointerException e) {
                    sender.sendMessage(Messages.PLAYER_LIVES_ERROR(playerName));
                }
            }
            case "sethealth" -> {
                // Set the maximum health for a specific player
                if (args.length < 4) {
                    sender.sendMessage(Messages.PLAYER_SETHEALTH_USAGE());
                    return false;
                }
                String playerHealthName = args[2];
                try {
                    int health = Integer.parseInt(args[3]);
                    Player player = Bukkit.getPlayer(playerHealthName);
                    if (player == null) {
                        sender.sendMessage(Messages.TEAM_PLAYER_NOT_FOUND());
                        return false;
                    }
                    AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
                    if (attribute != null) {
                        attribute.setBaseValue(health);
                        player.setHealth(health); // Also set current health to max
                        sender.sendMessage(Messages.PLAYER_HEALTH_SET(playerHealthName, health));
                    }
                } catch (NumberFormatException | NullPointerException e) {
                    sender.sendMessage(Messages.PLAYER_HEALTH_ERROR(playerHealthName));
                }
            }
            case "revive" -> {
                // Revive a player and teleport them to their spawn
                if (args.length < 3) {
                    sender.sendMessage(Messages.PLAYER_REVIVE_USAGE());
                    return false;
                }
                String playerReviveName = args[2];
                try {
                    Player player = Bukkit.getPlayer(playerReviveName);
                    if (player == null) {
                        sender.sendMessage(Messages.TEAM_PLAYER_NOT_FOUND());
                        return false;
                    }
                    vch.uhc.models.UHCPlayer uhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(player.getUniqueId());
                    if (uhcPlayer != null) {
                        uhcPlayer.setLives(1);
                        player.teleport(uhcPlayer.getSpawn());
                        sender.sendMessage(Messages.PLAYER_REVIVED(playerReviveName));
                    }
                } catch (NullPointerException e) {
                    sender.sendMessage(Messages.PLAYER_REVIVE_ERROR(playerReviveName));
                }
            }
            default ->
                sender.sendMessage(Messages.PLAYER_UNKNOWN_SUBCOMMAND());
        }

        return true;
    }
}
