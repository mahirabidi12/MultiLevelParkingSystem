package com.carpark.service;

import com.carpark.model.ParkReceipt;
import com.carpark.strategy.pricing.ChargePolicy;
import java.time.LocalDateTime;

public class FeeProcessor {
    private final ChargePolicy chargePolicy;

    public FeeProcessor(ChargePolicy chargePolicy) {
        this.chargePolicy = chargePolicy;
    }

    public double computeFee(ParkReceipt receipt, LocalDateTime exitTime) {
        return chargePolicy.calculateCost(receipt, exitTime);
    }
}
