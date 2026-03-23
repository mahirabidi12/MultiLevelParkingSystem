package com.carpark.spots;

import com.carpark.model.enums.SpotCategory;

public class CompactSpot extends ParkingSpot {
    public CompactSpot(double ratePerHour) {
        super(ratePerHour, SpotCategory.COMPACT);
    }
}
