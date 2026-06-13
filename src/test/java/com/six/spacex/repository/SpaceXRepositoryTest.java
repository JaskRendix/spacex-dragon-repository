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
}
