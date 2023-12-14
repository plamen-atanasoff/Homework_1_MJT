package bg.sofia.uni.fmi.mjt.itinerary;

public record City(String name, Location location) {
    public City {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is null or blank");
        }

        if (location == null) {
            throw new IllegalArgumentException("location is null");
        }
    }
}
