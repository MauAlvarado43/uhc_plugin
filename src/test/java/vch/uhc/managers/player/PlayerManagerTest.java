package vch.uhc.managers.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import vch.uhc.models.UHCPlayer;
import vch.uhc.managers.player.PlayerManager;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PlayerManager Tests")
class PlayerManagerTest {

    private PlayerManager playerManager;
    private UHCPlayer player1;
    private UHCPlayer player2;
    private UUID uuid1;
    private UUID uuid2;

    @BeforeEach
    void setUp() {
        playerManager = new PlayerManager();
        uuid1 = UUID.randomUUID();
        uuid2 = UUID.randomUUID();
        player1 = new UHCPlayer(uuid1, "Player1");
        player2 = new UHCPlayer(uuid2, "Player2");
    }

    @Nested
    @DisplayName("Add Player Tests")
    class AddPlayerTests {

        @Test
        @DisplayName("Should add player successfully")
        void shouldAddPlayer() {
            playerManager.addPlayer(player1);
            assertThat(playerManager.getPlayers()).contains(player1);
        }

        @Test
        @DisplayName("Should add multiple players")
        void shouldAddMultiplePlayers() {
            playerManager.addPlayer(player1);
            playerManager.addPlayer(player2);
            assertThat(playerManager.getPlayers()).containsExactly(player1, player2);
        }

        @Test
        @DisplayName("Should allow adding same player multiple times")
        void shouldAllowAddingSamePlayerMultipleTimes() {
            playerManager.addPlayer(player1);
            playerManager.addPlayer(player1);
        }
    }

    @Nested
    @DisplayName("Remove Player Tests")
    class RemovePlayerTests {

        @Test
        @DisplayName("Should remove player successfully")
        void shouldRemovePlayer() {
            playerManager.addPlayer(player1);
            playerManager.addPlayer(player2);
            playerManager.removePlayer(player1);
            assertThat(playerManager.getPlayers()).doesNotContain(player1);
            assertThat(playerManager.getPlayers()).contains(player2);
        }

        @Test
        @DisplayName("Should handle removing non-existent player")
        void shouldHandleRemovingNonExistentPlayer() {
            playerManager.addPlayer(player1);
            playerManager.removePlayer(player2);
            assertThat(playerManager.getPlayers()).contains(player1);
        }

        @Test
        @DisplayName("Should handle removing from empty list")
        void shouldHandleRemovingFromEmptyList() {
            assertThatCode(() -> playerManager.removePlayer(player1))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Get Players Tests")
    class GetPlayersTests {

        @Test
        @DisplayName("Should return empty list when no players added")
        void shouldReturnEmptyListWhenNoPlayers() {
            assertThat(playerManager.getPlayers()).isEmpty();
        }

        @Test
        @DisplayName("Should only return playing players")
        void shouldOnlyReturnPlayingPlayers() {
            player1.setPlaying(true);
            player2.setPlaying(false);
            
            playerManager.addPlayer(player1);
            playerManager.addPlayer(player2);
            
            assertThat(playerManager.getPlayers()).containsExactly(player1);
            assertThat(playerManager.getPlayers()).doesNotContain(player2);
        }

        @Test
        @DisplayName("Should filter out non-playing players")
        void shouldFilterOutNonPlayingPlayers() {
            playerManager.addPlayer(player1);
            playerManager.addPlayer(player2);
            
            player1.setPlaying(false);
            
            assertThat(playerManager.getPlayers()).containsExactly(player2);
        }

        @Test
        @DisplayName("Should return all players when all are playing")
        void shouldReturnAllPlayersWhenAllPlaying() {
            player1.setPlaying(true);
            player2.setPlaying(true);
            
            playerManager.addPlayer(player1);
            playerManager.addPlayer(player2);
            
            assertThat(playerManager.getPlayers()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Get Player By UUID Tests")
    class GetPlayerByUUIDTests {

        @Test
        @DisplayName("Should return existing player by UUID")
        void shouldReturnExistingPlayerByUUID() {
            playerManager.addPlayer(player1);
            UHCPlayer found = playerManager.getPlayerByUUID(uuid1);
            assertThat(found).isEqualTo(player1);
        }

        @Test
        @DisplayName("Should find correct player among multiple")
        void shouldFindCorrectPlayerAmongMultiple() {
            playerManager.addPlayer(player1);
            playerManager.addPlayer(player2);
            UHCPlayer found = playerManager.getPlayerByUUID(uuid2);
            assertThat(found).isEqualTo(player2);
        }

        @Test
        @DisplayName("Should return player even if not playing")
        void shouldReturnPlayerEvenIfNotPlaying() {
            player1.setPlaying(false);
            playerManager.addPlayer(player1);
            UHCPlayer found = playerManager.getPlayerByUUID(uuid1);
            assertThat(found).isEqualTo(player1);
        }

    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle player status changes")
        void shouldHandlePlayerStatusChanges() {
            playerManager.addPlayer(player1);
            playerManager.addPlayer(player2);
            
            assertThat(playerManager.getPlayers()).hasSize(2);
            
            player1.setPlaying(false);
            assertThat(playerManager.getPlayers()).hasSize(1);
            
            player1.setPlaying(true);
            assertThat(playerManager.getPlayers()).hasSize(2);
        }

        @Test
        @DisplayName("Should maintain correct count after additions and removals")
        void shouldMaintainCorrectCountAfterAdditionsAndRemovals() {
            playerManager.addPlayer(player1);
            playerManager.addPlayer(player2);
            assertThat(playerManager.getPlayers()).hasSize(2);
            
            playerManager.removePlayer(player1);
            assertThat(playerManager.getPlayers()).hasSize(1);
            
            UHCPlayer player3 = new UHCPlayer(UUID.randomUUID(), "Player3");
            playerManager.addPlayer(player3);
            assertThat(playerManager.getPlayers()).hasSize(2);
        }
    }
}
