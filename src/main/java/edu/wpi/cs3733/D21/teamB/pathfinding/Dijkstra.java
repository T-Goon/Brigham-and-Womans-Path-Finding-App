package edu.wpi.cs3733.D21.teamB.pathfinding;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import java.util.*;

public class Dijkstra extends AlgoTemplate implements Pathfinder{

    public Path findPath(String startID, String endID, boolean mobility, String category, String comparisonType) {

        Graph graph = Graph.getGraph();
        graph.updateGraph();
        Node startNode = graph.getNodes().get(startID);
        Node endNode = graph.getNodes().get(endID);

        //Initialize data structures used in the A* algorithm
        LinkedList<String> ret = new LinkedList<>();
        Queue<Node> pQueue = new PriorityQueue<>();
        Map<String, String> cameFrom = new HashMap<>();
        Map<String, Double> costSoFar = new HashMap<>();

        pQueue.add(startNode);
        cameFrom.put(startID, "START");
        costSoFar.put(startID, 0.0);

        Node current = null;
        while (!pQueue.isEmpty()) {

            //Takes next node in the priority queue which should be the node with the greatest fVal
            current = pQueue.poll();

            //If the node has reached the end node break out of the loop
            if (categoryCompare(comparisonType, current, endNode, category))
                break;

            //Try-catch will catch a NullPointerException caused by a node with no edges
            try {
                //Check the adj nodes of the current node
                for (Node neighbor : graph.getAdjNodesById(current.getNodeID())) {

                    // Deals with mobility
                    if (mobility && neighbor.getNodeType().equals("STAI")) continue;

                    //Calculate the cost of reaching the next node
                    double newCost = costSoFar.get(current.getNodeID()) + Graph.dist(current, neighbor);

                    //If the cost is not in the hash map, or if this cost would be cheaper
                    if (!costSoFar.containsKey(neighbor.getNodeID()) || newCost < costSoFar.get(neighbor.getNodeID())) {

                        //Add the new cost to the hashmap
                        costSoFar.put(neighbor.getNodeID(), newCost);

                        //Set the new fVal of the node
                        neighbor.setFVal(newCost);

                        //Add the node to the priority queue
                        pQueue.add(neighbor);

                        //Add the node to the cameFrom hashmap to indicate it came from this node.
                        cameFrom.put(neighbor.getNodeID(), current.getNodeID());
                    }
                }
            } catch (NullPointerException e) {
                return new Path(new LinkedList<>(), 0);
            }
        }

        // Cannot find path
        assert current != null;
        if(pQueue.isEmpty()) {
            if (!current.equals(endNode)) return new Path(new LinkedList<>(), 0);
        }


        //backtrack from end node to start node to create final path.
        String currentID = current.getNodeID();
        while (!currentID.equals("START")) {
            ret.addFirst(currentID);
            currentID = cameFrom.get(currentID);
        }

        return new Path(ret, graph.calculateCost(ret));
    }

    public double calculateFVal(double newCost, double heur){
        return newCost;
    }

    /**
     *
     * @param category
     * @return
     */
    public boolean categoryCompare(String compString, Node start, Node end, String category) {
        if(compString.equals("ID")){
            return start.getNodeID().equals(end.getNodeID());
        }
        else if(compString.equals("Cat")){
            return start.getNodeType().equals(category);
        }
        else{
            throw new IllegalArgumentException("Not an ID or category");
        }
    }

}
