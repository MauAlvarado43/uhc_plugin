package vch.uhc.models;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Team Model Tests")
class TeamTest {

    private Team team;
    private Player leader;
    private Player member1;
    private Player member2;

    @BeforeEach
    void setUp() {
        leader = new Player(UUID.randomUUID(), "Leader");
        member1 = new Player(UUID.randomUUID(), "Member1");
        member2 = new Player(UUID.randomUUID(), "Member2");
        team = new Team("TestTeam", leader);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create team with correct name")
        void shouldCreateTeamWithCorrectName() {
            assertThat(team.getName()).isEqualTo("TestTeam");
        }

        @Test
        @DisplayName("Should create team with correct leader")
        void shouldCreateTeamWithCorrectLeader() {
            assertThat(team.getLeader()).isEqualTo(leader);
        }

        @Test
        @DisplayName("Should initialize with empty members list")
        void shouldInitializeWithEmptyMembers() {
            assertThat(team.getMembers()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Team Name Tests")
    class TeamNameTests {

        @Test
        @DisplayName("Should change team name")
        void shouldChangeTeamName() {
            team.setName("NewTeamName");
            assertThat(team.getName()).isEqualTo("NewTeamName");
        }

        @Test
        @DisplayName("Should accept empty name")
        void shouldAcceptEmptyName() {
            team.setName("");
            assertThat(team.getName()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Leader Management Tests")
    class LeaderManagementTests {

        @Test
        @DisplayName("Should change leader")
        void shouldChangeLeader() {
            team.setLeader(member1);
            assertThat(team.getLeader()).isEqualTo(member1);
        }

        @Test
        @DisplayName("Leader can be different from members")
        void leaderCanBeDifferentFromMembers() {
            team.addMember(member1);
            assertThat(team.getLeader()).isNotEqualTo(member1);
            assertThat(team.getMembers()).contains(member1);
        }
    }

    @Nested
    @DisplayName("Member Management Tests")
    class MemberManagementTests {

        @Test
        @DisplayName("Should add member to team")
        void shouldAddMember() {
            team.addMember(member1);
            assertThat(team.getMembers()).containsExactly(member1);
        }

        @Test
        @DisplayName("Should add multiple members")
        void shouldAddMultipleMembers() {
            team.addMember(member1);
            team.addMember(member2);
            assertThat(team.getMembers()).containsExactly(member1, member2);
        }

        @Test
        @DisplayName("Should not add duplicate member")
        void shouldNotAddDuplicateMember() {
            team.addMember(member1);
            team.addMember(member1);
            assertThat(team.getMembers()).hasSize(1);
            assertThat(team.getMembers()).containsExactly(member1);
        }

        @Test
        @DisplayName("Should remove member from team")
        void shouldRemoveMember() {
            team.addMember(member1);
            team.addMember(member2);
            team.removeMember(member1);
            assertThat(team.getMembers()).containsExactly(member2);
        }

        @Test
        @DisplayName("Should handle removing non-existent member")
        void shouldHandleRemovingNonExistentMember() {
            team.addMember(member1);
            team.removeMember(member2);
            assertThat(team.getMembers()).containsExactly(member1);
        }

        @Test
        @DisplayName("Should remove all instances when removing member")
        void shouldRemoveAllInstancesWhenRemovingMember() {
            team.addMember(member1);
            team.removeMember(member1);
            assertThat(team.getMembers()).doesNotContain(member1);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle leader being added as member")
        void shouldHandleLeaderAsemberMember() {
            team.addMember(leader);
            assertThat(team.getMembers()).containsExactly(leader);
            assertThat(team.getLeader()).isEqualTo(leader);
        }

        @Test
        @DisplayName("Should handle removing all members")
        void shouldHandleRemovingAllMembers() {
            team.addMember(member1);
            team.addMember(member2);
            team.removeMember(member1);
            team.removeMember(member2);
            assertThat(team.getMembers()).isEmpty();
        }

        @Test
        @DisplayName("Should maintain member list integrity after multiple operations")
        void shouldMaintainMemberListIntegrity() {
            team.addMember(member1);
            team.addMember(member2);
            team.removeMember(member1);
            team.addMember(member1);
            assertThat(team.getMembers()).hasSize(2);
            assertThat(team.getMembers()).contains(member1, member2);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Team should work with player team assignment")
        void teamShouldWorkWithPlayerTeamAssignment() {
            member1.setTeam(team);
            team.addMember(member1);
            
            assertThat(member1.getTeam()).isEqualTo(team);
            assertThat(team.getMembers()).contains(member1);
        }

        @Test
        @DisplayName("Should handle team switching")
        void shouldHandleTeamSwitching() {
            Player newLeader = new Player(UUID.randomUUID(), "NewLeader");
            Team team2 = new Team("Team2", newLeader);
            
            member1.setTeam(team);
            team.addMember(member1);
            
            team.removeMember(member1);
            team2.addMember(member1);
            member1.setTeam(team2);
            
            assertThat(team.getMembers()).doesNotContain(member1);
            assertThat(team2.getMembers()).contains(member1);
            assertThat(member1.getTeam()).isEqualTo(team2);
        }
    }
}
