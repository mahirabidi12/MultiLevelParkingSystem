package com.carpark.strategy.selection;

import com.carpark.model.SpotEntry;
import com.carpark.spots.ParkingSpot;
import java.util.TreeSet;

public interface SpotSelectionPolicy {
    ParkingSpot findSpot(TreeSet<SpotEntry> nearbySpots);
}
