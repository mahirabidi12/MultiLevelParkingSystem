package com.carpark.assembler;

import com.carpark.controller.CarPark;
import com.carpark.model.Entrance;
import com.carpark.model.Floor;
import com.carpark.model.dto.CarParkConfig;
import com.carpark.model.dto.SpotSpec;
import com.carpark.model.enums.SpotCategory;
import com.carpark.service.FeeProcessor;
import com.carpark.service.SpotRegistry;
import com.carpark.spots.CompactSpot;
import com.carpark.spots.OversizedSpot;
import com.carpark.spots.ParkingSpot;
import com.carpark.spots.RegularSpot;
import com.carpark.strategy.pricing.HourlyChargePolicy;
import com.carpark.strategy.selection.ClosestSpotPolicy;

import java.util.ArrayList;
import java.util.List;

public class CarParkAssembler {

    public static CarPark assemble(CarParkConfig config) {
        SpotRegistry registry = new SpotRegistry(new ClosestSpotPolicy());
        List<Entrance> allEntrances = new ArrayList<>();

        for (int i = 0; i < config.getTotalFloors(); i++) {
            Floor floor = new Floor(i + 1);
            registry.registerFloor(floor);

            for (int e = 0; e < config.getEntrancesPerFloor()[i]; e++) {
                Entrance entrance = new Entrance(allEntrances.size());
                floor.addEntrance(entrance);
                registry.registerEntrance(entrance);
                allEntrances.add(entrance);
            }
        }

        for (int i = 0; i < config.getTotalFloors(); i++) {
            Floor floor = registry.getFloor(i + 1);
            List<SpotSpec> specsOnFloor = config.getFloorSpecs().get(i);

            for (SpotSpec spec : specsOnFloor) {
                ParkingSpot spot = buildSpot(spec.category, spec.ratePerHour);
                floor.addSpot(spot);

                for (int eIdx = 0; eIdx < spec.distancesFromEntrances.length; eIdx++) {
                    int dist = spec.distancesFromEntrances[eIdx];
                    allEntrances.get(eIdx).addSpotDistance(spot, dist);
                }
            }
        }

        return new CarPark(registry, new FeeProcessor(new HourlyChargePolicy()));
    }

    private static ParkingSpot buildSpot(SpotCategory category, double ratePerHour) {
        return switch (category) {
            case COMPACT -> new CompactSpot(ratePerHour);
            case REGULAR -> new RegularSpot(ratePerHour);
            case OVERSIZED -> new OversizedSpot(ratePerHour);
        };
    }
}
