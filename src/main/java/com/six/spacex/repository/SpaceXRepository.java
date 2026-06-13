package com.six.spacex.repository;

import com.six.spacex.model.*;
import java.util.*;

public class SpaceXRepository {
    private final Map<String, Rocket> rockets = new HashMap<>();
    private final Map<String, Mission> missions = new HashMap<>();

    public void addRocket(String name) {
        rockets.put(name, new Rocket(name));
    }

    public void addMission(String name) {
        missions.put(name, new Mission(name));
    }

    public Rocket getRocket(String name) {
        return rockets.get(name);
    }

    public Mission getMission(String name) {
        return missions.get(name);
    }
}
