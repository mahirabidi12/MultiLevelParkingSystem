package com.carpark.spots;

import com.carpark.model.Floor;
import com.carpark.model.enums.SpotCategory;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ParkingSpot {
    private final String spotId;
    private final double ratePerHour;
    private final SpotCategory category;
    private final AtomicBoolean taken = new AtomicBoolean(false);
    private Floor parentFloor;

    protected ParkingSpot(double ratePerHour, SpotCategory category) {
        this.spotId = UUID.randomUUID().toString();
        this.ratePerHour = ratePerHour;
        this.category = category;
    }

    public boolean reserve() {
        if (parentFloor != null && !parentFloor.isOperational()) {
            return false;
        }
        return taken.compareAndSet(false, true);
    }

    public void release() {
        taken.set(false);
    }

    public void setParentFloor(Floor floor) { this.parentFloor = floor; }

    public String getSpotId() { return spotId; }
    public double getRatePerHour() { return ratePerHour; }
    public SpotCategory getCategory() { return category; }
    public boolean isTaken() { return taken.get(); }
}
