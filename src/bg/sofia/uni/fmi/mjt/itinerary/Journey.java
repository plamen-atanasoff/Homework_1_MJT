package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType;

import java.math.BigDecimal;

public record Journey(VehicleType vehicleType, City from, City to, BigDecimal price) implements Comparable<Journey> {
    public Journey {
        if (vehicleType == null) {
            throw new IllegalArgumentException("vehicleType is null");
        }

        if (from == null || to == null) {
            throw new IllegalArgumentException("from or to is null");
        }
    }

    @Override
    public int compareTo(Journey other) {
        return this.price.compareTo(other.price);
    }
}
