package com.carpark.model;

import com.carpark.model.enums.SpotCategory;
import com.carpark.spots.ParkingSpot;
import java.time.LocalDateTime;

public class ParkReceipt {
    private final String receiptId;
    private final ParkingSpot reservedSpot;
    private final Vehicle vehicle;
    private final SpotCategory allocatedSlotType;
    private final LocalDateTime checkInTime;

    public ParkReceipt(String receiptId, ParkingSpot reservedSpot, Vehicle vehicle, LocalDateTime checkInTime) {
        this.receiptId = receiptId;
        this.reservedSpot = reservedSpot;
        this.vehicle = vehicle;
        this.allocatedSlotType = reservedSpot.getCategory();
        this.checkInTime = checkInTime;
    }

    public String getReceiptId() { return receiptId; }
    public ParkingSpot getReservedSpot() { return reservedSpot; }
    public Vehicle getVehicle() { return vehicle; }
    public String getAllocatedSlotId() { return reservedSpot.getSpotId(); }
    public SpotCategory getAllocatedSlotType() { return allocatedSlotType; }
    public LocalDateTime getCheckInTime() { return checkInTime; }
}
