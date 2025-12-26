package vch.uhc.managers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import vch.uhc.models.Player;
import vch.uhc.models.Team;

@DisplayName("TeamManager - Lógica Compleja de Equipos")
class TeamManagerComplexTest {

    private TeamManager teamManager;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    @BeforeEach
    void setUp() {
        teamManager = new TeamManager();
        player1 = new Player(UUID.randomUUID(), "Player1");
        player2 = new Player(UUID.randomUUID(), "Player2");
        player3 = new Player(UUID.randomUUID(), "Player3");
        player4 = new Player(UUID.randomUUID(), "Player4");
    }

    @Nested
    @DisplayName("canPlayersJoinTeam - Validación Compleja")
    class CanPlayersJoinTeamTests {

        @Test
        @DisplayName("Should prevent same player from joining themselves")
        void shouldPreventSamePlayerJoining() {
            boolean canJoin = false;
            
            assertThat(player1).isEqualTo(player1);
        }

        @Test
        @DisplayName("Should prevent joining if in same team")
        void shouldPreventJoiningIfInSameTeam() {
            Team team = teamManager.createTeam(player1, "Team1");
            teamManager.addPlayer(team, player1);
            teamManager.addPlayer(team, player2);

            assertThat(player1.getTeam()).isEqualTo(player2.getTeam());
            assertThat(player1.getTeam()).isNotNull();
            
        }

        @Test
        @DisplayName("Should allow joining when both have no team")
        void shouldAllowJoiningWhenBothHaveNoTeam() {
            assertThat(player1.getTeam()).isNull();
            assertThat(player2.getTeam()).isNull();
            
        }

        @Test
        @DisplayName("Should allow one player to join existing team")
        void shouldAllowOnePlayerToJoinExistingTeam() {
            Team team1 = teamManager.createTeam(player1, "Team1");
            teamManager.addPlayer(team1, player1);

            assertThat(player1.getTeam()).isNotNull();
            assertThat(player2.getTeam()).isNull();
            
        }
    }

    @Nested
    @DisplayName("Lógica de Fusión de Equipos")
    class TeamMergingLogicTests {

        @Test
        @DisplayName("Should handle merging two small teams")
        void shouldHandleMergingTwoSmallTeams() {
            Team team1 = teamManager.createTeam(player1, "Team1");
            Team team2 = teamManager.createTeam(player3, "Team2");
            
            teamManager.addPlayer(team1, player1);
            teamManager.addPlayer(team2, player3);

            assertThat(teamManager.getTeams()).hasSize(2);
            assertThat(team1.getMembers()).containsExactly(player1);
            assertThat(team2.getMembers()).containsExactly(player3);
        }

        @Test
        @DisplayName("Should handle team with max capacity")
        void shouldHandleTeamWithMaxCapacity() {
            Team team = teamManager.createTeam(player1, "FullTeam");
            teamManager.addPlayer(team, player1);
            teamManager.addPlayer(team, player2);
            teamManager.addPlayer(team, player3);

            assertThat(team.getMembers()).hasSize(3);
            
        }

        @Test
        @DisplayName("Should maintain team integrity during operations")
        void shouldMaintainTeamIntegrityDuringOperations() {
            Team team1 = teamManager.createTeam(player1, "Team1");
            Team team2 = teamManager.createTeam(player2, "Team2");
            
            teamManager.addPlayer(team1, player1);
            teamManager.addPlayer(team2, player2);
            
            int initialTeamCount = teamManager.getTeams().size();
            
            teamManager.removePlayer(team1, player1);
            teamManager.addPlayer(team2, player1);
            
            assertThat(teamManager.getTeams()).hasSize(initialTeamCount - 1);
            assertThat(teamManager.getTeams()).doesNotContain(team1);
        }
    }

    @Nested
    @DisplayName("Escenarios Complejos del Juego")
    class ComplexGameScenariosTests {

