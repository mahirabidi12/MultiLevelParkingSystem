package com.carpark.controller;

import com.carpark.model.Car;
import com.carpark.model.ParkReceipt;
import com.carpark.service.FeeProcessor;
import com.carpark.service.SpotRegistry;
import com.carpark.spots.ParkingSpot;

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

    public ParkReceipt checkIn(Car car, int entranceId) {
        ParkingSpot spot = spotRegistry.getClosestAvailableSpot(entranceId, car.getCategory());

        if (spot == null) {
            return null;
        }

        ParkReceipt receipt = new ParkReceipt(
            UUID.randomUUID().toString(),
            spot,
            car.getPlateNumber()
        );

        openReceipts.put(receipt.getReceiptId(), receipt);
        return receipt;
    }

    public double checkOut(String receiptId) {
        ParkReceipt receipt = openReceipts.remove(receiptId);
        if (receipt == null) {
            throw new IllegalArgumentException("Receipt ID not found or already closed: " + receiptId);
        }

        double fee = feeProcessor.computeFee(receipt);
        receipt.getReservedSpot().release();
        return fee;
    }
}
