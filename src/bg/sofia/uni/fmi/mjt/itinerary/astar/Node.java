package bg.sofia.uni.fmi.mjt.itinerary.astar;

import bg.sofia.uni.fmi.mjt.itinerary.Journey;

import java.math.BigDecimal;
import java.util.SortedSet;
import java.util.TreeSet;

public class Node implements Comparable<Node> {
    private final String city;
    public Node parent = null;
    public Journey bestPathToParent = null;
    SortedSet<Edge> neighbours;
    BigDecimal totalPrice = BigDecimal.valueOf(-1);
    BigDecimal price = BigDecimal.valueOf(-1);
    BigDecimal heuristic;

    Node(String city, BigDecimal heuristic) {
        this.city = city;
        this.heuristic = heuristic;
        this.neighbours = new TreeSet<>();
    }

    @Override
    public int compareTo(Node other) {
        return totalPrice.compareTo(other.totalPrice);
    }

    public record Edge(Journey journey, Node node) implements Comparable<Edge> {

        @Override
        public int compareTo(Edge other) {
            return node.city.compareTo(other.node.city);
        }
    }

    public void addBranch(Journey journey, Node node) {
        Edge newEdge = new Edge(journey, node);
        neighbours.add(newEdge);
    }

    public String getCity() {
        return city;
    }
}
