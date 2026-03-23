package com.carpark;

import com.carpark.assembler.CarParkAssembler;
import com.carpark.controller.CarPark;
import com.carpark.model.ParkReceipt;
import com.carpark.model.Vehicle;
import com.carpark.model.dto.CarParkConfig;
import com.carpark.model.dto.SpotSpec;
import com.carpark.model.enums.SpotCategory;
import com.carpark.model.enums.VehicleType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParkingSimulation {

    private static final int TOTAL_FLOORS = 4;
    private static final int ENTRANCES_PER_FLOOR = 2;
    private static final int SPOTS_PER_FLOOR = 25;
    private static final int TOTAL_VEHICLES = 120;
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) throws InterruptedException {
        CarPark carPark = buildCarPark();

        // Print initial slot availability
        System.out.println("=== Initial Availability ===");
        printStatus(carPark.status());

        ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CountDownLatch latch = new CountDownLatch(TOTAL_VEHICLES);
        Random random = new Random();

        int totalEntrances = TOTAL_FLOORS * ENTRANCES_PER_FLOOR;
        VehicleType[] vehicleTypes = VehicleType.values();

        for (int i = 0; i < TOTAL_VEHICLES; i++) {
            final int vehicleIndex = i;
            pool.submit(() -> {
                try {
                    VehicleType vehicleType = vehicleTypes[random.nextInt(vehicleTypes.length)];
                    String plate = vehicleType.name().charAt(0) + "-" + String.format("%03d", vehicleIndex);
                    Vehicle vehicle = new Vehicle(plate, vehicleType);

                    SpotCategory requestedSlot = CarPark.minSlotFor(vehicleType);
                    int entranceId = random.nextInt(totalEntrances);
                    LocalDateTime entryTime = LocalDateTime.now();

                    ParkReceipt receipt = carPark.park(vehicle, entryTime, requestedSlot, entranceId);

                    if (receipt == null) {
                        System.out.printf("%s (%s) could not park — no compatible slot available%n", plate, vehicleType);
                        return;
                    }

                    System.out.printf("%s (%s) parked in %s slot %s | rate: $%.2f/hr%n",
                            plate, vehicleType,
                            receipt.getAllocatedSlotType(),
                            receipt.getAllocatedSlotId().substring(0, 8),
                            receipt.getReservedSpot().getRatePerHour());

                    int parkDurationMs = 1000 + random.nextInt(2000);
                    Thread.sleep(parkDurationMs);

                    LocalDateTime exitTime = LocalDateTime.now();
                    double fee = carPark.exit(receipt, exitTime);
                    System.out.printf("%s exited | billed on %s slot | fee: $%.2f%n",
                            plate, receipt.getAllocatedSlotType(), fee);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        pool.shutdown();

        // Print final slot availability
        System.out.println("\n=== Final Availability ===");
        printStatus(carPark.status());
        System.out.println("Simulation complete.");
    }

    private static void printStatus(Map<SpotCategory, Long> statusMap) {
        for (SpotCategory cat : SpotCategory.values()) {
            System.out.printf("  %-8s: %d available%n", cat, statusMap.getOrDefault(cat, 0L));
        }
    }

    private static CarPark buildCarPark() {
        Random random = new Random();
        SpotCategory[] categories = SpotCategory.values();
        int totalEntrances = TOTAL_FLOORS * ENTRANCES_PER_FLOOR;

        CarParkConfig config = new CarParkConfig()
                .setTotalFloors(TOTAL_FLOORS)
                .setEntrancesPerFloor(new int[]{
                        ENTRANCES_PER_FLOOR, ENTRANCES_PER_FLOOR,
                        ENTRANCES_PER_FLOOR, ENTRANCES_PER_FLOOR
                });

        for (int f = 0; f < TOTAL_FLOORS; f++) {
            List<SpotSpec> specs = new ArrayList<>();
            for (int s = 0; s < SPOTS_PER_FLOOR; s++) {
                SpotCategory cat = categories[random.nextInt(categories.length)];
                double rate = 10 + random.nextInt(16);
                int[] distances = new int[totalEntrances];
                for (int e = 0; e < totalEntrances; e++) {
                    distances[e] = 1 + random.nextInt(100);
                }
                specs.add(new SpotSpec(cat, rate, distances));
            }
            config.addFloorSpecs(specs);
        }

        return CarParkAssembler.assemble(config);
    }
}
