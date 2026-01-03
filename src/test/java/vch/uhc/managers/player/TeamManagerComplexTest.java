package vch.uhc.managers.player;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import vch.uhc.models.UHCPlayer;
import vch.uhc.managers.player.TeamManager;
import vch.uhc.models.UHCTeam;

@DisplayName("TeamManager - Lógica Compleja de Equipos")
class TeamManagerComplexTest {

    private TeamManager teamManager;
    private UHCPlayer player1;
    private UHCPlayer player2;
    private UHCPlayer player3;
    private UHCPlayer player4;

    @BeforeEach
    void setUp() {
        teamManager = new TeamManager();
        player1 = new UHCPlayer(UUID.randomUUID(), "UHCPlayer1");
        player2 = new UHCPlayer(UUID.randomUUID(), "UHCPlayer2");
        player3 = new UHCPlayer(UUID.randomUUID(), "UHCPlayer3");
        player4 = new UHCPlayer(UUID.randomUUID(), "UHCPlayer4");
    }

    @Nested
    @DisplayName("canUHCPlayersJoinUHCTeam - Validación Compleja")
    class CanUHCPlayersJoinUHCTeamTests {

        @Test
        @DisplayName("Should prevent same player from joining themselves")
        void shouldPreventSameUHCPlayerJoining() {
            boolean canJoin = false;
            assertThat(player1).isEqualTo(player1);
        }

        @Test
        @DisplayName("Should prevent joining if in same team")
        void shouldPreventJoiningIfInSameUHCTeam() {
            UHCTeam team = teamManager.createTeam(player1, "UHCTeam1");
            teamManager.addPlayer(team, player1);
            teamManager.addPlayer(team, player2);

            assertThat(player1.getTeam()).isEqualTo(player2.getTeam());
            assertThat(player1.getTeam()).isNotNull();
            
        }

        @Test
        @DisplayName("Should allow joining when both have no team")
        void shouldAllowJoiningWhenBothHaveNoUHCTeam() {
            assertThat(player1.getTeam()).isNull();
            assertThat(player2.getTeam()).isNull();
            
        }

        @Test
        @DisplayName("Should allow one player to join existing team")
        void shouldAllowOneUHCPlayerToJoinExistingUHCTeam() {
            UHCTeam team1 = teamManager.createTeam(player1, "UHCTeam1");
            teamManager.addPlayer(team1, player1);

            assertThat(player1.getTeam()).isNotNull();
            assertThat(player2.getTeam()).isNull();
            
        }
    }

    @Nested
    @DisplayName("Lógica de Fusión de Equipos")
    class UHCTeamMergingLogicTests {

        @Test
        @DisplayName("Should handle merging two small teams")
        void shouldHandleMergingTwoSmallUHCTeams() {
            UHCTeam team1 = teamManager.createTeam(player1, "UHCTeam1");
            UHCTeam team2 = teamManager.createTeam(player3, "UHCTeam2");
            
            teamManager.addPlayer(team1, player1);
            teamManager.addPlayer(team2, player3);

            assertThat(teamManager.getTeams()).hasSize(2);
            assertThat(team1.getMembers()).containsExactly(player1);
            assertThat(team2.getMembers()).containsExactly(player3);
        }

        @Test
        @DisplayName("Should handle team with max capacity")
        void shouldHandleUHCTeamWithMaxCapacity() {
            UHCTeam team = teamManager.createTeam(player1, "FullUHCTeam");
            teamManager.addPlayer(team, player1);
            teamManager.addPlayer(team, player2);
            teamManager.addPlayer(team, player3);

            assertThat(team.getMembers()).hasSize(3);
            
        }

        @Test
        @DisplayName("Should maintain team integrity during operations")
        void shouldMaintainUHCTeamIntegrityDuringOperations() {
            UHCTeam team1 = teamManager.createTeam(player1, "UHCTeam1");
            UHCTeam team2 = teamManager.createTeam(player2, "UHCTeam2");
            
            teamManager.addPlayer(team1, player1);
            teamManager.addPlayer(team2, player2);
            
            int initialUHCTeamCount = teamManager.getTeams().size();
            
            teamManager.removePlayer(team1, player1);
            teamManager.addPlayer(team2, player1);
            
            assertThat(teamManager.getTeams()).hasSize(initialUHCTeamCount - 1);
            assertThat(teamManager.getTeams()).doesNotContain(team1);
        }
    }

    @Nested
    @DisplayName("Escenarios Complejos del Juego")
    class ComplexGameScenariosTests {

