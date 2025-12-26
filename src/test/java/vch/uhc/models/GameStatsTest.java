package vch.uhc.models;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("GameStats - Estadísticas del Juego")
class GameStatsTest {

    private GameStats gameStats;
    private UUID player1UUID;
    private UUID player2UUID;

    @BeforeEach
    void setUp() {
        gameStats = new GameStats();
        player1UUID = UUID.randomUUID();
        player2UUID = UUID.randomUUID();
    }

    @Nested
    @DisplayName("Inicialización y Tiempo de Juego")
    class TimeManagementTests {

        @Test
        @DisplayName("Should initialize with current timestamp")
        void shouldInitializeWithCurrentTimestamp() {
            long before = System.currentTimeMillis();
            GameStats stats = new GameStats();
            long after = System.currentTimeMillis();

            assertThat(stats.getGameStartTime())
                .isGreaterThanOrEqualTo(before)
                .isLessThanOrEqualTo(after);
        }

        @Test
        @DisplayName("Should calculate total game duration correctly")
        void shouldCalculateTotalGameDuration() {
            long startTime = gameStats.getGameStartTime();
            long endTime = startTime + 3661000;

            gameStats.setGameEndTime(endTime);

            assertThat(gameStats.getTotalGameDuration()).isEqualTo(3661000);
        }

        @Test
        @DisplayName("Should format duration as HH:MM:SS")
        void shouldFormatDurationCorrectly() {
            long startTime = gameStats.getGameStartTime();

            gameStats.setGameEndTime(startTime + (3600 + 1800 + 45) * 1000);
            assertThat(gameStats.getFormattedDuration()).isEqualTo("01:30:45");

            GameStats stats2 = new GameStats();
            stats2.setGameEndTime(stats2.getGameStartTime() + (300 + 3) * 1000);
            assertThat(stats2.getFormattedDuration()).isEqualTo("00:05:03");
        }

        @Test
        @DisplayName("Should format duration for games longer than 24 hours")
        void shouldFormatLongDuration() {
            long startTime = gameStats.getGameStartTime();
            gameStats.setGameEndTime(startTime + (25 * 3600 * 1000));
            assertThat(gameStats.getFormattedDuration()).isEqualTo("25:00:00");
        }

        @Test
        @DisplayName("Should format zero duration correctly")
        void shouldFormatZeroDuration() {
            long time = gameStats.getGameStartTime();
            gameStats.setGameEndTime(time);
            assertThat(gameStats.getFormattedDuration()).isEqualTo("00:00:00");
        }
    }

    @Nested
    @DisplayName("Gestión de Ganadores")
    class WinnerManagementTests {

        @Test
        @DisplayName("Should set and get winner name")
        void shouldSetAndGetWinner() {
            gameStats.setWinner("Player1");
            assertThat(gameStats.getWinner()).isEqualTo("Player1");
        }

        @Test
        @DisplayName("Should handle team winner names")
        void shouldHandleTeamWinnerNames() {
            gameStats.setWinner("Team Alpha");
            assertThat(gameStats.getWinner()).isEqualTo("Team Alpha");
        }

        @Test
        @DisplayName("Should allow updating winner")
        void shouldAllowUpdatingWinner() {
            gameStats.setWinner("Player1");
            gameStats.setWinner("Player2");
            assertThat(gameStats.getWinner()).isEqualTo("Player2");
        }

        @Test
        @DisplayName("Should start with no winner")
        void shouldStartWithNoWinner() {
            assertThat(gameStats.getWinner()).isNull();
        }
    }

    @Nested
    @DisplayName("Estadísticas de Jugadores")
    class PlayerStatsTests {

        @Test
        @DisplayName("Should record player statistics")
        void shouldRecordPlayerStats() {
            gameStats.recordPlayerStat(player1UUID, "Player1", 5, 1, 3600000);

            assertThat(gameStats.getPlayerStats()).containsKey(player1UUID);
            var stats = gameStats.getPlayerStats().get(player1UUID);
            assertThat(stats.getPlayerName()).isEqualTo("Player1");
            assertThat(stats.getKills()).isEqualTo(5);
            assertThat(stats.getDeaths()).isEqualTo(1);
            assertThat(stats.getSurvivalTime()).isEqualTo(3600000);
        }

        @Test
        @DisplayName("Should record multiple players statistics")
        void shouldRecordMultiplePlayersStats() {
            gameStats.recordPlayerStat(player1UUID, "Player1", 5, 1, 3600000);
            gameStats.recordPlayerStat(player2UUID, "Player2", 3, 2, 1800000);

            assertThat(gameStats.getPlayerStats()).hasSize(2);
            assertThat(gameStats.getPlayerStats()).containsKeys(player1UUID, player2UUID);
        }

        @Test
        @DisplayName("Should update stats if recorded again for same player")
        void shouldUpdateStatsIfRecordedAgain() {
            gameStats.recordPlayerStat(player1UUID, "Player1", 5, 1, 3600000);
            gameStats.recordPlayerStat(player1UUID, "Player1", 10, 2, 7200000);

            var stats = gameStats.getPlayerStats().get(player1UUID);
            assertThat(stats.getKills()).isEqualTo(10);
            assertThat(stats.getDeaths()).isEqualTo(2);
            assertThat(stats.getSurvivalTime()).isEqualTo(7200000);
        }

