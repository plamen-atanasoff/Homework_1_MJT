package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.astar.AStar;
import bg.sofia.uni.fmi.mjt.itinerary.astar.Node;
import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.StartCitySameAsDestinationCity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.SequencedCollection;
import java.util.Set;

public class RideRight implements ItineraryPlanner {
    private final List<Journey> schedule;
    private final Set<City> cities;

    public RideRight(List<Journey> schedule) {
        this.schedule = new ArrayList<>(schedule);
        this.cities = new HashSet<>();

        for (Journey journey : schedule) {
            cities.add(journey.from());
            cities.add(journey.to());
        }
    }

    @Override
    public SequencedCollection<Journey> findCheapestPath(City start, City destination, boolean allowTransfer)
        throws CityNotKnownException, NoPathToDestinationException {
        if (start.equals(destination)) {
            throw new StartCitySameAsDestinationCity("start equal to destination");
        }

        if (!cities.contains(start) || !cities.contains(destination)) {
            throw new CityNotKnownException("start or destination city is not present");
        }

        SequencedCollection<Journey> cheapestPath = new ArrayList<>();
        if (!allowTransfer) {
            cheapestPath.add(getCheapestDirectPath(start, destination));
        } else {
            Node node = AStar.getCheapestPath(start, destination, cities, schedule);

            if (node == null) {
                throw new NoPathToDestinationException("no such path exists");
            }

            while (node.parent != null) {
                cheapestPath.add(node.bestPathToParent);
                node = node.parent;
            }
        }

        return cheapestPath.reversed();
    }

    private Journey getCheapestDirectPath(City start, City destination) throws NoPathToDestinationException {
        Journey cheapestDirectPath = null;

        for (Journey journey : schedule) {
            if (journey.from().equals(start) && journey.to().equals(destination)) {
                if (cheapestDirectPath == null || journey.compareTo(cheapestDirectPath) < 0) {
                    cheapestDirectPath = journey;
                }
            }
        }

        if (cheapestDirectPath == null) {
            throw new NoPathToDestinationException("no such path exists");
        }

        return cheapestDirectPath;
    }
}
