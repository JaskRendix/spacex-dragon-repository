package com.six.spacex.repository;

import com.six.spacex.model.*;
import java.util.*;

public class SpaceXRepository {
    private final Map<String, Rocket> rockets = new HashMap<>();
    private final Map<String, Mission> missions = new HashMap<>();

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    public void addRocket(String name) {
        validateName(name);
        if (rockets.containsKey(name)) {
            throw new IllegalStateException("Rocket already exists: " + name);
        }
        rockets.put(name, new Rocket(name));
    }

    public Rocket getRocket(String name) {
        Rocket rocket = rockets.get(name);
        if (rocket == null) {
            throw new IllegalArgumentException("Rocket not found: " + name);
        }
        return rocket;
    }

    public void addMission(String name) {
        validateName(name);
        if (missions.containsKey(name)) {
            throw new IllegalStateException("Mission already exists: " + name);
        }
        missions.put(name, new Mission(name));
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

        if (rocket.getStatus() == RocketStatus.IN_SPACE) {
            throw new IllegalStateException("Rocket " + rocketName + " is already assigned to a mission.");
        }

        if (mission.getStatus() == MissionStatus.ENDED) {
            throw new IllegalStateException("Mission " + missionName + " has ended and cannot accept new rockets.");
        }

        mission.addRocket(rocket);
        rocket.setStatus(RocketStatus.IN_SPACE);
    }

    public List<Mission> getMissionSummary() {
        List<Mission> list = new ArrayList<>(missions.values());

        list.sort((a, b) -> {
            int diff = b.getAssignedRockets().size() - a.getAssignedRockets().size();
            if (diff != 0) return diff;

            try {
                int numA = Integer.parseInt(a.getName().substring(1));
                int numB = Integer.parseInt(b.getName().substring(1));
                return Integer.compare(numB, numA);
            } catch (Exception e) {
                return b.getName().compareToIgnoreCase(a.getName());
            }
        });

        return list;
    }
}
