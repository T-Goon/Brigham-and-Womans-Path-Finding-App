package edu.wpi.teamB.pathfinding;

import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import lombok.Getter;

import java.sql.SQLException;
import java.util.*;

@Getter
public class Graph {

    private static Graph graph;

    private Map<String, Node> nodes;
    private Map<String, Edge> edges;
    private Map<String, List<Node>> adjMap;

    private Graph() {
        adjMap = new HashMap<>();
        DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");

        try {
            nodes = db.getNodes();
            edges = db.getEdges();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //Populates the adjMap
        for(Edge edge: edges.values()){

            //Adds edges from start to end node
            if(!adjMap.containsKey(edge.getStartNodeName())){
                LinkedList<Node> tempList = new LinkedList<>();
                tempList.add(nodes.get(edge.getEndNodeName()));
                adjMap.put(edge.getStartNodeName(), tempList);
            }else{
                adjMap.get(edge.getStartNodeName()).add(nodes.get(edge.getEndNodeName()));
            }

            //Adds edges from end to start node
            if(!adjMap.containsKey(edge.getEndNodeName())){
                LinkedList<Node> tempList = new LinkedList<>();
                tempList.add(nodes.get(edge.getStartNodeName()));
                adjMap.put(edge.getEndNodeName(), tempList);
            }else{
                adjMap.get(edge.getEndNodeName()).add(nodes.get(edge.getStartNodeName()));
            }

        }

    }

    /**
     * Gets a list of the nodes adjacent to the given node ID
     * @param nodeID ID of the node to get the adjacent nodes of
     * @return LinkedList of adjacent nodes
     */
    public List<Node> getAdjNodesById(String nodeID){
        return adjMap.get(nodeID);
    }

    /**
     * @return the graph singleton
     */
    public static Graph getGraph() {
        if (graph == null) {
            graph = new Graph();
        }
        return graph;
    }

    /**
     * Given two nodes, return the distance between them
     * based on their coordinates.
     *
     * @param start starting node
     * @param end ending node
     * @return the distance between the two nodes
     */
    public static double dist(Node start, Node end) {
        double dist = Math.pow((start.getXCoord() - end.getXCoord()), 2) + Math.pow((start.getYCoord() + end.getYCoord()), 2);
        return Math.sqrt(dist);
    }
}