package bg.sofia.uni.fmi.mjt.itinerary.exception;

public class StartCitySameAsDestinationCity extends RuntimeException {
    public StartCitySameAsDestinationCity(String message) {
        super(message);
    }
}
