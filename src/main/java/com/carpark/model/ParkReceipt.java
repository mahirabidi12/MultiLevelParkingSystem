package com.carpark.model;

import com.carpark.spots.ParkingSpot;
import java.time.LocalDateTime;

public class ParkReceipt {
    private final String receiptId;
    private final ParkingSpot reservedSpot;
    private final String plateNumber;
    private final LocalDateTime checkInTime;

    public ParkReceipt(String receiptId, ParkingSpot reservedSpot, String plateNumber) {
        this.receiptId = receiptId;
        this.reservedSpot = reservedSpot;
        this.plateNumber = plateNumber;
        this.checkInTime = LocalDateTime.now();
    }

    public String getReceiptId() { return receiptId; }
    public ParkingSpot getReservedSpot() { return reservedSpot; }
    public String getPlateNumber() { return plateNumber; }
    public LocalDateTime getCheckInTime() { return checkInTime; }
}
