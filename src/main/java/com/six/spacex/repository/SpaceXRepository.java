package com.six.spacex.repository;

import com.six.spacex.model.*;
import java.util.*;

public class SpaceXRepository {
    private final Map<String, Rocket> rockets = new HashMap<>();
    private final Map<String, Mission> missions = new HashMap<>();

    public void addRocket(String name) {
        rockets.putIfAbsent(name, new Rocket(name));
    }

    public void addMission(String name) {
        missions.putIfAbsent(name, new Mission(name));
    }

    public Rocket getRocket(String name) {
        Rocket rocket = rockets.get(name);
        if (rocket == null) {
            throw new IllegalArgumentException("Rocket not found: " + name);
        }
        return rocket;
    }

    public Mission getMission(String name) {
        Mission mission = missions.get(name);
        if (mission == null) {
            throw new IllegalArgumentException("Mission not found: " + name);
        }
        return mission;
    }

    public void assignRocketToMission(String rocketName, String missionName) {
        Rocket rocket = getRocket(rocketName);
        Mission mission = getMission(missionName);
        mission.addRocket(rocket);
    }

    public List<Mission> getMissionSummary() {
        List<Mission> list = new ArrayList<>(missions.values());

        list.sort((a, b) -> {
            int diff = b.getAssignedRockets().size() - a.getAssignedRockets().size();
            if (diff != 0) return diff;
            return b.getName().compareToIgnoreCase(a.getName());
        });

        return list;
    }
}
