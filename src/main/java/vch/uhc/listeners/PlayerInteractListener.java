package vch.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import vch.uhc.UHC;
import vch.uhc.misc.Messages;
import vch.uhc.misc.Settings;
import vch.uhc.misc.enums.GameState;
import vch.uhc.misc.enums.TeamMode;
import vch.uhc.models.UHCTeam;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }


        Settings settings = UHC.getPlugin().getSettings();
        if (settings.getTeamMode() != TeamMode.IN_GAME) {
            return;
        }


        if (settings.getGameState() != GameState.IN_PROGRESS || 
            UHC.getPlugin().getUHCManager().areTeamsFormed()) {
            return;
        }

        Player clicker = event.getPlayer();
        Player target = (Player) event.getRightClicked();

        vch.uhc.models.UHCPlayer p1 = UHC.getPlugin().getPlayerManager().getPlayerByUUID(clicker.getUniqueId());
        vch.uhc.models.UHCPlayer p2 = UHC.getPlugin().getPlayerManager().getPlayerByUUID(target.getUniqueId());

        if (p1 == null || p2 == null) {
            return;
        }

        boolean canJoin = UHC.getPlugin().getTeamManager().canPlayersJoinTeam(p1, p2);
        if (!canJoin) {
            clicker.sendMessage(Messages.INTERACT_CANNOT_TEAM(target.getName()));
            return;
        }

        UHCTeam teamP1 = p1.getTeam();
        UHCTeam teamP2 = p2.getTeam();

        if (teamP1 != null && teamP2 != null) {

            int totalMembers = teamP1.getMembers().size() + teamP2.getMembers().size();
            if (totalMembers > settings.getTeamSize()) {
                clicker.sendMessage(Messages.INTERACT_TEAMS_COMBINED_EXCEED(settings.getTeamSize()));
                return;
            }
            for (vch.uhc.models.UHCPlayer member : teamP2.getMembers()) {
                UHC.getPlugin().getTeamManager().removePlayer(teamP2, member);
                UHC.getPlugin().getTeamManager().addPlayer(teamP1, member);
                Player bukkitMember = member.getBukkitPlayer();
                if (bukkitMember != null) {
                    bukkitMember.sendMessage(Messages.INTERACT_TEAMS_MERGED(teamP1.getName()));
                }
            }
            UHC.getPlugin().getTeamManager().deleteTeam(teamP2);
            clicker.sendMessage(Messages.INTERACT_TEAM_MERGED_WITH(target.getName()));
        } else if (teamP1 != null) {

            if (teamP1.getMembers().size() >= settings.getTeamSize()) {
                clicker.sendMessage(Messages.INTERACT_YOUR_TEAM_FULL());
                return;
            }
            UHC.getPlugin().getTeamManager().addPlayer(teamP1, p2);
            clicker.sendMessage(target.getName() + " se unió a tu equipo!");
            target.sendMessage(Messages.INTERACT_JOINED_TEAM(clicker.getName()));
        } else if (teamP2 != null) {

            if (teamP2.getMembers().size() >= settings.getTeamSize()) {
                clicker.sendMessage(Messages.INTERACT_THEIR_TEAM_FULL(target.getName()));
                return;
            }
            UHC.getPlugin().getTeamManager().addPlayer(teamP2, p1);
            clicker.sendMessage(Messages.INTERACT_JOINED_TEAM(target.getName()));
            target.sendMessage(clicker.getName() + " se unió a tu equipo!");
        } else {

            UHCTeam newTeam = UHC.getPlugin().getTeamManager().createTeam(p1, "Team " + (UHC.getPlugin().getTeamManager().getTeams().size() + 1));
            UHC.getPlugin().getTeamManager().addPlayer(newTeam, p2);
            clicker.sendMessage(Messages.INTERACT_TEAM_FORMED_WITH(target.getName()));
            target.sendMessage(Messages.INTERACT_TEAM_FORMED_WITH(clicker.getName()));
        }
    }

}
