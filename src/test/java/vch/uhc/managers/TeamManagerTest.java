package vch.uhc.managers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import vch.uhc.models.Player;
import vch.uhc.models.Team;

@DisplayName("TeamManager Tests")
class TeamManagerTest {

    private TeamManager teamManager;
    private Player player1;
    private Player player2;
    private Player player3;

    @BeforeEach
    void setUp() {
        teamManager = new TeamManager();
        player1 = new Player(UUID.randomUUID(), "Player1");
        player2 = new Player(UUID.randomUUID(), "Player2");
        player3 = new Player(UUID.randomUUID(), "Player3");
    }

    @Nested
    @DisplayName("Create Team Tests")
    class CreateTeamTests {

        @Test
        @DisplayName("Should create team with correct name and owner")
        void shouldCreateTeamWithCorrectNameAndOwner() {
            Team team = teamManager.createTeam(player1, "TestTeam");
            
            assertThat(team).isNotNull();
            assertThat(team.getName()).isEqualTo("TestTeam");
            assertThat(team.getLeader()).isEqualTo(player1);
        }

        @Test
        @DisplayName("Should add created team to teams list")
        void shouldAddCreatedTeamToTeamsList() {
            Team team = teamManager.createTeam(player1, "TestTeam");
            
            assertThat(teamManager.getTeams()).contains(team);
        }

        @Test
        @DisplayName("Should create multiple teams")
        void shouldCreateMultipleTeams() {
            Team team1 = teamManager.createTeam(player1, "Team1");
            Team team2 = teamManager.createTeam(player2, "Team2");
            
            assertThat(teamManager.getTeams()).hasSize(2);
            assertThat(teamManager.getTeams()).containsExactly(team1, team2);
        }
    }

    @Nested
    @DisplayName("Delete Team Tests")
    class DeleteTeamTests {

        @Test
        @DisplayName("Should delete team successfully")
        void shouldDeleteTeam() {
            Team team = teamManager.createTeam(player1, "TestTeam");
            teamManager.deleteTeam(team);
            
            assertThat(teamManager.getTeams()).doesNotContain(team);
        }

        @Test
        @DisplayName("Should handle deleting non-existent team")
        void shouldHandleDeletingNonExistentTeam() {
            Team team = new Team("NonExistent", player1);
            
            assertThatCode(() -> teamManager.deleteTeam(team))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should only delete specified team")
        void shouldOnlyDeleteSpecifiedTeam() {
            Team team1 = teamManager.createTeam(player1, "Team1");
            Team team2 = teamManager.createTeam(player2, "Team2");
            
            teamManager.deleteTeam(team1);
            
            assertThat(teamManager.getTeams()).containsExactly(team2);
        }
    }

    @Nested
    @DisplayName("Add Player Tests")
    class AddPlayerTests {

        @Test
        @DisplayName("Should add player to team")
        void shouldAddPlayerToTeam() {
            Team team = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(team, player2);
            
            assertThat(team.getMembers()).contains(player2);
        }

        @Test
        @DisplayName("Should set player's team reference")
        void shouldSetPlayerTeamReference() {
            Team team = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(team, player2);
            
            assertThat(player2.getTeam()).isEqualTo(team);
        }

        @Test
        @DisplayName("Should add multiple players to team")
        void shouldAddMultiplePlayersToTeam() {
            Team team = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(team, player2);
            teamManager.addPlayer(team, player3);
            
            assertThat(team.getMembers()).containsExactly(player2, player3);
        }
    }

    @Nested
    @DisplayName("Remove Player Tests")
    class RemovePlayerTests {

        @Test
        @DisplayName("Should remove player from team")
        void shouldRemovePlayerFromTeam() {
            Team team = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(team, player2);
            teamManager.removePlayer(team, player2);
            
            assertThat(team.getMembers()).doesNotContain(player2);
        }

