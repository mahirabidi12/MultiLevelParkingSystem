package com.carpark.model;

import com.carpark.model.enums.SpotCategory;

public class Car {
    private final String plateNumber;
    private final SpotCategory category;

    public Car(String plateNumber, SpotCategory category) {
        this.plateNumber = plateNumber;
        this.category = category;
    }

    public String getPlateNumber() { return plateNumber; }
    public SpotCategory getCategory() { return category; }
}
