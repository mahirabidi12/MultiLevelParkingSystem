package com.carpark.spots;

import com.carpark.model.enums.SpotCategory;

public class SmallSpot extends ParkingSpot {
    public SmallSpot(double ratePerHour) {
        super(ratePerHour, SpotCategory.SMALL);
    }
}
