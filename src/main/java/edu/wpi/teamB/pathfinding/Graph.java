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

    private DatabaseHandler db;

    private Graph() {
        this.db = DatabaseHandler.getDatabaseHandler("main.db");
        updateGraph();
    }

    private Graph(DatabaseHandler db) {
        this.db = db;
        updateGraph();
    }

    public void updateGraph() {
        adjMap = new HashMap<>();
        nodes = db.getNodes();
        edges = db.getEdges();

        for (Edge edge : edges.values()) {
            if (!adjMap.containsKey(edge.getStartNodeName())) {
                LinkedList<Node> tempList = new LinkedList<>();
                tempList.add(nodes.get(edge.getEndNodeName()));
                adjMap.put(edge.getStartNodeName(), tempList);
            } else {
                adjMap.get(edge.getStartNodeName()).add(nodes.get(edge.getEndNodeName()));
            }

            if (!adjMap.containsKey(edge.getEndNodeName())) {
                LinkedList<Node> tempList = new LinkedList<>();
                tempList.add(nodes.get(edge.getStartNodeName()));
                adjMap.put(edge.getEndNodeName(), tempList);
            } else {
                adjMap.get(edge.getEndNodeName()).add(nodes.get(edge.getStartNodeName()));
            }

        }
    }

    public List<Node> getAdjNodesById(String nodeID) {
        return adjMap.get(nodeID);
    }

    /**
     * Changes the database that the graph uses.
     * Should only be used in testing
     *
     * @param db database handler to switch the graph to
     */
    public static void setGraph(DatabaseHandler db) {
        graph = new Graph(db);
    }

    /**
     * Get the graph with whatever database handler is set
     *
     * @return graph singleton
     */
    public static Graph getGraph() {
        if (graph == null) graph = new Graph();
        return graph;
    }


    /**
     * Given two nodes, return the distance between them
     * based on their coordinates.
     *
     * @param start starting node
     * @param end   ending node
     * @return the distance between the two nodes
     */
    public static double dist(Node start, Node end) {
        double dist = Math.sqrt(Math.pow((start.getXCoord() - end.getXCoord()), 2) + Math.pow((start.getYCoord() + end.getYCoord()), 2));
        dist += Math.abs(end.getFloorAsInt() - start.getFloorAsInt()) * 5000;
        return dist;
    }
}