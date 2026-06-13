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
}