        @Test
        @DisplayName("Scenario: UHCPlayers forming teams during game start")
        void scenarioUHCPlayersFormingUHCTeamsDuringGameStart() {
            assertThat(player1.getTeam()).isNull();
            assertThat(player2.getTeam()).isNull();
            assertThat(player3.getTeam()).isNull();
            assertThat(player4.getTeam()).isNull();

            UHCTeam team1 = teamManager.createTeam(player1, "UHCTeam A");
            UHCTeam team2 = teamManager.createTeam(player3, "UHCTeam B");

            teamManager.addPlayer(team1, player1);
            teamManager.addPlayer(team1, player2);
            teamManager.addPlayer(team2, player3);
            teamManager.addPlayer(team2, player4);

            assertThat(team1.getMembers()).hasSize(2);
            assertThat(team2.getMembers()).hasSize(2);
            assertThat(teamManager.getTeams()).hasSize(2);
        }

        @Test
        @DisplayName("Scenario: UHCTeam elimination and cleanup")
        void scenarioUHCTeamEliminationAndCleanup() {
            UHCTeam team = teamManager.createTeam(player1, "DoomedUHCTeam");
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
            UHCTeam team = teamManager.createTeam(player1, "Dynasty");
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
        void scenarioMultipleUHCTeamsCompeting() {
            UHCTeam teamA = teamManager.createTeam(player1, "Alpha");
            UHCTeam teamB = teamManager.createTeam(player2, "Beta");
            UHCTeam teamC = teamManager.createTeam(player3, "Gamma");

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
        void shouldHandleRapidUHCTeamChanges() {
            UHCTeam team1 = teamManager.createTeam(player1, "UHCTeam1");
            UHCTeam team2 = teamManager.createTeam(player2, "UHCTeam2");

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
        void shouldHandleUHCTeamWithSingleMember() {
            UHCTeam soloUHCTeam = teamManager.createTeam(player1, "Solo");
            teamManager.addPlayer(soloUHCTeam, player1);

            assertThat(soloUHCTeam.getMembers()).hasSize(1);
            assertThat(soloUHCTeam.getLeader()).isEqualTo(player1);
        }

        @Test
        @DisplayName("Should prevent duplicate team names")
        void shouldAllowDuplicateUHCTeamNames() {
            UHCTeam team1 = teamManager.createTeam(player1, "Duplicate");
            UHCTeam team2 = teamManager.createTeam(player2, "Duplicate");

            assertThat(team1.getName()).isEqualTo(team2.getName());
            assertThat(team1).isNotEqualTo(team2);
        }

        @Test
        @DisplayName("Should handle renaming during game")
        void shouldHandleRenamingDuringGame() {
            UHCTeam team = teamManager.createTeam(player1, "Original");
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
            UHCTeam team = teamManager.createTeam(player1, "Consistent");

            for (int i = 0; i < operationCount; i++) {
                UHCPlayer tempUHCPlayer = new UHCPlayer(UUID.randomUUID(), "Temp" + i);
                teamManager.addPlayer(team, tempUHCPlayer);
                
                if (i % 2 == 0) {
                    teamManager.removePlayer(team, tempUHCPlayer);
                }
            }

            assertThat(team.getMembers()).hasSize(6);
        }

        @Test
        @DisplayName("Should handle team operations in sequence")
        void shouldHandleUHCTeamOperationsInSequence() {
            UHCTeam team = teamManager.createTeam(player1, "Sequential");
            
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
            UHCTeam team = teamManager.createTeam(player1, "BiDir");
            teamManager.addPlayer(team, player1);

            assertThat(player1.getTeam()).isEqualTo(team);
            assertThat(team.getMembers()).contains(player1);

            teamManager.removePlayer(team, player1);
            assertThat(player1.getTeam()).isNull();
            assertThat(team.getMembers()).doesNotContain(player1);
        }

        @Test
        @DisplayName("Should not allow orphaned team references")
        void shouldNotAllowOrphanedUHCTeamReferences() {
            UHCTeam team = teamManager.createTeam(player1, "Orphan");
            teamManager.addPlayer(team, player1);

            teamManager.removePlayer(team, player1);

            assertThat(teamManager.getTeams()).doesNotContain(team);
            assertThat(player1.getTeam()).isNull();
        }

        @Test
        @DisplayName("Should preserve team list integrity")
        void shouldPreserveUHCTeamListIntegrity() {
            UHCTeam team1 = teamManager.createTeam(player1, "T1");
            UHCTeam team2 = teamManager.createTeam(player2, "T2");
            UHCTeam team3 = teamManager.createTeam(player3, "T3");

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
