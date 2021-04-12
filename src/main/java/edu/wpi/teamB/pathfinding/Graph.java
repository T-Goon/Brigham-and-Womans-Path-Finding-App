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

    private Graph() {
        DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");

        try {
            nodes = db.getNodes();
            edges = db.getEdges();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
     * findAdjNodes: Finds adjacent nodes of the given node
     *
     * @param ID: The NodeID that we are getting the adjacent nodes of
     * @return The list of adjacent nodes of the given node id
     */
    public static List<Node> findAdjNodes(String ID) {
        List<Node> adjNodeList = new ArrayList<>();

        // Get edges and nodes from database
        Map<String, Edge> edgeInfo;
        Map<String, Node> nodeInfo;
        try {
            DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");
            edgeInfo = db.getEdges();
            nodeInfo = db.getNodes();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        // Fills the adjacent node list bidirectionally
        for (Edge edges : edgeInfo.values()) {
            if (edges.getStartNodeName().equals(ID)) {
                for (Node node : nodeInfo.values()) {
                    if (edges.getEndNodeName().equals(node.getNodeID())) {
                        adjNodeList.add(node);
                        break;
                    }
                }
            } else if (edges.getEndNodeName().equals(ID)) {
                for (Node node : nodeInfo.values()) {
                    if (edges.getStartNodeName().equals(node.getNodeID())) {
                        adjNodeList.add(node);
                        break;
                    }
                }
            }
        }
        return adjNodeList;
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