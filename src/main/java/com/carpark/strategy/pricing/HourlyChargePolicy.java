package com.carpark.strategy.pricing;

import com.carpark.model.ParkReceipt;
import java.time.Duration;
import java.time.LocalDateTime;

public class HourlyChargePolicy implements ChargePolicy {

    @Override
    public double calculateCost(ParkReceipt receipt) {
        LocalDateTime checkOutTime = LocalDateTime.now();
        Duration parkedDuration = Duration.between(receipt.getCheckInTime(), checkOutTime);

        long hours = (long) Math.ceil(parkedDuration.toMinutes() / 60.0);
        if (hours <= 0) hours = 1;

        return hours * receipt.getReservedSpot().getRatePerHour();
    }
}
