package vch.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vch.uhc.UHC;
import vch.uhc.models.Team;

public class TeamCommandHandler {

    public static boolean onTeamCommand(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Uso: /uhc team <create|add|remove|rename|leave|list> [args]");
            return false;
        }

        String subcommand = args[1];

        switch (subcommand) {
            case "list":
                if (UHC.getPlugin().getTeamManager().getTeams().isEmpty()) {
                    sender.sendMessage(ChatColor.YELLOW + "No hay equipos creados.");
                    return true;
                }
                sender.sendMessage(ChatColor.GOLD + "=== Equipos (Total: " + UHC.getPlugin().getTeamManager().getTeams().size() + ") ===");
                for (Team team : UHC.getPlugin().getTeamManager().getTeams()) {
                    sender.sendMessage(ChatColor.AQUA + team.getName() + ChatColor.GRAY + " (" + team.getMembers().size() + " miembros):");
                    for (vch.uhc.models.Player member : team.getMembers()) {
                        String prefix = member == team.getLeader() ? ChatColor.GOLD + "★ " : ChatColor.WHITE + "  - ";
                        sender.sendMessage(prefix + member.getName() + ChatColor.GRAY + " (UUID: " + member.getUuid() + ")");
                    }
                }
                return true;
                
            case "create":
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
                    return false;
                }
                if (UHC.getPlugin().getSettings().getTeamMode() != vch.uhc.misc.Settings.TeamMode.MANUAL) {
                    sender.sendMessage(ChatColor.RED + "Este comando solo está disponible en modo de equipos MANUAL.");
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Uso: /uhc team create <nombre>");
                    return false;
                }
                String teamName = args[2];
                Team newTeam = UHC.getPlugin().getTeamManager().createTeam(teamName);
                sender.sendMessage(ChatColor.GREEN + "Equipo '" + teamName + "' creado.");
                break;

            case "add":
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
                    return false;
                }
                if (UHC.getPlugin().getSettings().getTeamMode() != vch.uhc.misc.Settings.TeamMode.MANUAL) {
                    sender.sendMessage(ChatColor.RED + "Este comando solo está disponible en modo de equipos MANUAL.");
                    return false;
                }
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "Uso: /uhc team add <jugador> <equipo>");
                    return false;
                }
                String playerName = args[2];
                String targetTeam = args[3];
                Player bukkitPlayer = Bukkit.getPlayer(playerName);
                if (bukkitPlayer == null) {
                    sender.sendMessage(ChatColor.RED + "Jugador no encontrado.");
                    return false;
                }
                vch.uhc.models.Player uhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(bukkitPlayer.getUniqueId());
                if (uhcPlayer == null) {
                    sender.sendMessage(ChatColor.RED + "Jugador no está en el UHC.");
                    return false;
                }
                Team team = UHC.getPlugin().getTeamManager().getTeamByName(targetTeam);
                if (team == null) {
                    sender.sendMessage(ChatColor.RED + "Equipo no encontrado.");
                    return false;
                }
                UHC.getPlugin().getTeamManager().addPlayer(team, uhcPlayer);
                sender.sendMessage(ChatColor.GREEN + "Jugador " + playerName + " agregado al equipo '" + targetTeam + "'.");
                break;

            case "remove":
                if (!sender.hasPermission("uhc.admin")) {
                    sender.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
                    return false;
                }
                if (UHC.getPlugin().getSettings().getTeamMode() != vch.uhc.misc.Settings.TeamMode.MANUAL) {
                    sender.sendMessage(ChatColor.RED + "Este comando solo está disponible en modo de equipos MANUAL.");
                    return false;
                }
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "Uso: /uhc team remove <jugador> <equipo>");
                    return false;
                }
                String removePlayerName = args[2];
                String removeTeamName = args[3];
                Player removeBukkitPlayer = Bukkit.getPlayer(removePlayerName);
                if (removeBukkitPlayer == null) {
                    sender.sendMessage(ChatColor.RED + "Jugador no encontrado.");
                    return false;
                }
                vch.uhc.models.Player removeUhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(removeBukkitPlayer.getUniqueId());
                if (removeUhcPlayer == null) {
                    sender.sendMessage(ChatColor.RED + "Jugador no está en el UHC.");
                    return false;
                }
                Team removeTeam = UHC.getPlugin().getTeamManager().getTeamByName(removeTeamName);
                if (removeTeam == null) {
                    sender.sendMessage(ChatColor.RED + "Equipo no encontrado.");
                    return false;
                }
                UHC.getPlugin().getTeamManager().removePlayer(removeTeam, removeUhcPlayer);
                sender.sendMessage(ChatColor.GREEN + "Jugador " + removePlayerName + " removido del equipo '" + removeTeamName + "'.");
                break;

            case "rename":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Solo jugadores pueden usar este comando.");
                    return false;
                }
                Player renamePlayer = (Player) sender;
                vch.uhc.models.Player renameUhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(renamePlayer.getUniqueId());
                if (renameUhcPlayer == null || renameUhcPlayer.getTeam() == null) {
                    sender.sendMessage(ChatColor.RED + "No estás en ningún equipo.");
                    return false;
                }
                Team playerTeam = renameUhcPlayer.getTeam();
                if (playerTeam.getLeader() != renameUhcPlayer) {
                    sender.sendMessage(ChatColor.RED + "Solo el líder del equipo puede renombrarlo.");
                    return false;
                }
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Uso: /uhc team rename <nuevoNombre>");
                    sender.sendMessage(ChatColor.GRAY + "Usa & para colores. Ejemplo: &aVerde &b&lAzul Negrita");
                    return false;
                }
                String oldTeamName = playerTeam.getName();
                StringBuilder newTeamNameBuilder = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    if (i > 2) newTeamNameBuilder.append(" ");
                    newTeamNameBuilder.append(args[i]);
                }
                String newTeamName = ChatColor.translateAlternateColorCodes('&', newTeamNameBuilder.toString());
                playerTeam.setName(newTeamName);
                sender.sendMessage(ChatColor.GREEN + "Equipo renombrado de '" + oldTeamName + "' a '" + newTeamName + "'.");
                break;

            case "leave":
                if (UHC.getPlugin().getSettings().getTeamMode() != vch.uhc.misc.Settings.TeamMode.MANUAL) {
                    sender.sendMessage(ChatColor.RED + "Este comando solo está disponible en modo de equipos MANUAL.");
                    return false;
                }
                if (UHC.getPlugin().getSettings().getGameStatus() == vch.uhc.misc.Settings.GameStatus.IN_PROGRESS) {
                    sender.sendMessage(ChatColor.RED + "No puedes salir de un equipo mientras el UHC está en progreso.");
                    return false;
                }
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "Solo jugadores pueden usar este comando.");
                    return false;
                }
                Player leavePlayer = (Player) sender;
                vch.uhc.models.Player leaveUhcPlayer = UHC.getPlugin().getPlayerManager().getPlayerByUUID(leavePlayer.getUniqueId());
                if (leaveUhcPlayer == null || leaveUhcPlayer.getTeam() == null) {
                    sender.sendMessage(ChatColor.RED + "No estás en ningún equipo.");
                    return false;
                }
                Team leaveTeam = leaveUhcPlayer.getTeam();
                String leaveTeamName = leaveTeam.getName();
                UHC.getPlugin().getTeamManager().removePlayer(leaveTeam, leaveUhcPlayer);
                sender.sendMessage(ChatColor.GREEN + "Has salido del equipo '" + leaveTeamName + "'.");
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Subcomando desconocido. Usa: create, add, remove, rename, leave, list");
                return false;
        }

        return true;

    }

}