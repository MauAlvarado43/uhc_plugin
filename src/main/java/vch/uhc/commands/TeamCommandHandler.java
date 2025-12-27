package vch.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vch.uhc.UHC;
import vch.uhc.misc.Messages;
import vch.uhc.models.UHCTeam;
import vch.uhc.misc.enums.TeamMode;
import vch.uhc.misc.enums.GameState;

/**
 * Handler for team management subcommands under /uhc team. Includes
 * functionality for creating, adding, removing, renaming, leaving, and listing
 * teams.
 */
public class TeamCommandHandler {

    /**
     * Executes team-related subcommands.
     *
     * @param sender The command sender
     * @param args The command arguments
     * @return true if handled, false otherwise
     */
    public static boolean onTeamCommand(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(Messages.TEAM_USAGE());
            return false;
        }

        String subcommand = args[1].toLowerCase();

        switch (subcommand) {
            case "list" -> {
                // List all teams and their members
                if (UHC.getPlugin().getTeamManager().getTeams().isEmpty()) {
                    sender.sendMessage(Messages.TEAM_NONE_CREATED());
                    return true;
                }
                sender.sendMessage(Messages.TEAM_LIST_HEADER());
                for (UHCTeam team : UHC.getPlugin().getTeamManager().getTeams()) {
                    sender.sendMessage(Messages.TEAM_LIST_ENTRY(team.getName(), team.getMembers().size()));
                    for (vch.uhc.models.UHCPlayer member : team.getMembers()) {
                        String prefix = member == team.getLeader() ? "\u00a76★ " : "\u00a7f  - ";
                        sender.sendMessage(prefix + member.getName());
                    }
                }
                sender.sendMessage(Messages.TEAM_LIST_FOOTER());
            }
            case "create" -> {
                // Create a new team (Admins only, manual mode)
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(Messages.NO_PERMISSION());
                    return false;
                }
                if (UHC.getPlugin().getSettings().getTeamMode() != TeamMode.MANUAL) {
                    sender.sendMessage(Messages.TEAM_MANUAL_ONLY());
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(Messages.TEAM_CREATE_USAGE());
                    return false;
                }
                String teamName = args[2];
                if (UHC.getPlugin().getTeamManager().getTeamByName(teamName) != null) {
                    sender.sendMessage(Messages.TEAM_NAME_TAKEN());
                    return false;
                }
                UHC.getPlugin().getTeamManager().createTeam(teamName);
                sender.sendMessage(Messages.TEAM_CREATED(teamName));
            }
            case "add" -> {
                // Add a player to a team (Admins only, manual mode)
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(Messages.NO_PERMISSION());
                    return false;
                }
                if (UHC.getPlugin().getSettings().getTeamMode() != TeamMode.MANUAL) {
                    sender.sendMessage(Messages.TEAM_MANUAL_ONLY());
                    return false;
                }
                if (args.length < 4) {
                    sender.sendMessage(Messages.TEAM_ADD_USAGE());
                    return false;
                }
                String addPlayerName = args[2];
                String targetTeamName = args[3];
                Player addBukkitPlayer = Bukkit.getPlayer(addPlayerName);
                if (addBukkitPlayer == null) {
                    sender.sendMessage(Messages.TEAM_PLAYER_NOT_FOUND());
                    return false;
                }
                vch.uhc.models.UHCPlayer addUhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(addBukkitPlayer.getUniqueId());
                if (addUhcPlayer == null) {
                    sender.sendMessage(Messages.TEAM_PLAYER_NOT_FOUND());
                    return false;
                }
                UHCTeam addTeam = UHC.getPlugin().getTeamManager().getTeamByName(targetTeamName);
                if (addTeam == null) {
                    sender.sendMessage(Messages.TEAM_NOT_FOUND(targetTeamName));
                    return false;
                }
                if (addTeam.getMembers().size() >= UHC.getPlugin().getSettings().getTeamSize()) {
                    sender.sendMessage(Messages.TEAM_FULL(UHC.getPlugin().getSettings().getTeamSize()));
                    return false;
                }
                UHC.getPlugin().getTeamManager().addPlayer(addTeam, addUhcPlayer);
                sender.sendMessage(Messages.TEAM_PLAYER_ADDED(addPlayerName, targetTeamName));
                addBukkitPlayer.sendMessage(Messages.TEAM_YOU_WERE_ADDED(targetTeamName));
            }
            case "remove" -> {
                // Remove a player from a team (Admins only, manual mode)
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(Messages.NO_PERMISSION());
                    return false;
                }
                if (UHC.getPlugin().getSettings().getTeamMode() != TeamMode.MANUAL) {
                    sender.sendMessage(Messages.TEAM_MANUAL_ONLY());
                    return false;
                }
                if (args.length < 4) {
                    sender.sendMessage(Messages.TEAM_REMOVE_USAGE());
                    return false;
                }
                String removePlayerName = args[2];
                String removeTeamName = args[3];
                Player removeBukkitPlayer = Bukkit.getPlayer(removePlayerName);
                if (removeBukkitPlayer == null) {
                    sender.sendMessage(Messages.TEAM_PLAYER_NOT_FOUND());
                    return false;
                }
                vch.uhc.models.UHCPlayer removeUhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(removeBukkitPlayer.getUniqueId());
                if (removeUhcPlayer == null) {
                    sender.sendMessage(Messages.TEAM_PLAYER_NOT_FOUND());
                    return false;
                }
                UHCTeam removeTeam = UHC.getPlugin().getTeamManager().getTeamByName(removeTeamName);
                if (removeTeam == null) {
                    sender.sendMessage(Messages.TEAM_NOT_FOUND(removeTeamName));
                    return false;
                }
                UHC.getPlugin().getTeamManager().removePlayer(removeTeam, removeUhcPlayer);
                sender.sendMessage(Messages.TEAM_PLAYER_REMOVED(removePlayerName, removeTeamName));
                removeBukkitPlayer.sendMessage(Messages.TEAM_YOU_WERE_REMOVED(removeTeamName));
            }
            case "rename" -> {
                // Rename your own team (Leader only)
                if (!(sender instanceof Player renamePlayer)) {
                    if (sender != null) {
                        sender.sendMessage(Messages.TEAM_PLAYERS_ONLY());
                    }
                    return false;
                }
                vch.uhc.models.UHCPlayer renameUhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(renamePlayer.getUniqueId());
                UHCTeam playerRenameTeam = renameUhcPlayer != null ? renameUhcPlayer.getTeam() : null;
                if (renameUhcPlayer == null || playerRenameTeam == null) {
                    sender.sendMessage(Messages.TEAM_NOT_IN_TEAM());
                    return false;
                }
                if (playerRenameTeam.getLeader() != renameUhcPlayer) {
                    sender.sendMessage(Messages.TEAM_LEADER_ONLY());
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(Messages.TEAM_RENAME_USAGE());
                    return false;
                }
                String oldName = playerRenameTeam.getName();
                StringBuilder newNameBuilder = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    if (i > 2) {
                        newNameBuilder.append(" ");
                    }
                    newNameBuilder.append(args[i]);
                }
                String newName = net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand().serialize(
                    net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand().deserialize(newNameBuilder.toString())
                );
                playerRenameTeam.setName(newName);
                sender.sendMessage(Messages.TEAM_RENAMED(oldName, newName));
                playerRenameTeam.getMembers().forEach(m -> {
                    Player p = Bukkit.getPlayer(m.getUuid());
                    if (p != null) {
                        p.sendMessage(Messages.TEAM_YOU_WERE_RENAMED(newName));
                    }
                });
            }
            case "leave" -> {
                // Leave your current team (Manual mode before game start)
                if (UHC.getPlugin().getSettings().getTeamMode() != TeamMode.MANUAL) {
                    sender.sendMessage(Messages.TEAM_MANUAL_ONLY());
                    return false;
                }
                if (UHC.getPlugin().getSettings().getGameState() == GameState.IN_PROGRESS) {
                    sender.sendMessage(Messages.TEAM_LEAVE_IN_PROGRESS());
                    return false;
                }
                if (!(sender instanceof Player leavePlayer)) {
                    if (sender != null) {
                        sender.sendMessage(Messages.TEAM_PLAYERS_ONLY());
                    }
                    return false;
                }
                vch.uhc.models.UHCPlayer leaveUhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(leavePlayer.getUniqueId());
                if (leaveUhcPlayer == null || leaveUhcPlayer.getTeam() == null) {
                    sender.sendMessage(Messages.TEAM_NOT_IN_TEAM());
                    return false;
                }
                UHCTeam leaveTeam = leaveUhcPlayer.getTeam();
                String leaveTeamName = leaveTeam.getName();
                UHC.getPlugin().getTeamManager().removePlayer(leaveTeam, leaveUhcPlayer);
                sender.sendMessage(Messages.TEAM_YOU_LEFT(leaveTeamName));
                leaveTeam.getMembers().forEach(m -> {
                    Player p = Bukkit.getPlayer(m.getUuid());
                    if (p != null) {
                        p.sendMessage(Messages.TEAM_PLAYER_LEFT(leavePlayer.getName()));
                    }
                });
            }
            default -> {
                sender.sendMessage(Messages.TEAM_UNKNOWN_SUBCOMMAND());
                return false;
            }
        }

        return true;
    }
}
