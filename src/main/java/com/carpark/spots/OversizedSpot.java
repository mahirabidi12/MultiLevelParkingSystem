package com.carpark.spots;

import com.carpark.model.enums.SpotCategory;

public class OversizedSpot extends ParkingSpot {
    public OversizedSpot(double ratePerHour) {
        super(ratePerHour, SpotCategory.OVERSIZED);
    }
}
