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

    //testing
    public static List<Node> findAdjNodes(String ID) {
        List<Node> adjNodeList = new ArrayList<>();
        List<Edge> edgeInfo = Read.parseCSVEdges("src/main/resources/edu/wpi/teamB/csvfiles/MapBedges.csv");
        HashMap<String, Node> nodeInfo = Read.parseCSVNodes("src/main/resources/edu/wpi/teamB/csvfiles/MapBnodes.csv");

        for (Edge edges : edgeInfo) {
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

    //test dist
    public static double dist(Node start, Node end) {
        double dist = Math.pow((start.getXCoord() - end.getXCoord()), 2) + Math.pow((start.getYCoord() + end.getYCoord()), 2);
        return Math.sqrt(dist);
    }
}