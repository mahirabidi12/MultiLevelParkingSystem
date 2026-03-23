package com.carpark.model;

import com.carpark.spots.ParkingSpot;

public class SpotEntry implements Comparable<SpotEntry> {
    private final int distance;
    private final ParkingSpot spot;

    public SpotEntry(ParkingSpot spot, int distance) {
        this.spot = spot;
        this.distance = distance;
    }

    public int getDistance() { return distance; }
    public ParkingSpot getSpot() { return spot; }

    @Override
    public int compareTo(SpotEntry other) {
        int cmp = Integer.compare(this.distance, other.distance);
        if (cmp != 0) return cmp;
        return this.spot.getSpotId().compareTo(other.spot.getSpotId());
    }
}