        @Test
        @DisplayName("Should handle zero stats")
        void shouldHandleZeroStats() {
            gameStats.recordPlayerStat(player1UUID, "Player1", 0, 0, 0);

            var stats = gameStats.getPlayerStats().get(player1UUID);
            assertThat(stats.getKills()).isEqualTo(0);
            assertThat(stats.getDeaths()).isEqualTo(0);
            assertThat(stats.getSurvivalTime()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle high kill counts")
        void shouldHandleHighKillCounts() {
            gameStats.recordPlayerStat(player1UUID, "Pro Player", 50, 1, 10000000);

            var stats = gameStats.getPlayerStats().get(player1UUID);
            assertThat(stats.getKills()).isEqualTo(50);
        }
    }

    @Nested
    @DisplayName("Casos Edge de PlayerStats")
    class PlayerStatsEdgeCasesTests {

        @Test
        @DisplayName("Should preserve player name with special characters")
        void shouldPreservePlayerNameWithSpecialCharacters() {
            gameStats.recordPlayerStat(player1UUID, "Player_123-ABC", 5, 1, 1000);

            var stats = gameStats.getPlayerStats().get(player1UUID);
            assertThat(stats.getPlayerName()).isEqualTo("Player_123-ABC");
        }

        @Test
        @DisplayName("Should handle very long survival times")
        void shouldHandleVeryLongSurvivalTimes() {
            long sevenDaysInMillis = 7L * 24 * 3600 * 1000;
            gameStats.recordPlayerStat(player1UUID, "Survivor", 0, 0, sevenDaysInMillis);

            var stats = gameStats.getPlayerStats().get(player1UUID);
            assertThat(stats.getSurvivalTime()).isEqualTo(sevenDaysInMillis);
        }

        @Test
        @DisplayName("Should return empty map initially")
        void shouldReturnEmptyMapInitially() {
            assertThat(gameStats.getPlayerStats()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Integración de Tiempo y Estadísticas")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete game lifecycle")
        void shouldHandleCompleteGameLifecycle() {
            long startTime = gameStats.getGameStartTime();
            assertThat(startTime).isGreaterThan(0);

            gameStats.recordPlayerStat(player1UUID, "Winner", 10, 1, 3600000);
            gameStats.recordPlayerStat(player2UUID, "Loser", 2, 5, 1800000);

            gameStats.setGameEndTime(startTime + 3661000);
            gameStats.setWinner("Winner");

            assertThat(gameStats.getWinner()).isEqualTo("Winner");
            assertThat(gameStats.getPlayerStats()).hasSize(2);
            assertThat(gameStats.getFormattedDuration()).isEqualTo("01:01:01");
            assertThat(gameStats.getTotalGameDuration()).isEqualTo(3661000);
        }

        @Test
        @DisplayName("Should calculate KD ratio scenario")
        void shouldCalculateKDRatioScenario() {
            gameStats.recordPlayerStat(player1UUID, "Pro", 20, 2, 5000000);

            var stats = gameStats.getPlayerStats().get(player1UUID);
            assertThat(stats.getKills()).isEqualTo(20);
            assertThat(stats.getDeaths()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should track multiple stat updates throughout game")
        void shouldTrackMultipleStatUpdates() {
            gameStats.recordPlayerStat(player1UUID, "Player", 1, 0, 600000);
            assertThat(gameStats.getPlayerStats().get(player1UUID).getKills()).isEqualTo(1);

            gameStats.recordPlayerStat(player1UUID, "Player", 5, 1, 1800000);
            assertThat(gameStats.getPlayerStats().get(player1UUID).getKills()).isEqualTo(5);

            gameStats.recordPlayerStat(player1UUID, "Player", 10, 2, 3600000);
            var finalStats = gameStats.getPlayerStats().get(player1UUID);
            assertThat(finalStats.getKills()).isEqualTo(10);
            assertThat(finalStats.getDeaths()).isEqualTo(2);
            assertThat(finalStats.getSurvivalTime()).isEqualTo(3600000);
        }
    }

    @Nested
    @DisplayName("Escenarios Realistas de Juego")
    class RealisticGameScenariosTests {

        @Test
        @DisplayName("Scenario: Quick match (5 minutes)")
        void scenarioQuickMatch() {
            long startTime = gameStats.getGameStartTime();
            gameStats.setGameEndTime(startTime + 300000);

            gameStats.recordPlayerStat(player1UUID, "FastWinner", 1, 0, 300000);
            gameStats.recordPlayerStat(player2UUID, "FastLoser", 0, 1, 240000);
            gameStats.setWinner("FastWinner");

            assertThat(gameStats.getFormattedDuration()).isEqualTo("00:05:00");
            assertThat(gameStats.getWinner()).isEqualTo("FastWinner");
        }

        @Test
        @DisplayName("Scenario: Epic match (3 hours)")
        void scenarioEpicMatch() {
            long startTime = gameStats.getGameStartTime();
            gameStats.setGameEndTime(startTime + 10800000);

            gameStats.recordPlayerStat(player1UUID, "Champion", 15, 3, 10800000);
            gameStats.recordPlayerStat(player2UUID, "Runner-up", 12, 4, 10500000);
            gameStats.setWinner("Champion");

            assertThat(gameStats.getFormattedDuration()).isEqualTo("03:00:00");
            assertThat(gameStats.getPlayerStats()).hasSize(2);
        }

        @Test
        @DisplayName("Scenario: Team victory")
        void scenarioTeamVictory() {
            UUID player3UUID = UUID.randomUUID();
            UUID player4UUID = UUID.randomUUID();

            gameStats.recordPlayerStat(player1UUID, "TeamA_Player1", 5, 1, 3600000);
            gameStats.recordPlayerStat(player2UUID, "TeamA_Player2", 3, 2, 3600000);
            gameStats.recordPlayerStat(player3UUID, "TeamB_Player1", 2, 3, 2400000);
            gameStats.recordPlayerStat(player4UUID, "TeamB_Player2", 1, 4, 1800000);

            gameStats.setWinner("Team A");

            assertThat(gameStats.getWinner()).isEqualTo("Team A");
            assertThat(gameStats.getPlayerStats()).hasSize(4);
        }
    }
}