        @Test
        @DisplayName("Should clear player's team reference")
        void shouldClearPlayerTeamReference() {
            Team team = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(team, player2);
            teamManager.removePlayer(team, player2);
            
            assertThat(player2.getTeam()).isNull();
        }

        @Test
        @DisplayName("Should delete team when last member is removed")
        void shouldDeleteTeamWhenLastMemberRemoved() {
            Team team = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(team, player2);
            teamManager.removePlayer(team, player2);
            
            assertThat(teamManager.getTeams()).doesNotContain(team);
        }

        @Test
        @DisplayName("Should promote new leader when leader leaves")
        void shouldPromoteNewLeaderWhenLeaderLeaves() {
            Team team = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(team, player1);
            teamManager.addPlayer(team, player2);
            teamManager.addPlayer(team, player3);
            
            teamManager.removePlayer(team, player1);
            
            assertThat(team.getLeader()).isEqualTo(player2);
        }

        @Test
        @DisplayName("Should not delete team if members remain")
        void shouldNotDeleteTeamIfMembersRemain() {
            Team team = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(team, player2);
            teamManager.addPlayer(team, player3);
            
            teamManager.removePlayer(team, player2);
            
            assertThat(teamManager.getTeams()).contains(team);
            assertThat(team.getMembers()).containsExactly(player3);
        }
    }

    @Nested
    @DisplayName("Rename Team Tests")
    class RenameTeamTests {

        @Test
        @DisplayName("Should rename team successfully")
        void shouldRenameTeam() {
            Team team = teamManager.createTeam(player1, "OldName");
            teamManager.renameTeam(team, "NewName");
            
            assertThat(team.getName()).isEqualTo("NewName");
        }

        @Test
        @DisplayName("Should keep team in teams list after rename")
        void shouldKeepTeamInTeamsListAfterRename() {
            Team team = teamManager.createTeam(player1, "OldName");
            teamManager.renameTeam(team, "NewName");
            
            assertThat(teamManager.getTeams()).contains(team);
        }
    }

    @Nested
    @DisplayName("Get Teams Tests")
    class GetTeamsTests {

        @Test
        @DisplayName("Should return empty list when no teams")
        void shouldReturnEmptyListWhenNoTeams() {
            assertThat(teamManager.getTeams()).isEmpty();
        }

        @Test
        @DisplayName("Should return all teams")
        void shouldReturnAllTeams() {
            Team team1 = teamManager.createTeam(player1, "Team1");
            Team team2 = teamManager.createTeam(player2, "Team2");
            
            assertThat(teamManager.getTeams()).hasSize(2);
            assertThat(teamManager.getTeams()).containsExactly(team1, team2);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete team lifecycle")
        void shouldHandleCompleteTeamLifecycle() {
            Team team = teamManager.createTeam(player1, "TestTeam");
            assertThat(teamManager.getTeams()).contains(team);
            
            teamManager.addPlayer(team, player2);
            teamManager.addPlayer(team, player3);
            assertThat(team.getMembers()).hasSize(2);
            
            teamManager.renameTeam(team, "RenamedTeam");
            assertThat(team.getName()).isEqualTo("RenamedTeam");
            
            teamManager.removePlayer(team, player2);
            assertThat(team.getMembers()).hasSize(1);
            
            teamManager.removePlayer(team, player3);
            assertThat(teamManager.getTeams()).doesNotContain(team);
        }

        @Test
        @DisplayName("Should handle player switching teams")
        void shouldHandlePlayerSwitchingTeams() {
            Team team1 = teamManager.createTeam(player1, "Team1");
            Team team2 = teamManager.createTeam(player2, "Team2");
            
            teamManager.addPlayer(team1, player3);
            assertThat(player3.getTeam()).isEqualTo(team1);
            
            teamManager.removePlayer(team1, player3);
            teamManager.addPlayer(team2, player3);
            
            assertThat(player3.getTeam()).isEqualTo(team2);
            assertThat(team1.getMembers()).doesNotContain(player3);
            assertThat(team2.getMembers()).contains(player3);
        }
    }
}
