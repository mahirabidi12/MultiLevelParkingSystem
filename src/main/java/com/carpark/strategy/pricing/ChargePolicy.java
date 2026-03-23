package com.carpark.strategy.pricing;

import com.carpark.model.ParkReceipt;

public interface ChargePolicy {
    double calculateCost(ParkReceipt receipt);
}
