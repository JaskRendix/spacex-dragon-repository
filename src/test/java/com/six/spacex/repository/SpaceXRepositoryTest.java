package com.six.spacex.repository;

import com.six.spacex.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
    void shouldThrowExceptionWhenAssigningAlreadyAssignedRocket() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addRocket("Dragon1");
        repo.addMission("MissionA");
        repo.addMission("MissionB");

        repo.assignRocketToMission("Dragon1", "MissionA");

        assertThrows(IllegalStateException.class, () -> 
            repo.assignRocketToMission("Dragon1", "MissionB")
        );
    }

    @Test
    void shouldThrowWhenAssigningRocketToNonExistingMission() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addRocket("Dragon1");

        assertThrows(IllegalArgumentException.class, () ->
            repo.assignRocketToMission("Dragon1", "NoSuchMission")
        );
    }

    @Test
    void shouldThrowWhenAssigningNonExistingRocket() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addMission("Alpha");
    
        assertThrows(IllegalArgumentException.class, () ->
            repo.assignRocketToMission("GhostRocket", "Alpha")
        );
    }

    @Test
    void shouldNotAllowAssignmentWhenMissionIsEnded() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addRocket("Dragon1");
        repo.addMission("Alpha");
    
        repo.getMission("Alpha").setStatus(MissionStatus.ENDED);
    
        assertThrows(IllegalStateException.class, () ->
            repo.assignRocketToMission("Dragon1", "Alpha")
        );
    }

    @Test
    void shouldSetMissionStatusToPendingWhenRocketIsInRepair() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addRocket("Dragon1");
        repo.addMission("Alpha");
    
        repo.getRocket("Dragon1").setStatus(RocketStatus.IN_REPAIR);
    
        repo.assignRocketToMission("Dragon1", "Alpha");
    
        assertEquals(MissionStatus.PENDING, repo.getMission("Alpha").getStatus());
    }

    @Test
    void shouldSetMissionStatusToInProgressWhenRocketIsHealthy() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addRocket("Dragon1");
        repo.addMission("Alpha");
    
        repo.assignRocketToMission("Dragon1", "Alpha");
    
        assertEquals(MissionStatus.IN_PROGRESS, repo.getMission("Alpha").getStatus());
    }

    @Test
    void shouldReturnMissionByName() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addMission("Alpha");
    
        Mission m = repo.getMission("Alpha");
    
        assertNotNull(m);
        assertEquals("Alpha", m.getName());
    }

    @Test
    void shouldReturnRocketByName() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addRocket("Dragon1");
    
        Rocket r = repo.getRocket("Dragon1");
    
        assertNotNull(r);
        assertEquals("Dragon1", r.getName());
    }

    @Test
    void shouldThrowWhenAddingDuplicateRocket() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addRocket("Dragon1");
    
        assertThrows(IllegalStateException.class, () ->
            repo.addRocket("Dragon1")
        );
    }

    @Test
    void shouldHandleEmptyRepositorySummary() {
        SpaceXRepository repo = new SpaceXRepository();
    
        List<Mission> summary = repo.getMissionSummary();
    
        assertTrue(summary.isEmpty());
    }

    @Test
    void shouldAllowManyRocketsWithoutAssignments() {
        SpaceXRepository repo = new SpaceXRepository();
    
        for (int i = 0; i < 1000; i++) {
            repo.addRocket("R" + i);
        }
    
        assertNotNull(repo.getRocket("R500"));
        assertEquals("R500", repo.getRocket("R500").getName());
    }

    @Test
    void shouldAllowManyMissionsWithoutAssignments() {
        SpaceXRepository repo = new SpaceXRepository();
    
        for (int i = 0; i < 500; i++) {
            repo.addMission("M" + i);
        }
    
        assertEquals(500, repo.getMissionSummary().size());
    }

    @Test
    void shouldRejectEmptyRocketName() {
        SpaceXRepository repo = new SpaceXRepository();
    
        assertThrows(IllegalArgumentException.class, () ->
            repo.addRocket("")
        );
    }

    @Test
    void shouldRejectEmptyMissionName() {
        SpaceXRepository repo = new SpaceXRepository();
    
        assertThrows(IllegalArgumentException.class, () ->
            repo.addMission("")
        );
    }

    @Test
    void shouldRejectNullRocketName() {
        SpaceXRepository repo = new SpaceXRepository();
    
        assertThrows(IllegalArgumentException.class, () ->
            repo.addRocket(null)
        );
    }

    @Test
    void shouldRejectNullMissionName() {
        SpaceXRepository repo = new SpaceXRepository();
    
        assertThrows(IllegalArgumentException.class, () ->
            repo.addMission(null)
        );
    }

    @Test
    void shouldRejectWhitespaceOnlyRocketName() {
        SpaceXRepository repo = new SpaceXRepository();
    
        assertThrows(IllegalArgumentException.class, () ->
            repo.addRocket("   ")
        );
    }

    @Test
    void shouldRejectWhitespaceOnlyMissionName() {
        SpaceXRepository repo = new SpaceXRepository();
    
        assertThrows(IllegalArgumentException.class, () ->
            repo.addMission("   ")
        );
    }

    @Test
    void summaryShouldSortLargeDatasetCorrectly() {
        SpaceXRepository repo = new SpaceXRepository();
    
        repo.addMission("A");
        repo.addMission("B");
        repo.addMission("C");
    
        repo.addRocket("R1");
        repo.addRocket("R2");
        repo.addRocket("R3");
    
        repo.assignRocketToMission("R1", "B");
        repo.assignRocketToMission("R2", "B");
        repo.assignRocketToMission("R3", "A");
    
        List<Mission> summary = repo.getMissionSummary();
    
        assertEquals("B", summary.get(0).getName());
        assertEquals("A", summary.get(1).getName());
        assertEquals("C", summary.get(2).getName());
    }

    @Test
    void shouldHandleMissionWithManyRockets() {
        SpaceXRepository repo = new SpaceXRepository();
        repo.addMission("Mega");
    
        for (int i = 0; i < 500; i++) {
            repo.addRocket("R" + i);
            repo.assignRocketToMission("R" + i, "Mega");
        }
    
        assertEquals(500, repo.getMission("Mega").getAssignedRockets().size());
    }

    @Test
    void shouldSortHundredsOfMissionsCorrectly() {
        SpaceXRepository repo = new SpaceXRepository();
    
        for (int i = 0; i < 200; i++) {
            repo.addMission("M" + i);
            repo.addRocket("R" + i);
            repo.assignRocketToMission("R" + i, "M" + i);
        }
    
        List<Mission> summary = repo.getMissionSummary();
    
        assertEquals("M199", summary.get(0).getName());
    }

    @Test
    void shouldLookupManyRocketsQuickly() {
        SpaceXRepository repo = new SpaceXRepository();
    
        for (int i = 0; i < 2000; i++) {
            repo.addRocket("R" + i);
        }
    
        assertDoesNotThrow(() -> repo.getRocket("R1500"));
    }

    @Test
    void shouldRemainConsistentAfterInterleavedOperations() {
        SpaceXRepository repo = new SpaceXRepository();
    
        for (int i = 0; i < 300; i++) {
            repo.addRocket("R" + i);
            if (i % 3 == 0) repo.addMission("M" + i);
        }
    
        repo.assignRocketToMission("R0", "M0");
        repo.assignRocketToMission("R3", "M3");
    
        assertEquals(1, repo.getMission("M0").getAssignedRockets().size());
        assertEquals(1, repo.getMission("M3").getAssignedRockets().size());
    }

    @Test
    void shouldSetMissionPendingWhenAnyRocketIsInRepair() {
        SpaceXRepository repo = new SpaceXRepository();
    
        repo.addMission("X");
        repo.addRocket("A");
        repo.addRocket("B");
    
        Rocket a = repo.getRocket("A");
        Rocket b = repo.getRocket("B");
    
        a.setStatus(RocketStatus.IN_REPAIR);
    
        repo.assignRocketToMission("A", "X");
        repo.assignRocketToMission("B", "X");
    
        assertEquals(MissionStatus.PENDING, repo.getMission("X").getStatus());
    }

    @Test
    void shouldKeepMissionInProgressWhenAllRocketsHealthy() {
        SpaceXRepository repo = new SpaceXRepository();
    
        repo.addMission("X");
        repo.addRocket("A");
        repo.addRocket("B");
    
        repo.assignRocketToMission("A", "X");
        repo.assignRocketToMission("B", "X");
    
        assertEquals(MissionStatus.IN_PROGRESS, repo.getMission("X").getStatus());
    }

    @Test
    void shouldNotAllowAssignmentsAfterMissionEnded() {
        SpaceXRepository repo = new SpaceXRepository();
    
        repo.addMission("X");
        repo.addRocket("A");
    
        Mission m = repo.getMission("X");
        m.setStatus(MissionStatus.ENDED);
    
        assertThrows(IllegalStateException.class, () ->
            repo.assignRocketToMission("A", "X")
        );
    }
}
