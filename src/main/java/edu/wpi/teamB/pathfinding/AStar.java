package edu.wpi.teamB.pathfinding;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Node;

import java.sql.SQLException;
import java.util.*;

public class AStar {

    public static LinkedList<String> findPath(String startID, String endID){
        DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");
        Node startNode = null;
        Node endNode = null;
        Node current = null;

        try{
            startNode = db.getNodeById(startID);
            endNode = db.getNodeById(endID);
        }catch (SQLException e){
            e.printStackTrace();
        }

        LinkedList<String> ret = new LinkedList<>();
        PriorityQueue<Node> pQueue = new PriorityQueue<>();
        HashMap<String, String> cameFrom = new HashMap<>();
        HashMap<String, Double> costSoFar = new HashMap<>();

        pQueue.add(startNode);
        cameFrom.put(startID, "START");
        costSoFar.put(startID, 0.0);


        while(!pQueue.isEmpty()){
            current = pQueue.poll();
            if (current.equals(endNode))
                break;

            for (Node neighbor: Graph.findAdjNodes(current.getNodeID())){
                double newCost = costSoFar.get(current.getNodeID()) + Graph.dist(current, neighbor);
                if(!costSoFar.containsKey(neighbor.getNodeID()) || newCost<costSoFar.get(neighbor.getNodeID())){
                   costSoFar.put(neighbor.getNodeID(), newCost);
                   neighbor.setFVal(newCost+Graph.dist(neighbor, endNode));
                   pQueue.add(neighbor);
                   cameFrom.put(neighbor.getNodeID(), current.getNodeID());
                }
            }
        }

        String currentID = current.getNodeID();
        while(!currentID.equals("START")){
            ret.addFirst(currentID);
            currentID = cameFrom.get(currentID);
        }


        return ret;

    }




}
