package com.carpark.model.dto;

import java.util.ArrayList;
import java.util.List;

public class CarParkConfig {
    private int totalFloors;
    private int[] entrancesPerFloor;
    private List<List<SpotSpec>> floorSpecs = new ArrayList<>();

    public CarParkConfig setTotalFloors(int totalFloors) {
        this.totalFloors = totalFloors;
        return this;
    }

    public CarParkConfig setEntrancesPerFloor(int[] entrancesPerFloor) {
        this.entrancesPerFloor = entrancesPerFloor;
        return this;
    }

    public CarParkConfig addFloorSpecs(List<SpotSpec> specs) {
        this.floorSpecs.add(specs);
        return this;
    }

    public int getTotalFloors() { return totalFloors; }
    public int[] getEntrancesPerFloor() { return entrancesPerFloor; }
    public List<List<SpotSpec>> getFloorSpecs() { return floorSpecs; }
}
