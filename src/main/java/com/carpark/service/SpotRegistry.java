package com.carpark.service;

import com.carpark.model.Entrance;
import com.carpark.model.Floor;
import com.carpark.model.SpotEntry;
import com.carpark.model.enums.SpotCategory;
import com.carpark.spots.ParkingSpot;
import com.carpark.strategy.selection.SpotSelectionPolicy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SpotRegistry {
    private final Map<Integer, Entrance> entrances = new ConcurrentHashMap<>();
    private final Map<Integer, Floor> floors = new ConcurrentHashMap<>();
    private final List<ParkingSpot> allSpots = new ArrayList<>();
    private final SpotSelectionPolicy selectionPolicy;

    public SpotRegistry(SpotSelectionPolicy selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    public ParkingSpot getClosestAvailableSpot(int entranceId, SpotCategory category) {
        Entrance entrance = entrances.get(entranceId);
        if (entrance == null) {
            throw new NoSuchElementException("Entrance ID " + entranceId + " not found.");
        }

        TreeSet<SpotEntry> nearbySpots = entrance.getSpotsByCategory(category);
        return selectionPolicy.findSpot(nearbySpots);
    }

    public Map<SpotCategory, Long> availabilityByCategory() {
        return allSpots.stream()
                .filter(spot -> !spot.isTaken())
                .collect(Collectors.groupingBy(ParkingSpot::getCategory, Collectors.counting()));
    }

    public void registerFloor(Floor floor) {
        floors.put(floor.getFloorId(), floor);
    }

    public void registerEntrance(Entrance entrance) {
        entrances.put(entrance.getEntranceId(), entrance);
    }

    public void registerSpot(ParkingSpot spot) {
        allSpots.add(spot);
    }

    public Floor getFloor(int floorId) {
        return floors.get(floorId);
    }
}
