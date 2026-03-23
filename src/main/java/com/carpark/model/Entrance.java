package com.carpark.model;

import com.carpark.model.enums.SpotCategory;
import com.carpark.spots.ParkingSpot;
import java.util.EnumMap;
import java.util.Map;
import java.util.TreeSet;

public class Entrance {
    private final int entranceId;
    private final Map<SpotCategory, TreeSet<SpotEntry>> spotsByCategory = new EnumMap<>(SpotCategory.class);

    public Entrance(int entranceId) {
        this.entranceId = entranceId;
        for (SpotCategory cat : SpotCategory.values()) {
            spotsByCategory.put(cat, new TreeSet<>());
        }
    }

    public void addSpotDistance(ParkingSpot spot, int distance) {
        spotsByCategory.get(spot.getCategory()).add(new SpotEntry(spot, distance));
    }

    public TreeSet<SpotEntry> getSpotsByCategory(SpotCategory category) {
        return spotsByCategory.get(category);
    }

    public int getEntranceId() { return entranceId; }
}