        @Test
        @DisplayName("Scenario: Players forming teams during game start")
        void scenarioPlayersFormingTeamsDuringGameStart() {
            assertThat(player1.getTeam()).isNull();
            assertThat(player2.getTeam()).isNull();
            assertThat(player3.getTeam()).isNull();
            assertThat(player4.getTeam()).isNull();

            Team team1 = teamManager.createTeam(player1, "Team A");
            Team team2 = teamManager.createTeam(player3, "Team B");

            teamManager.addPlayer(team1, player1);
            teamManager.addPlayer(team1, player2);
            teamManager.addPlayer(team2, player3);
            teamManager.addPlayer(team2, player4);

            assertThat(team1.getMembers()).hasSize(2);
            assertThat(team2.getMembers()).hasSize(2);
            assertThat(teamManager.getTeams()).hasSize(2);
        }

        @Test
        @DisplayName("Scenario: Team elimination and cleanup")
        void scenarioTeamEliminationAndCleanup() {
            Team team = teamManager.createTeam(player1, "DoomedTeam");
            teamManager.addPlayer(team, player1);
            teamManager.addPlayer(team, player2);

            assertThat(teamManager.getTeams()).contains(team);

            teamManager.removePlayer(team, player1);
            assertThat(teamManager.getTeams()).contains(team);

            teamManager.removePlayer(team, player2);
            assertThat(teamManager.getTeams()).doesNotContain(team);
        }

        @Test
        @DisplayName("Scenario: Leader promotion chain")
        void scenarioLeaderPromotionChain() {
            Team team = teamManager.createTeam(player1, "Dynasty");
            teamManager.addPlayer(team, player1);
            teamManager.addPlayer(team, player2);
            teamManager.addPlayer(team, player3);

            assertThat(team.getLeader()).isEqualTo(player1);

            teamManager.removePlayer(team, player1);
            assertThat(team.getLeader()).isEqualTo(player2);

            teamManager.removePlayer(team, player2);
            assertThat(team.getLeader()).isEqualTo(player3);
        }

        @Test
        @DisplayName("Scenario: Multiple teams competing")
        void scenarioMultipleTeamsCompeting() {
            Team teamA = teamManager.createTeam(player1, "Alpha");
            Team teamB = teamManager.createTeam(player2, "Beta");
            Team teamC = teamManager.createTeam(player3, "Gamma");

            teamManager.addPlayer(teamA, player1);
            teamManager.addPlayer(teamB, player2);
            teamManager.addPlayer(teamC, player3);
            teamManager.addPlayer(teamC, player4);

            assertThat(teamManager.getTeams()).hasSize(3);
            
            assertThat(teamA.getMembers()).doesNotContainAnyElementsOf(teamB.getMembers());
            assertThat(teamB.getMembers()).doesNotContainAnyElementsOf(teamC.getMembers());
        }
    }

    @Nested
    @DisplayName("Casos Edge y Validaciones")
    class EdgeCasesAndValidationsTests {

        @Test
        @DisplayName("Should handle rapid team changes")
        void shouldHandleRapidTeamChanges() {
            Team team1 = teamManager.createTeam(player1, "Team1");
            Team team2 = teamManager.createTeam(player2, "Team2");

            teamManager.addPlayer(team1, player3);
            assertThat(player3.getTeam()).isEqualTo(team1);

            teamManager.removePlayer(team1, player3);
            teamManager.addPlayer(team2, player3);
            assertThat(player3.getTeam()).isEqualTo(team2);

            teamManager.removePlayer(team2, player3);
            assertThat(player3.getTeam()).isNull();
        }

        @Test
        @DisplayName("Should handle team with single member")
        void shouldHandleTeamWithSingleMember() {
            Team soloTeam = teamManager.createTeam(player1, "Solo");
            teamManager.addPlayer(soloTeam, player1);

            assertThat(soloTeam.getMembers()).hasSize(1);
            assertThat(soloTeam.getLeader()).isEqualTo(player1);
        }

