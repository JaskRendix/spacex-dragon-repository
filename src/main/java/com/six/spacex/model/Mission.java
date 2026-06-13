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

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    public List<Rocket> getAssignedRockets() {
        return Collections.unmodifiableList(assignedRockets);
    }

    public void addRocket(Rocket rocket) {
        assignedRockets.add(rocket);

        if (rocket.getStatus() == RocketStatus.IN_REPAIR) {
            this.status = MissionStatus.PENDING;
        } else {
            this.status = MissionStatus.IN_PROGRESS;
        }
    }
}
