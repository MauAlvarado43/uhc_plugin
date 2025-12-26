package vch.uhc.managers;

import java.util.ArrayList;

import vch.uhc.models.Player;
import vch.uhc.models.Team;

public class TeamManager {

    private ArrayList<Team> teams = new ArrayList<>();

    public Team createTeam(Player owner, String teamName) {
        Team team = new Team(teamName, owner);
        teams.add(team);
        owner.setTeam(team);
        team.addMember(owner);
        return team;
    }

    public Team createTeam(String teamName) {
        Team team = new Team(teamName, null);
        teams.add(team);
        return team;
    }

    public void deleteTeam(Team team) {
        teams.remove(team);
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public Team getTeamByName(String name) {
        return teams.stream()
            .filter(team -> team.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    public void addPlayer(Team team, Player player) {
        player.setTeam(team);
        team.addMember(player);
    }

    public void removePlayer(Team team, Player player) {

        player.setTeam(null);
        team.removeMember(player);

        if (team.getMembers().isEmpty()) {
            deleteTeam(team);
            return;
        }

        if (team.getLeader() == player)
            team.setLeader(team.getMembers().get(0));

    }

    public void renameTeam(Team team, String newName) {
        team.setName(newName);
    }

    public boolean canPlayersJoinTeam(Player p1, Player p2) {
        if (p1.getTeam() == null || p2.getTeam() == null)
            return false;
        
        if (p1.getTeam() != p2.getTeam())
            return false;
        
        return p1.getTeam().getMembers().size() < 5;
    }

}