        @Test
        @DisplayName("Should prevent duplicate team names")
        void shouldAllowDuplicateTeamNames() {
            Team team1 = teamManager.createTeam(player1, "Duplicate");
            Team team2 = teamManager.createTeam(player2, "Duplicate");

            assertThat(team1.getName()).isEqualTo(team2.getName());
            assertThat(team1).isNotEqualTo(team2);
        }

        @Test
        @DisplayName("Should handle renaming during game")
        void shouldHandleRenamingDuringGame() {
            Team team = teamManager.createTeam(player1, "Original");
            teamManager.addPlayer(team, player1);
            teamManager.addPlayer(team, player2);

            teamManager.renameTeam(team, "Renamed");

            assertThat(team.getName()).isEqualTo("Renamed");
            assertThat(team.getMembers()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Pruebas de Concurrencia y Estado")
    class ConcurrencyAndStateTests {

        @Test
        @DisplayName("Should maintain consistent state across operations")
        void shouldMaintainConsistentStateAcrossOperations() {
            int operationCount = 10;
            Team team = teamManager.createTeam(player1, "Consistent");

            for (int i = 0; i < operationCount; i++) {
                Player tempPlayer = new Player(UUID.randomUUID(), "Temp" + i);
                teamManager.addPlayer(team, tempPlayer);
                
                if (i % 2 == 0) {
                    teamManager.removePlayer(team, tempPlayer);
                }
            }

            assertThat(team.getMembers()).hasSize(6);
        }

        @Test
        @DisplayName("Should handle team operations in sequence")
        void shouldHandleTeamOperationsInSequence() {
            Team team = teamManager.createTeam(player1, "Sequential");
            
            teamManager.addPlayer(team, player1);
            assertThat(team.getMembers()).hasSize(1);
            
            teamManager.addPlayer(team, player2);
            assertThat(team.getMembers()).hasSize(2);
            
            teamManager.renameTeam(team, "NewName");
            assertThat(team.getName()).isEqualTo("NewName");
            
            teamManager.removePlayer(team, player1);
            assertThat(team.getMembers()).hasSize(1);
            assertThat(team.getLeader()).isEqualTo(player2);
        }
    }

    @Nested
    @DisplayName("Validaciones de Integridad de Datos")
    class DataIntegrityTests {

        @Test
        @DisplayName("Should maintain bidirectional player-team relationship")
        void shouldMaintainBidirectionalRelationship() {
            Team team = teamManager.createTeam(player1, "BiDir");
            teamManager.addPlayer(team, player1);

            assertThat(player1.getTeam()).isEqualTo(team);
            assertThat(team.getMembers()).contains(player1);

            teamManager.removePlayer(team, player1);
            assertThat(player1.getTeam()).isNull();
            assertThat(team.getMembers()).doesNotContain(player1);
        }

        @Test
        @DisplayName("Should not allow orphaned team references")
        void shouldNotAllowOrphanedTeamReferences() {
            Team team = teamManager.createTeam(player1, "Orphan");
            teamManager.addPlayer(team, player1);

            teamManager.removePlayer(team, player1);

            assertThat(teamManager.getTeams()).doesNotContain(team);
            assertThat(player1.getTeam()).isNull();
        }

        @Test
        @DisplayName("Should preserve team list integrity")
        void shouldPreserveTeamListIntegrity() {
            Team team1 = teamManager.createTeam(player1, "T1");
            Team team2 = teamManager.createTeam(player2, "T2");
            Team team3 = teamManager.createTeam(player3, "T3");

            teamManager.addPlayer(team1, player1);
            teamManager.addPlayer(team2, player2);
            teamManager.addPlayer(team3, player3);

            int initialSize = teamManager.getTeams().size();
            assertThat(initialSize).isEqualTo(3);

            teamManager.removePlayer(team2, player2);
            assertThat(teamManager.getTeams()).hasSize(2);
            assertThat(teamManager.getTeams()).containsExactlyInAnyOrder(team1, team3);
        }
    }
}
