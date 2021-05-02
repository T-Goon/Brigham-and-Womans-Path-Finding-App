package edu.wpi.cs3733.D21.teamB.pathfinding;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;

import java.util.*;

public class BFS implements Pathfinder {

    /**
     * Implementation of breadth-first search given the starting nodeID.
     *
     * @param start the starting NodeID
     * @param end   the ending NodeID
     * @return a stack of the path of nodeIDs from start to end
     */
    public Path findPath(String start, String end, boolean mobility) {
        Graph graph = Graph.getGraph();
        graph.updateGraph();
        Queue<Pair> queue = new LinkedList<>();
        queue.add(new Pair(start, new ArrayList<>(Collections.singleton(graph.getNodes().get(start)))));

        while (!queue.isEmpty()) {
            Pair p = queue.remove();
            List<Node> neighbors = graph.getAdjNodesById(p.getCurrent());
            neighbors.removeAll(p.getPath());

            for (Node n : neighbors) {

                // Deals with mobility
                if (mobility && n.getNodeType().equals("STAI")) continue;

                if (n.getNodeID().equals(end)) {
                    p.getPath().add(n);
                    List<String> path = new ArrayList<>();
                    p.getPath().forEach(node -> path.add(node.getNodeID()));
                    return new Path(path, graph.calculateCost(path));
                } else {
                    ArrayList<Node> nodes = new ArrayList<>(p.getPath());
                    nodes.add(n);
                    queue.add(new Pair(n.getNodeID(), nodes));
                }
            }
        }

        return null;
    }

    /**
     * @param startID      starting node
     * @param destinations we take in nodes of the same category that the user
     *                     wants to go to
     * @return The shortest path from the starting node to any of the nodes in the destinations list
     * to it
     */
    public Path shortestPathToNodeInList(String startID, List<String> destinations) {

        Graph graph = Graph.getGraph();
        graph.updateGraph();

        double min = Double.MAX_VALUE;
        Path shortestPath = new Path();

        for (String dest : destinations) {

            Path tempPath = findPath(startID, dest, false);
            double cost = tempPath.getTotalPathCost();
            if (cost != 0 && cost < min) {
                shortestPath = tempPath;
                min = cost;
            }
        }
        return shortestPath;
    }

}
