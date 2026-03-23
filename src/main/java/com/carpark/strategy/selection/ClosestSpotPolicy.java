package com.carpark.strategy.selection;

import com.carpark.model.SpotEntry;
import com.carpark.spots.ParkingSpot;
import java.util.TreeSet;

public class ClosestSpotPolicy implements SpotSelectionPolicy {

    @Override
    public ParkingSpot findSpot(TreeSet<SpotEntry> nearbySpots) {
        if (nearbySpots == null || nearbySpots.isEmpty()) {
            return null;
        }

        for (SpotEntry entry : nearbySpots) {
            ParkingSpot spot = entry.getSpot();
            if (spot.reserve()) {
                System.out.println("Assigned spot at distance: " + entry.getDistance());
                return spot;
            }
        }

        return null;
    }
}
