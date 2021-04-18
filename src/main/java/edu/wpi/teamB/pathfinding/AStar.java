package edu.wpi.teamB.pathfinding;

import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.entities.Path;

import java.util.*;

public class AStar {

    /**
     * Main pathfinding algorithm used to find a path between two nodes.
     *
     * @param startID nodeID of the starting node
     * @param endID   nodeID of the ending node
     * @return LinkedList of nodeIDs which dictates the order of nodes in the path
     */
    public static Path findPath(String startID, String endID) {

        Graph graph = Graph.getGraph();
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

            //Try-catch will catch a NullPointerException caused by a node with no edges
            try {
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
            } catch (NullPointerException e) {
                return new Path(new LinkedList<>(), 0);
            }
        }


        //backtrack from end node to start node to create final path.
        assert current != null;
        String currentID = current.getNodeID();
        while (!currentID.equals("START")) {
            ret.addFirst(currentID);
            currentID = cameFrom.get(currentID);
        }

        return new Path(ret, costSoFar.get(current.getNodeID()));
    }

    /**
     *
     * @param startID starting node
     * @param destinations we take in nodes of the same category that the user
     *              wants to go to
     * @return the closest path from currLoc to which ever node is closest
     * to it
     */
    public static List<String> closestPath(String startID, List<Node> destinations) {

        Graph graph = Graph.getGraph();
        double min = Integer.MAX_VALUE;

        List<String> shortestPath = new LinkedList<>();

        for(Node dest: destinations){
            Path p = findPath(startID, dest.getNodeID());
            double cost = p.getTotalPathCost();
            if(cost!=0 && cost<min){
                shortestPath = p.getPath();
                min = cost;
            }
        }
        return shortestPath;
    }

    public static double eta(String start, String end){
        Path path = findPath(start, end);
        //3-4mph average human walking speed
        //1 mph = 88 fpm

        //bWALK00601,1738,1545,1,Parking,WALK,Francis Vining Intersection Top Left,FrancisViningIntTopLeft
        //bWALK01201,3373,1554,1,Parking,WALK,Francis Top Sidewalk 3,FrancisSidewalk3
        //According to google maps, path from one corner of Francis street to other is ~500 ft:
        //using this to get pixles / minute
        //double pixDist = Math.sqrt((3373 - 1738)^2 +(1554-1545)^2);
        double pixDist = 1635.025;
//        double realDist = 500;
//        double feetPerMin = 88;

        double timeConst = (pixDist / 2);

        return path.getTotalPathCost()*timeConst;

    }

}
