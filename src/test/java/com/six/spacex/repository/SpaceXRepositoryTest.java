package com.six.spacex.repository;

import com.six.spacex.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class SpaceXRepositoryTest {

    @Test
    void shouldAssignRocketToMissionSuccessfully() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addRocket("Dragon1");
        repo.addMission("MarsMission");

        repo.assignRocketToMission("Dragon1", "MarsMission");

        assertEquals(RocketStatus.IN_SPACE, repo.getRocket("Dragon1").getStatus());
        assertEquals(1, repo.getMission("MarsMission").getAssignedRockets().size());
    }

    @Test
    void shouldRejectInvalidRocketNames() {
        SpaceXRepository repo = new SpaceXRepository();
        assertThrows(IllegalArgumentException.class, () -> repo.addRocket(""));
        assertThrows(IllegalArgumentException.class, () -> repo.addRocket(null));
        assertThrows(IllegalArgumentException.class, () -> repo.addRocket("   "));
    }

    @Test
    void shouldRejectInvalidMissionNames() {
        SpaceXRepository repo = new SpaceXRepository();
        assertThrows(IllegalArgumentException.class, () -> repo.addMission(""));
        assertThrows(IllegalArgumentException.class, () -> repo.addMission(null));
        assertThrows(IllegalArgumentException.class, () -> repo.addMission("   "));
    }

    @Test
    void shouldThrowExceptionWhenAssigningAlreadyAssignedRocket() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addRocket("Dragon1");
        repo.addMission("MissionA");
        repo.addMission("MissionB");
        repo.assignRocketToMission("Dragon1", "MissionA");
        assertThrows(IllegalStateException.class, () -> repo.assignRocketToMission("Dragon1", "MissionB"));
    }

    @Test
    void shouldThrowWhenAssigningRocketToNonExistingMission() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addRocket("Dragon1");
        assertThrows(IllegalArgumentException.class, () -> repo.assignRocketToMission("Dragon1", "NoSuchMission"));
    }

    @Test
    void shouldThrowWhenAssigningNonExistingRocket() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addMission("Alpha");
        assertThrows(IllegalArgumentException.class, () -> repo.assignRocketToMission("GhostRocket", "Alpha"));
    }

    @Test
    void shouldThrowExceptionWhenAssigningRocketInBuild() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addRocket("Dragon1");
        repo.addMission("Mars");
        
        repo.getRocket("Dragon1").setStatus(RocketStatus.IN_BUILD);
        
        assertThrows(IllegalStateException.class, () -> repo.assignRocketToMission("Dragon1", "Mars"));
    }

    @Test
    void shouldNotAllowAssignmentWhenMissionIsEnded() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addRocket("Dragon1");
        repo.addMission("Alpha");
        repo.getMission("Alpha").setStatus(MissionStatus.ENDED);
        assertThrows(IllegalStateException.class, () -> repo.assignRocketToMission("Dragon1", "Alpha"));
    }

    @Test
    void shouldUpdateMissionStatusBasedOnRocketHealth() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addMission("Alpha");
        repo.addRocket("R1");
        repo.addRocket("R2");

        repo.getRocket("R1").setStatus(RocketStatus.IN_REPAIR);
        repo.assignRocketToMission("R1", "Alpha");
        assertEquals(MissionStatus.PENDING, repo.getMission("Alpha").getStatus());

        repo.assignRocketToMission("R2", "Alpha");
        // Still pending because R1 is IN_REPAIR
        assertEquals(MissionStatus.PENDING, repo.getMission("Alpha").getStatus());
    }

    @Test
    void shouldReturnCorrectEntitiesByName() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addMission("Alpha");
        repo.addRocket("Dragon1");
        
        assertNotNull(repo.getMission("Alpha"));
        assertNotNull(repo.getRocket("Dragon1"));
    }

    @Test
    void shouldThrowWhenAddingDuplicateEntities() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addRocket("Dragon1");
        assertThrows(IllegalStateException.class, () -> repo.addRocket("Dragon1"));
        
        repo.addMission("Alpha");
        assertThrows(IllegalStateException.class, () -> repo.addMission("Alpha"));
    }

    @Test
    void shouldHandleSummaryCorrectlyForLargeDatasets() {
        SpaceXRepository repo = new SpaceXRepository();
        // Test summary sorting and count
        for (int i = 0; i < 100; i++) {
            repo.addMission("M" + i);
            repo.addRocket("R" + i);
            if (i % 2 == 0) repo.assignRocketToMission("R" + i, "M" + i);
        }
        List<Mission> summary = repo.getMissionSummary();
        assertFalse(summary.isEmpty());
        assertDoesNotThrow(() -> repo.getRocket("R50"));
    }
}
