package com.six.spacex.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mission {
    private final String name;
    private MissionStatus status;
    private final List<Rocket> assignedRockets;

    public Mission(String name) {
        this.name = name;
        this.status = MissionStatus.SCHEDULED;
        this.assignedRockets = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public List<Rocket> getAssignedRockets() {
        return Collections.unmodifiableList(assignedRockets);
    }

    public void addRocket(Rocket rocket) {
        if (status == MissionStatus.ENDED) {
            throw new IllegalStateException("Mission already ended");
        }

        assignedRockets.add(rocket);
    }

    private void updateStatus() {
        if (assignedRockets.isEmpty()) {
            status = MissionStatus.SCHEDULED;
            return;
        }

        boolean anyInRepair = assignedRockets.stream()
                .anyMatch(r -> r.getStatus() == RocketStatus.IN_REPAIR);

        status = anyInRepair ? MissionStatus.PENDING : MissionStatus.IN_PROGRESS;
    }

    public void setStatus(MissionStatus status) {
        if (status == MissionStatus.ENDED) {
            this.status = MissionStatus.ENDED;
            return;
        }
        throw new IllegalStateException("Mission status can only be ended manually");
    }
}
