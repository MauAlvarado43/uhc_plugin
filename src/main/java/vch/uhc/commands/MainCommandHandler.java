package vch.uhc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vch.uhc.UHC;
import vch.uhc.misc.Messages;
import vch.uhc.misc.enums.Permission;

/**
 * Main command handler for /uhc command. Handles subcommands like start,
 * cancel, reload, info, join, leave, settings, menu, stats, players, and team.
 */
public class MainCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Only handling "uhc" command
        if (command.getName().equalsIgnoreCase("uhc")) {
            return onUCHCommand(sender, args);
        }

        sender.sendMessage(Messages.COMMAND_UNKNOWN_SUBCOMMAND());
        return true;
    }

    /**
     * Handles the logic for /uhc subcommands.
     *
     * @param sender The sender of the command
     * @param args The arguments passed to the command
     * @return true if handled, false otherwise
     */
    public boolean onUCHCommand(CommandSender sender, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(Messages.COMMAND_SPECIFY_SUBCOMMAND());
            return false;
        }

        UHC plugin = UHC.getPlugin();
        if (plugin == null) {
            return false;
        }

        // Handle subcommands using modern switch rule syntax
        switch (args[0].toLowerCase()) {
            case "start" -> {
                if (!sender.hasPermission(Permission.ADMIN.getNode())) {
                    sender.sendMessage(Messages.NO_PERMISSION());
                    return false;
                }
                plugin.getUHCManager().start();
                sender.sendMessage(Messages.GAME_STARTED());
            }
            case "cancel" -> {
                if (!sender.hasPermission(Permission.ADMIN.getNode())) {
                    sender.sendMessage(Messages.NO_PERMISSION());
                    return false;
                }
                plugin.getUHCManager().cancel();
                sender.sendMessage(Messages.GAME_CANCELLED());
            }
            case "reload" -> {
                if (!sender.hasPermission(Permission.ADMIN.getNode())) {
                    sender.sendMessage(Messages.NO_PERMISSION());
                    return false;
                }
                sender.sendMessage(Messages.SETTINGS_LOADED());
                plugin.getUHCManager().reload();
            }
            case "info" -> {
                sender.sendMessage(Messages.INFO_HEADER());
                sender.sendMessage(Messages.INFO_GAME_STATE(plugin.getSettings().getGameState()));
                sender.sendMessage(Messages.INFO_TEAM_MODE(plugin.getSettings().getTeamMode()));
                sender.sendMessage(Messages.INFO_TEAM_SIZE(plugin.getSettings().getTeamSize()));
                sender.sendMessage(Messages.INFO_PLAYER_LIVES(plugin.getSettings().getPlayerLives()));
                sender.sendMessage(Messages.INFO_MAX_WORLD_SIZE(plugin.getSettings().getMaxWorldSize()));
                sender.sendMessage(Messages.INFO_MIN_WORLD_SIZE(plugin.getSettings().getMinWorldSize()));
                sender.sendMessage(Messages.INFO_GAME_TIME(
                        plugin.getSettings().getGameHours(),
                        plugin.getSettings().getGameMinutes(),
                        plugin.getSettings().getGameSeconds()
                ));
                sender.sendMessage(Messages.INFO_AGREEMENT_TIME(
                        plugin.getSettings().getAgreementHours(),
                        plugin.getSettings().getAgreementMinutes(),
                        plugin.getSettings().getAgreementSeconds()
                ));
                sender.sendMessage(Messages.INFO_MIN_BORDER_TIME(
                        plugin.getSettings().getMinWorldBorderHours(),
                        plugin.getSettings().getMinWorldBorderMinutes(),
                        plugin.getSettings().getMinWorldBorderSeconds()
                ));
                sender.sendMessage(Messages.INFO_MAX_INGAME_TEAMS_TIME(
                        plugin.getSettings().getMaxTeamInGameHours(),
                        plugin.getSettings().getMaxTeamInGameMinutes(),
                        plugin.getSettings().getMaxTeamInGameSeconds()
                ));
                sender.sendMessage(Messages.INFO_TEAMS_COUNT(plugin.getTeamManager().getTeams().size()));
                sender.sendMessage(Messages.INFO_PLAYERS_COUNT(plugin.getPlayerManager().getPlayers().size()));

                sender.sendMessage(Messages.INFO_RECIPES_HEADER());
                plugin.getSettings().getItems().forEach(item -> {
                    String status = item.isEnabled() ? Messages.INFO_ENABLED() : Messages.INFO_DISABLED();
                    sender.sendMessage(Messages.INFO_RECIPES_ITEM(item.getName(), status));
                });
            }
            case "join" -> {
                if (sender instanceof Player player) {
                    vch.uhc.models.UHCPlayer uhcPlayer = plugin.getPlayerManager().getPlayerByUUID(player.getUniqueId());
                    if (uhcPlayer != null) {
                        uhcPlayer.setPlaying(true);
                        sender.sendMessage(Messages.MAIN_JOINED_UHC());
                    }
                } else {
                    if (sender != null) {
                        sender.sendMessage(Messages.MAIN_PLAYERS_ONLY_JOIN());
                    }
                }
            }
            case "leave" -> {
                if (sender instanceof Player player) {
                    vch.uhc.models.UHCPlayer uhcPlayer = plugin.getPlayerManager().getPlayerByUUID(player.getUniqueId());
                    if (uhcPlayer != null) {
                        uhcPlayer.setPlaying(false);
                        sender.sendMessage(Messages.MAIN_LEFT_UHC());
                    }
                } else {
                    if (sender != null) {
                        sender.sendMessage(Messages.MAIN_PLAYERS_ONLY_LEAVE());
                    }
                }
            }
            case "settings" -> {
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(Messages.NO_PERMISSION());
                    return false;
                }
                SettingsCommandHandler.onSettingsCommand(sender, args);
            }
            case "menu" -> {
                if (sender instanceof Player player) {
                    plugin.getMenuManager().openMainMenu(player);
                } else {
                    if (sender != null) {
                        sender.sendMessage(Messages.MAIN_PLAYERS_ONLY_MENU());
                    }
                }
            }
            case "stats" ->
                sender.sendMessage(plugin.getStatsManager().getStatsReport());
            case "players" -> {
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(Messages.NO_PERMISSION());
                    return false;
                }
                PlayerCommandHandler.onPlayerCommand(sender, args);
            }
            case "team" ->
                TeamCommandHandler.onTeamCommand(sender, args);
            default -> {
                sender.sendMessage(Messages.COMMAND_UNKNOWN_SUBCOMMAND());
                return false;
            }
        }

        return true;
    }
}
