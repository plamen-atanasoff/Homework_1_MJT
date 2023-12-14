package bg.sofia.uni.fmi.mjt.itinerary.astar;

import bg.sofia.uni.fmi.mjt.itinerary.City;
import bg.sofia.uni.fmi.mjt.itinerary.Journey;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class AStar {
    private static final BigDecimal HEURISTIC = BigDecimal.valueOf(20D / 1000D);

    private static Node initNodes(Set<City> cities, List<Journey> journeys, City start, City destination) {
        Set<Node> nodes = new HashSet<>(cities.size());
        Node startNode = null;

        for (City city : cities) {
            int manhattanDistance =
                    Math.abs(city.location().x() - destination.location().x()) +
                    Math.abs(city.location().y() - destination.location().y());
            Node node = new Node(city.name(), BigDecimal.valueOf(manhattanDistance).multiply(HEURISTIC));

            nodes.add(node);

            if (city.equals(start)) {
                startNode = node;
                startNode.price = BigDecimal.valueOf(0);
            }
        }

        for (Node node : nodes) {
            addBranches(node, journeys, nodes);
        }

        return startNode;
    }

    public static Node getCheapestPath(City from, City to, Set<City> cities, List<Journey> journeys) {
        Node start = initNodes(cities, journeys, from, to);

        PriorityQueue<Node> closedList = new PriorityQueue<>();
        PriorityQueue<Node> openList = new PriorityQueue<>();

        start.totalPrice = start.price.multiply(start.heuristic);
        openList.add(start);

        while (!openList.isEmpty()) {
            Node n = openList.peek();
            if (n.getCity().equals(to.name())) {
                return n;
            }

            checkNeighbours(n, openList, closedList);

            openList.remove(n);
            closedList.add(n);
        }

        return null;
    }

    private static void checkNeighbours(Node n, Queue<Node> openList, Queue<Node> closedList) {
        for (Node.Edge edge : n.neighbours) {
            Node m = edge.node();
            BigDecimal curWeight =
                n.price.add(edge.journey().price().multiply(
                    edge.journey().vehicleType().getGreenTax().add(BigDecimal.ONE))
                );

            if (!openList.contains(m) && !closedList.contains(m)) {
                setAttributes(m, curWeight, n, edge.journey());

                openList.add(m);
            } else {
                if (curWeight.compareTo(m.price) < 0) {
                    setAttributes(m, curWeight, n, edge.journey());

                    if (closedList.contains(m)) {
                        closedList.remove(m);
                        openList.add(m);
                    }
                }
            }
        }
    }

    private static void addBranches(Node node, List<Journey> journeys, Set<Node> nodes) {
        for (Journey journey : journeys) {
            if (journey.from().name().equals(node.getCity())) {
                Node branchNode = getNode(journey.to().name(), nodes);
                assert branchNode != null;

                node.addBranch(journey, branchNode);
            }
        }
    }

    private static Node getNode(String city, Set<Node> nodes) {
        for (Node node : nodes) {
            if (city.equals(node.getCity())) {
                return node;
            }
        }

        return null;
    }

    private static void setAttributes(Node node, BigDecimal curWeight, Node parent, Journey bestPathToParent) {
        node.price = curWeight;
        node.parent = parent;
        node.bestPathToParent = bestPathToParent;
        node.totalPrice = node.price.add(node.heuristic);
    }
}
