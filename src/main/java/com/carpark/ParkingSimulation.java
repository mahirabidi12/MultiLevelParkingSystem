package com.carpark;

import com.carpark.assembler.CarParkAssembler;
import com.carpark.controller.CarPark;
import com.carpark.model.Car;
import com.carpark.model.ParkReceipt;
import com.carpark.model.dto.CarParkConfig;
import com.carpark.model.dto.SpotSpec;
import com.carpark.model.enums.SpotCategory;

import java.util.ArrayList;
import java.util.List;
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

        ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        CountDownLatch latch = new CountDownLatch(TOTAL_VEHICLES);
        Random random = new Random();

        int totalEntrances = TOTAL_FLOORS * ENTRANCES_PER_FLOOR;
        SpotCategory[] categories = SpotCategory.values();

        for (int i = 0; i < TOTAL_VEHICLES; i++) {
            final int vehicleIndex = i;
            pool.submit(() -> {
                try {
                    String plate = "CAR-" + String.format("%03d", vehicleIndex);
                    SpotCategory category = categories[random.nextInt(categories.length)];
                    Car car = new Car(plate, category);

                    int entranceId = random.nextInt(totalEntrances);
                    ParkReceipt receipt = carPark.checkIn(car, entranceId);

                    if (receipt == null) {
                        System.out.println(plate + " could not park — lot full for category " + category);
                        return;
                    }

                    System.out.printf("%s checked in at entrance %d | rate: $%.2f/hr%n",
                            plate, entranceId, receipt.getReservedSpot().getRatePerHour());

                    int parkDurationMs = 1000 + random.nextInt(2000);
                    Thread.sleep(parkDurationMs);

                    double fee = carPark.checkOut(receipt.getReceiptId());
                    System.out.printf("%s checked out | fee: $%.2f%n", plate, fee);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        pool.shutdown();
        System.out.println("Simulation complete.");
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
