package com.carpark.spots;

import com.carpark.model.enums.SpotCategory;

public class MediumSpot extends ParkingSpot {
    public MediumSpot(double ratePerHour) {
        super(ratePerHour, SpotCategory.MEDIUM);
    }
}
