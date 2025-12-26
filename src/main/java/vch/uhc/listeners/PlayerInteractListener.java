package vch.uhc.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import vch.uhc.UHC;
import vch.uhc.misc.Settings;
import vch.uhc.models.Team;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }


        Settings settings = UHC.getPlugin().getSettings();
        if (settings.getTeamMode() != Settings.TeamMode.IN_GAME) {
            return;
        }


        if (settings.getGameStatus() != Settings.GameStatus.IN_PROGRESS || 
            UHC.getPlugin().getUHCManager().areTeamsFormed()) {
            return;
        }

        Player clicker = event.getPlayer();
        Player target = (Player) event.getRightClicked();

        vch.uhc.models.Player p1 = UHC.getPlugin().getPlayerManager().getPlayerByUUID(clicker.getUniqueId());
        vch.uhc.models.Player p2 = UHC.getPlugin().getPlayerManager().getPlayerByUUID(target.getUniqueId());

        if (p1 == null || p2 == null) {
            return;
        }

        boolean canJoin = UHC.getPlugin().getTeamManager().canPlayersJoinTeam(p1, p2);
        if (!canJoin) {
            clicker.sendMessage(ChatColor.RED + "No puedes formar equipo con " + target.getName());
            return;
        }

        Team teamP1 = p1.getTeam();
        Team teamP2 = p2.getTeam();

        if (teamP1 != null && teamP2 != null) {

            int totalMembers = teamP1.getMembers().size() + teamP2.getMembers().size();
            if (totalMembers > settings.getTeamSize()) {
                clicker.sendMessage(ChatColor.RED + "Los equipos combinados exceden el tamaño máximo (" + settings.getTeamSize() + ")");
                return;
            }
            for (vch.uhc.models.Player member : teamP2.getMembers()) {
                UHC.getPlugin().getTeamManager().removePlayer(teamP2, member);
                UHC.getPlugin().getTeamManager().addPlayer(teamP1, member);
                Player bukkitMember = member.getBukkitPlayer();
                if (bukkitMember != null) {
                    bukkitMember.sendMessage(ChatColor.GREEN + "¡Equipos fusionados! Ahora eres parte de " + teamP1.getName());
                }
            }
            UHC.getPlugin().getTeamManager().deleteTeam(teamP2);
            clicker.sendMessage(ChatColor.GREEN + "¡Equipo fusionado con " + target.getName() + "!");
        } else if (teamP1 != null) {

            if (teamP1.getMembers().size() >= settings.getTeamSize()) {
                clicker.sendMessage(ChatColor.RED + "Tu equipo ya está lleno");
                return;
            }
            UHC.getPlugin().getTeamManager().addPlayer(teamP1, p2);
            clicker.sendMessage(ChatColor.GREEN + target.getName() + " se unió a tu equipo!");
            target.sendMessage(ChatColor.GREEN + "¡Te uniste al equipo de " + clicker.getName() + "!");
        } else if (teamP2 != null) {

            if (teamP2.getMembers().size() >= settings.getTeamSize()) {
                clicker.sendMessage(ChatColor.RED + "El equipo de " + target.getName() + " ya está lleno");
                return;
            }
            UHC.getPlugin().getTeamManager().addPlayer(teamP2, p1);
            clicker.sendMessage(ChatColor.GREEN + "¡Te uniste al equipo de " + target.getName() + "!");
            target.sendMessage(ChatColor.GREEN + clicker.getName() + " se unió a tu equipo!");
        } else {

            Team newTeam = UHC.getPlugin().getTeamManager().createTeam(p1, "Team " + (UHC.getPlugin().getTeamManager().getTeams().size() + 1));
            UHC.getPlugin().getTeamManager().addPlayer(newTeam, p2);
            clicker.sendMessage(ChatColor.GREEN + "¡Equipo formado con " + target.getName() + "!");
            target.sendMessage(ChatColor.GREEN + "¡Equipo formado con " + clicker.getName() + "!");
        }
    }

}
