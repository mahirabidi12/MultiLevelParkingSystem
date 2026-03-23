package com.carpark.model;

import com.carpark.spots.ParkingSpot;
import java.util.ArrayList;
import java.util.List;

public class Floor {
    private final int floorId;
    private final List<ParkingSpot> spots = new ArrayList<>();
    private final List<Entrance> entrances = new ArrayList<>();
    private volatile boolean operational = true;

    public Floor(int floorId) {
        this.floorId = floorId;
    }

    public void addSpot(ParkingSpot spot) {
        spot.setParentFloor(this);
        spots.add(spot);
    }

    public void addEntrance(Entrance entrance) {
        entrances.add(entrance);
    }

    public boolean isOperational() { return operational; }
    public void setOperational(boolean operational) { this.operational = operational; }

    public List<ParkingSpot> getSpots() { return spots; }
    public int getFloorId() { return floorId; }
}
