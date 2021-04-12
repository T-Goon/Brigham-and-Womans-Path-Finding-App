package edu.wpi.teamB.pathfinding;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Node;

import java.util.*;

public class AStar {

    /**
     * Main pathfinding algorithm used to find a path between two nodes.
     *
     * @param startID nodeID of the starting node
     * @param endID   nodeID of the ending node
     * @return LinkedList of nodeIDs which dictates the order of nodes in the path
     */
    public static List<String> findPath(String startID, String endID) {

        Graph graph = Graph.getGraph(DatabaseHandler.getDatabaseHandler("main.db"));
        Node startNode = graph.getNodes().get(startID);
        Node endNode = graph.getNodes().get(endID);

        //Initialize data structures used in the A* algorithm
        LinkedList<String> ret = new LinkedList<>();
        Queue<Node> pQueue = new PriorityQueue<>();
        Map<String, String> cameFrom = new HashMap<>();
        Map<String, Double> costSoFar = new HashMap<>();

        //A* logic as described here
        //https://www.redblobgames.com/pathfinding/a-star/introduction.html:w
        pQueue.add(startNode);
        cameFrom.put(startID, "START");
        costSoFar.put(startID, 0.0);

        Node current = null;
        while (!pQueue.isEmpty()) {
            //Takes next node in the priority queue which should be the node with the greatest fVal
            current = pQueue.poll();

            //If the node has reached the end node break out of the loop
            if (current.equals(endNode))
                break;

            //Check the adj nodes of the current node
            for (Node neighbor : graph.getAdjNodesById(current.getNodeID())) {

                //Calculate the cost of reaching the next node
                double newCost = costSoFar.get(current.getNodeID()) + Graph.dist(current, neighbor);

                //If the cost is not in the hash map, or if this cost would be cheaper
                if (!costSoFar.containsKey(neighbor.getNodeID()) || newCost < costSoFar.get(neighbor.getNodeID())) {

                    //Add the new cost to the hashmap
                    costSoFar.put(neighbor.getNodeID(), newCost);

                    //Set the new fVal of the node
                    neighbor.setFVal(newCost + Graph.dist(neighbor, endNode));

                    //Add the node to the priority queue
                    pQueue.add(neighbor);

                    //Add the node to the cameFrom hashmap to indicate it came from this node.
                    cameFrom.put(neighbor.getNodeID(), current.getNodeID());
                }
            }
        }

        //backtrack from end node to start node to create final path.
        assert current != null;
        String currentID = current.getNodeID();
        while (!currentID.equals("START")) {
            ret.addFirst(currentID);
            currentID = cameFrom.get(currentID);
        }

        return ret;

    }


}
