package com.carpark.spots;

import com.carpark.model.enums.SpotCategory;

public class LargeSpot extends ParkingSpot {
    public LargeSpot(double ratePerHour) {
        super(ratePerHour, SpotCategory.LARGE);
    }
}
