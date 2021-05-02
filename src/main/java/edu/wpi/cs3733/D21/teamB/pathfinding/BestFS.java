package edu.wpi.cs3733.D21.teamB.pathfinding;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import java.util.*;

public class BestFS extends AlgoTemplate implements Pathfinder{

    /**

    public Path findPath(String start, String end, boolean mobility) {

        Graph graph = Graph.getGraph();
        graph.updateGraph();
        Queue<Pair> queue = new LinkedList<>();
        queue.add(new Pair(start, new ArrayList<>(Collections.singleton(graph.getNodes().get(start)))));

        while (!queue.isEmpty()) {
            //taking into account heuristic
            //informed BFS
            Pair p = orderQueue(queue, end);
            queue.remove(p);
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
     */
    public double calculateFVal(double heur, double edgeCost){
        return heur;
    }


//    /**
//     *
//     * @param queue the neighbors queue
//     * @param end the ending node
//     * @return the neighbor that is closest to the end node
//     */
//    public static Pair orderQueue(Queue<Pair> queue, String end){
//        Graph graph = Graph.getGraph();
//        Node endNode = graph.getNodes().get(end);
//        Pair smallestPair = null;
//        double minHeur = Integer.MAX_VALUE;
//
//        for(Pair pair : queue){
//            double heur = Graph.dist(graph.getNodes().get(pair.getCurrent()), endNode);
//
//            if(heur < minHeur){
//                smallestPair = pair;
//                minHeur = heur;
//            }
//        }
//
//        return smallestPair;
//    }
}
