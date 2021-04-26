package edu.wpi.cs3733.D21.teamB.pathfinding;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class BFS implements Pathfinder {

    /**
     * Implementation of depth-first search given the starting nodeID.
     *
     * @param start the starting NodeID
     * @param end   the ending NodeID
     * @return a stack of the path of nodeIDs from start to end
     */
    public Path findPath(String start, String end) {
        Graph graph = Graph.getGraph();
        graph.updateGraph();
        Queue<Pair> queue = new LinkedList<>();
        queue.add(new Pair(start, new ArrayList<>(Collections.singleton(graph.getNodes().get(start)))));

        while (!queue.isEmpty()) {
            Pair p = queue.remove();
            List<Node> neighbors = graph.getAdjNodesById(p.current);
            neighbors.removeAll(p.path);

            for (Node n : neighbors) {
                if (n.getNodeID().equals(end)) {
                    p.path.add(n);
                    List<String> path = new ArrayList<>();
                    p.path.forEach(node -> path.add(node.getNodeID()));
                    System.out.println(path);
                    return new Path(path, graph.calculateCost(path));
                } else {
                    ArrayList<Node> nodes = new ArrayList<>(p.path);
                    nodes.add(n);
                    queue.add(new Pair(n.getNodeID(), nodes));
                }
            }
        }

        return null;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Pair {
        private String current;
        private ArrayList<Node> path;
    }
}
