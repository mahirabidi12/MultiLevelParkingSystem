package com.carpark.strategy.pricing;

import com.carpark.model.ParkReceipt;
import java.time.LocalDateTime;

public interface ChargePolicy {
    double calculateCost(ParkReceipt receipt, LocalDateTime exitTime);
}
