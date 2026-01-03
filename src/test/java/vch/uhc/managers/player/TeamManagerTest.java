package vch.uhc.managers.player;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import vch.uhc.models.UHCPlayer;
import vch.uhc.managers.player.TeamManager;
import vch.uhc.models.UHCTeam;

@DisplayName("TeamManager Tests")
class TeamManagerTest {

    private TeamManager teamManager;
    private UHCPlayer player1;
    private UHCPlayer player2;
    private UHCPlayer player3;

    @BeforeEach
    void setUp() {
        teamManager = new TeamManager();
        player1 = new UHCPlayer(UUID.randomUUID(), "Player1");
        player2 = new UHCPlayer(UUID.randomUUID(), "Player2");
        player3 = new UHCPlayer(UUID.randomUUID(), "Player3");
    }

    @Nested
    @DisplayName("Create UHCTeam Tests")
    class CreateTeamTests {

        @Test
        @DisplayName("Should create UHCTeam with correct name and owner")
        void shouldCreateTeamWithCorrectNameAndOwner() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "TestTeam");
            
            assertThat(UHCTeam).isNotNull();
            assertThat(UHCTeam.getName()).isEqualTo("TestTeam");
            assertThat(UHCTeam.getLeader()).isEqualTo(player1);
        }

        @Test
        @DisplayName("Should add created UHCTeam to teams list")
        void shouldAddCreatedTeamToTeamsList() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "TestTeam");
            
            assertThat(teamManager.getTeams()).contains(UHCTeam);
        }

        @Test
        @DisplayName("Should create multiple teams")
        void shouldCreateMultipleTeams() {
            UHCTeam team1 = teamManager.createTeam(player1, "Team1");
            UHCTeam team2 = teamManager.createTeam(player2, "Team2");
            
            assertThat(teamManager.getTeams()).hasSize(2);
            assertThat(teamManager.getTeams()).containsExactly(team1, team2);
        }
    }

    @Nested
    @DisplayName("Delete UHCTeam Tests")
    class DeleteTeamTests {

        @Test
        @DisplayName("Should delete UHCTeam successfully")
        void shouldDeleteTeam() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "TestTeam");
            teamManager.deleteTeam(UHCTeam);
            
            assertThat(teamManager.getTeams()).doesNotContain(UHCTeam);
        }

        @Test
        @DisplayName("Should handle deleting non-existent UHCTeam")
        void shouldHandleDeletingNonExistentTeam() {
            UHCTeam UHCTeam = new UHCTeam("NonExistent", player1);
            
            assertThatCode(() -> teamManager.deleteTeam(UHCTeam))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should only delete specified UHCTeam")
        void shouldOnlyDeleteSpecifiedTeam() {
            UHCTeam team1 = teamManager.createTeam(player1, "Team1");
            UHCTeam team2 = teamManager.createTeam(player2, "Team2");
            
            teamManager.deleteTeam(team1);
            
            assertThat(teamManager.getTeams()).containsExactly(team2);
        }
    }

    @Nested
    @DisplayName("Add UHCPlayer Tests")
    class AddPlayerTests {

        @Test
        @DisplayName("Should add UHCPlayer to UHCTeam")
        void shouldAddPlayerToTeam() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(UHCTeam, player2);
            
            assertThat(UHCTeam.getMembers()).contains(player2);
        }

        @Test
        @DisplayName("Should set UHCPlayer's UHCTeam reference")
        void shouldSetPlayerTeamReference() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(UHCTeam, player2);
            
            assertThat(player2.getTeam()).isEqualTo(UHCTeam);
        }

        @Test
        @DisplayName("Should add multiple players to UHCTeam")
        void shouldAddMultiplePlayersToTeam() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(UHCTeam, player2);
            teamManager.addPlayer(UHCTeam, player3);
            
            assertThat(UHCTeam.getMembers()).containsExactly(player1, player2, player3);
        }
    }

    @Nested
    @DisplayName("Remove UHCPlayer Tests")
    class RemovePlayerTests {

        @Test
        @DisplayName("Should remove UHCPlayer from UHCTeam")
        void shouldRemovePlayerFromTeam() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(UHCTeam, player2);
            teamManager.removePlayer(UHCTeam, player2);
            
            assertThat(UHCTeam.getMembers()).doesNotContain(player2);
        }

        @Test
        @DisplayName("Should clear UHCPlayer's UHCTeam reference")
        void shouldClearPlayerTeamReference() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(UHCTeam, player2);
            teamManager.removePlayer(UHCTeam, player2);
            
            assertThat(player2.getTeam()).isNull();
        }

        @Test
        @DisplayName("Should delete UHCTeam when last member is removed")
        void shouldDeleteTeamWhenLastMemberRemoved() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(UHCTeam, player2);
            teamManager.removePlayer(UHCTeam, player2);
            teamManager.removePlayer(UHCTeam, player1);
            
            assertThat(teamManager.getTeams()).doesNotContain(UHCTeam);
        }

        @Test
        @DisplayName("Should promote new leader when leader leaves")
        void shouldPromoteNewLeaderWhenLeaderLeaves() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(UHCTeam, player2);
            teamManager.addPlayer(UHCTeam, player3);
            
            teamManager.removePlayer(UHCTeam, player1);
            
            assertThat(UHCTeam.getLeader()).isEqualTo(player2);
        }

        @Test
        @DisplayName("Should not delete UHCTeam if members remain")
        void shouldNotDeleteTeamIfMembersRemain() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "TestTeam");
            teamManager.addPlayer(UHCTeam, player2);
            teamManager.addPlayer(UHCTeam, player3);
            
            teamManager.removePlayer(UHCTeam, player2);
            
            assertThat(teamManager.getTeams()).contains(UHCTeam);
            assertThat(UHCTeam.getMembers()).containsExactly(player1, player3);
        }
    }

    @Nested
    @DisplayName("Rename UHCTeam Tests")
    class RenameTeamTests {

        @Test
        @DisplayName("Should rename UHCTeam successfully")
        void shouldRenameTeam() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "OldName");
            teamManager.renameTeam(UHCTeam, "NewName");
            
            assertThat(UHCTeam.getName()).isEqualTo("NewName");
        }

        @Test
        @DisplayName("Should keep UHCTeam in teams list after rename")
        void shouldKeepTeamInTeamsListAfterRename() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "OldName");
            teamManager.renameTeam(UHCTeam, "NewName");
            
            assertThat(teamManager.getTeams()).contains(UHCTeam);
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
            UHCTeam team1 = teamManager.createTeam(player1, "Team1");
            UHCTeam team2 = teamManager.createTeam(player2, "Team2");
            
            assertThat(teamManager.getTeams()).hasSize(2);
            assertThat(teamManager.getTeams()).containsExactly(team1, team2);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete UHCTeam lifecycle")
        void shouldHandleCompleteTeamLifecycle() {
            UHCTeam UHCTeam = teamManager.createTeam(player1, "TestTeam");
            assertThat(teamManager.getTeams()).contains(UHCTeam);
            
            teamManager.addPlayer(UHCTeam, player2);
            teamManager.addPlayer(UHCTeam, player3);
            assertThat(UHCTeam.getMembers()).hasSize(3);
            
            teamManager.renameTeam(UHCTeam, "RenamedTeam");
            assertThat(UHCTeam.getName()).isEqualTo("RenamedTeam");
            
            teamManager.removePlayer(UHCTeam, player2);
            assertThat(UHCTeam.getMembers()).hasSize(2);
            
            teamManager.removePlayer(UHCTeam, player3);
            teamManager.removePlayer(UHCTeam, player1);
            assertThat(teamManager.getTeams()).doesNotContain(UHCTeam);
        }

        @Test
        @DisplayName("Should handle UHCPlayer switching teams")
        void shouldHandlePlayerSwitchingTeams() {
            UHCTeam team1 = teamManager.createTeam(player1, "Team1");
            UHCTeam team2 = teamManager.createTeam(player2, "Team2");
            
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
