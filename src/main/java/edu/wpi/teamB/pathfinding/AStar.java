package edu.wpi.teamB.pathfinding;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Node;

import java.sql.SQLException;
import java.util.*;

public class AStar {


    /**
     * Main pathfinding algorithm used to find a path between two nodes.
     * @param startID nodeID of the starting node
     * @param endID nodeID of the ending node
     * @return LinkedList of nodeIDs which dictates the order of nodes in the path
     */
    public static LinkedList<String> findPath(String startID, String endID){

        //Database connection to get start and end nodes by the ids passed in as parameters
        DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");
        Node startNode = null;
        Node endNode = null;

        try{
            startNode = db.getNodeById(startID);
            endNode = db.getNodeById(endID);
        }catch (SQLException e){
            e.printStackTrace();
        }


        //Initialize data structures used in the A* algorithm
        LinkedList<String> ret = new LinkedList<>();
        PriorityQueue<Node> pQueue = new PriorityQueue<>();
        HashMap<String, String> cameFrom = new HashMap<>();
        HashMap<String, Double> costSoFar = new HashMap<>();



        //A* logic as described here
        //https://www.redblobgames.com/pathfinding/a-star/introduction.html:w
        pQueue.add(startNode);
        cameFrom.put(startID, "START");
        costSoFar.put(startID, 0.0);


        Node current = null;
        while(!pQueue.isEmpty()){
            //Takes next node in the priority queue which should be the node with the greatest fVal
            current = pQueue.poll();

            //If the node has reached the end node break out of the loop
            if (current.equals(endNode))
                break;

            //Check the adj nodes of the current node
            for (Node neighbor: Graph.findAdjNodes(current.getNodeID())){

                //Calculate the cost of reaching the next node
                double newCost = costSoFar.get(current.getNodeID()) + Graph.dist(current, neighbor);

                //If the cost is not in the hash map, or if this cost would be cheaper
                if(!costSoFar.containsKey(neighbor.getNodeID()) || newCost<costSoFar.get(neighbor.getNodeID())){

                    //Add the new cost to the hashmap
                   costSoFar.put(neighbor.getNodeID(), newCost);

                   //Set the new fVal of the node
                   neighbor.setFVal(newCost+Graph.dist(neighbor, endNode));

                   //Add the node to the priority queue
                   pQueue.add(neighbor);

                   //Add the node to the cameFrom hashmap to indicate it came from this node.
                   cameFrom.put(neighbor.getNodeID(), current.getNodeID());
                }
            }
        }


        //backtrack from end node to start node to create final path.
        String currentID = current.getNodeID();
        while(!currentID.equals("START")){
            ret.addFirst(currentID);
            currentID = cameFrom.get(currentID);
        }


        return ret;

    }




}
