package vch.uhc.managers;

import java.util.ArrayList;

import vch.uhc.models.UHCPlayer;
import vch.uhc.models.UHCTeam;

/**
 * Manages UHC teams and team operations.
 * Handles team creation, deletion, and lookups.
 */
public class TeamManager {

    private final ArrayList<UHCTeam> teams = new ArrayList<>();

    public UHCTeam createTeam(UHCPlayer owner, String teamName) {
        UHCTeam team = new UHCTeam(teamName, owner);
        teams.add(team);
        owner.setTeam(team);
        team.addMember(owner);
        return team;
    }

    public UHCTeam createTeam(String teamName) {
        UHCTeam team = new UHCTeam(teamName, null);
        teams.add(team);
        return team;
    }

    public void deleteTeam(UHCTeam team) {
        teams.remove(team);
    }

    public ArrayList<UHCTeam> getTeams() {
        return teams;
    }

    public UHCTeam getTeamByName(String name) {
        return teams.stream()
            .filter(team -> team.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    public void addPlayer(UHCTeam team, UHCPlayer player) {
        player.setTeam(team);
        team.addMember(player);
    }

    public void removePlayer(UHCTeam team, UHCPlayer player) {

        player.setTeam(null);
        team.removeMember(player);

        if (team.getMembers().isEmpty()) {
            deleteTeam(team);
            return;
        }

        if (team.getLeader() == player)
            team.setLeader(team.getMembers().get(0));

    }

    public void renameTeam(UHCTeam team, String newName) {
        team.setName(newName);
    }

    public boolean canPlayersJoinTeam(UHCPlayer p1, UHCPlayer p2) {
        if (p1.getUuid().equals(p2.getUuid()))
            return false;
        
        if (p1.getTeam() != null && p2.getTeam() != null && p1.getTeam() == p2.getTeam())
            return false;
        
        return true;
    }

}