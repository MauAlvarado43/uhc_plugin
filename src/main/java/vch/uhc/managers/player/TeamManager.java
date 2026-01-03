package vch.uhc.managers.player;

import java.util.ArrayList;

import vch.uhc.models.UHCPlayer;
import vch.uhc.models.UHCTeam;

/**
 * Manages UHC teams and team operations. Handles team creation, deletion, and
 * lookups.
 */
public class TeamManager {

    private final ArrayList<UHCTeam> teams = new ArrayList<>();

    /**
     * Creates a new team with an initial owner.
     *
     * @param owner The player who will lead the team.
     * @param teamName The name of the team.
     * @return The newly created UHCTeam.
     */
    public UHCTeam createTeam(UHCPlayer owner, String teamName) {
        UHCTeam team = new UHCTeam(teamName, owner);
        teams.add(team);
        owner.setTeam(team);
        team.addMember(owner);
        return team;
    }

    /**
     * Creates an empty team with no owner.
     *
     * @param teamName The name of the team.
     * @return The newly created UHCTeam.
     */
    public UHCTeam createTeam(String teamName) {
        UHCTeam team = new UHCTeam(teamName, null);
        teams.add(team);
        return team;
    }

    /**
     * Deletes a team from the manager.
     */
    public void deleteTeam(UHCTeam team) {
        teams.remove(team);
    }

    public ArrayList<UHCTeam> getTeams() {
        return teams;
    }

    /**
     * Finds a team by its name (case-insensitive).
     *
     * @param name The name to search for.
     * @return The UHCTeam instance, or null if not found.
     */
    public UHCTeam getTeamByName(String name) {
        return teams.stream()
                .filter(team -> team.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a player to a team and updates references.
     */
    public void addPlayer(UHCTeam team, UHCPlayer player) {
        player.setTeam(team);
        team.addMember(player);
    }

    /**
     * Removes a player from a team. If the team becomes empty, it is deleted.
     * If the leader leaves, a new one is promoted.
     */
    public void removePlayer(UHCTeam team, UHCPlayer player) {

        player.setTeam(null);
        team.removeMember(player);

        if (team.getMembers().isEmpty()) {
            deleteTeam(team);
            return;
        }

        if (team.getLeader() == player) {
            team.setLeader(team.getMembers().get(0));
        }

    }

    public void renameTeam(UHCTeam team, String newName) {
        team.setName(newName);
    }

    /**
     * Checks if two players are eligible to join the same team. Prevents
     * self-teaming or teaming with current teammates.
     */
    public boolean canPlayersJoinTeam(UHCPlayer p1, UHCPlayer p2) {
        return !p1.getUuid().equals(p2.getUuid())
                && (p1.getTeam() == null || !p1.getTeam().equals(p2.getTeam()));
    }

}
