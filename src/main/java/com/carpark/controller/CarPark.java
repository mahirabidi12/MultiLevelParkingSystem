package com.carpark.controller;

import com.carpark.model.ParkReceipt;
import com.carpark.model.Vehicle;
import com.carpark.model.enums.SpotCategory;
import com.carpark.model.enums.VehicleType;
import com.carpark.service.FeeProcessor;
import com.carpark.service.SpotRegistry;
import com.carpark.spots.ParkingSpot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CarPark {
    private final SpotRegistry spotRegistry;
    private final FeeProcessor feeProcessor;
    private final Map<String, ParkReceipt> openReceipts = new ConcurrentHashMap<>();

    public CarPark(SpotRegistry spotRegistry, FeeProcessor feeProcessor) {
        this.spotRegistry = spotRegistry;
        this.feeProcessor = feeProcessor;
    }

    /**
     * Parks a vehicle and returns the generated parking receipt.
     * If the requested slot type is full, the system upgrades to the next larger slot.
     */
    public ParkReceipt park(Vehicle vehicle, LocalDateTime entryTime, SpotCategory requestedSlotType, int entranceId) {
        for (SpotCategory candidate : fallbackOrder(requestedSlotType)) {
            ParkingSpot spot = spotRegistry.getClosestAvailableSpot(entranceId, candidate);
            if (spot != null) {
                ParkReceipt receipt = new ParkReceipt(
                    UUID.randomUUID().toString(),
                    spot,
                    vehicle,
                    entryTime
                );
                openReceipts.put(receipt.getReceiptId(), receipt);
                return receipt;
            }
        }
        return null; // no compatible slot available
    }

    /**
     * Returns the current count of available slots per slot type.
     */
    public Map<SpotCategory, Long> status() {
        return spotRegistry.availabilityByCategory();
    }

    /**
     * Processes vehicle exit and returns the total bill amount.
     * Billing is based on the allocated slot type's hourly rate.
     */
    public double exit(ParkReceipt receipt, LocalDateTime exitTime) {
        ParkReceipt stored = openReceipts.remove(receipt.getReceiptId());
        if (stored == null) {
            throw new IllegalArgumentException("Receipt not found or already closed: " + receipt.getReceiptId());
        }
        double fee = feeProcessor.computeFee(stored, exitTime);
        stored.getReservedSpot().release();
        return fee;
    }

    /**
     * Returns fallback slot categories in ascending size order starting from requestedSlotType.
     * TWO_WHEELER: SMALL -> MEDIUM -> LARGE
     * CAR:         MEDIUM -> LARGE
     * BUS:         LARGE
     */
    private List<SpotCategory> fallbackOrder(SpotCategory requestedSlotType) {
        return switch (requestedSlotType) {
            case SMALL  -> List.of(SpotCategory.SMALL, SpotCategory.MEDIUM, SpotCategory.LARGE);
            case MEDIUM -> List.of(SpotCategory.MEDIUM, SpotCategory.LARGE);
            case LARGE  -> List.of(SpotCategory.LARGE);
        };
    }

    /**
     * Derives the minimum slot type a vehicle is compatible with.
     */
    public static SpotCategory minSlotFor(VehicleType vehicleType) {
        return switch (vehicleType) {
            case TWO_WHEELER -> SpotCategory.SMALL;
            case CAR         -> SpotCategory.MEDIUM;
            case BUS         -> SpotCategory.LARGE;
        };
    }
}
