package com.carpark.model.dto;

import com.carpark.model.enums.SpotCategory;

public class SpotSpec {
    public final SpotCategory category;
    public final double ratePerHour;
    public final int[] distancesFromEntrances;

    public SpotSpec(SpotCategory category, double ratePerHour, int[] distancesFromEntrances) {
        this.category = category;
        this.ratePerHour = ratePerHour;
        this.distancesFromEntrances = distancesFromEntrances;
    }
}
