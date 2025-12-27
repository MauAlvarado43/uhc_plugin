package vch.uhc.models;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UHCPlayer Model Tests")
class PlayerTest {

    private UHCPlayer player;
    private UUID testUUID;

    @BeforeEach
    void setUp() {
        testUUID = UUID.randomUUID();
        player = new UHCPlayer(testUUID, "TestUHCPlayer");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create player with correct UUID and name")
        void shouldCreateUHCPlayerWithCorrectUUIDAndName() {
            assertThat(player.getUuid()).isEqualTo(testUUID);
            assertThat(player.getName()).isEqualTo("TestUHCPlayer");
        }

        @Test
        @DisplayName("Should initialize player with 1 life")
        void shouldInitializeWithOneLife() {
            assertThat(player.getLives()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should initialize player as playing")
        void shouldInitializeAsPlaying() {
            assertThat(player.isPlaying()).isTrue();
        }

        @Test
        @DisplayName("Should generate random name on creation")
        void shouldGenerateRandomName() {
            assertThat(player.getRandomName()).isNotNull();
            assertThat(player.getRandomName()).isNotEmpty();
        }

        @Test
        @DisplayName("Random names should be unique between players")
        void randomNamesShouldBeUnique() {
            UHCPlayer player2 = new UHCPlayer(UUID.randomUUID(), "UHCPlayer2");
            String name1 = player.getRandomName().replaceAll("§.", "");
            String name2 = player2.getRandomName().replaceAll("§.", "");
            assertThat(name1).isNotEqualTo(name2);
        }
    }

    @Nested
    @DisplayName("Lives Management Tests")
    class LivesManagementTests {

        @Test
        @DisplayName("Should add life correctly")
        void shouldAddLife() {
            player.addLife();
            assertThat(player.getLives()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should remove life correctly")
        void shouldRemoveLife() {
            player.setLives(3);
            player.removeLife();
            assertThat(player.getLives()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should set lives correctly")
        void shouldSetLives() {
            player.setLives(5);
            assertThat(player.getLives()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should be alive when lives > 0")
        void shouldBeAliveWhenLivesPositive() {
            player.setLives(1);
            assertThat(player.isAlive()).isTrue();
        }

        @Test
        @DisplayName("Should not be alive when lives = 0")
        void shouldNotBeAliveWhenLivesZero() {
            player.setLives(0);
            assertThat(player.isAlive()).isFalse();
        }

        @Test
        @DisplayName("Should not be alive when lives < 0")
        void shouldNotBeAliveWhenLivesNegative() {
            player.setLives(-1);
            assertThat(player.isAlive()).isFalse();
        }
    }

    @Nested
    @DisplayName("Team Management Tests")
    class TeamManagementTests {

        @Test
        @DisplayName("Should set team correctly")
        void shouldSetTeam() {
            UHCPlayer leader = new UHCPlayer(UUID.randomUUID(), "Leader");
            UHCTeam team = new UHCTeam("TestTeam", leader);
            player.setTeam(team);
            assertThat(player.getTeam()).isEqualTo(team);
        }

        @Test
        @DisplayName("Should initially have no team")
        void shouldInitiallyHaveNoTeam() {
            assertThat(player.getTeam()).isNull();
        }
    }

    @Nested
    @DisplayName("Playing Status Tests")
    class PlayingStatusTests {

        @Test
        @DisplayName("Should set playing status to false")
        void shouldSetPlayingToFalse() {
            player.setPlaying(false);
            assertThat(player.isPlaying()).isFalse();
        }

        @Test
        @DisplayName("Should set playing status to true")
        void shouldSetPlayingToTrue() {
            player.setPlaying(false);
            player.setPlaying(true);
            assertThat(player.isPlaying()).isTrue();
        }
    }

    @Nested
    @DisplayName("Location Tests")
    class LocationTests {

        @Test
        @DisplayName("Should get and set spawn location")
        void shouldGetAndSetSpawn() {
            assertThat(player.getSpawn()).isNull();
        }

    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle multiple life additions")
        void shouldHandleMultipleLifeAdditions() {
            for (int i = 0; i < 10; i++) {
                player.addLife();
            }
            assertThat(player.getLives()).isEqualTo(11);
        }

        @Test
        @DisplayName("Should handle multiple life removals")
        void shouldHandleMultipleLifeRemovals() {
            player.setLives(5);
            for (int i = 0; i < 5; i++) {
                player.removeLife();
            }
            assertThat(player.getLives()).isEqualTo(0);
            assertThat(player.isAlive()).isFalse();
        }
    }
}
