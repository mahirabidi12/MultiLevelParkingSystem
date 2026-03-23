package com.carpark.spots;

import com.carpark.model.enums.SpotCategory;

public class RegularSpot extends ParkingSpot {
    public RegularSpot(double ratePerHour) {
        super(ratePerHour, SpotCategory.REGULAR);
    }
}
