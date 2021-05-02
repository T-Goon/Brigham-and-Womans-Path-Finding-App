package edu.wpi.cs3733.D21.teamB.pathfinding;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Graph {

    private static Graph graph;

    private Map<String, Node> nodes;
    private Map<String, Edge> edges;
    private Map<String, List<Node>> adjMap;

    private final DatabaseHandler db;

    @Getter
    @Setter
    private int pathingTypeIndex;

    private Graph() {
        this.db = DatabaseHandler.getHandler();
        pathingTypeIndex = 0;
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

        if (edges == null || nodes == null) return;

        for (Edge edge : edges.values()) {
            if (!adjMap.containsKey(edge.getStartNodeID())) {
                LinkedList<Node> tempList = new LinkedList<>();
                tempList.add(nodes.get(edge.getEndNodeID()));
                adjMap.put(edge.getStartNodeID(), tempList);
            } else {
                adjMap.get(edge.getStartNodeID()).add(nodes.get(edge.getEndNodeID()));
            }

            if (!adjMap.containsKey(edge.getEndNodeID())) {
                LinkedList<Node> tempList = new LinkedList<>();
                tempList.add(nodes.get(edge.getStartNodeID()));
                adjMap.put(edge.getEndNodeID(), tempList);
            } else {
                adjMap.get(edge.getEndNodeID()).add(nodes.get(edge.getStartNodeID()));
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
        graph.updateGraph();
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
        double dist = Math.sqrt(Math.pow((start.getXCoord() - end.getXCoord()), 2) + Math.pow((start.getYCoord() - end.getYCoord()), 2));
        dist += Math.abs(end.getFloorAsInt() - start.getFloorAsInt()) * 5000;
        return dist;
    }

    /**
     * Calculates the cost of a path.
     *
     * @param path the list of nodeIDs
     * @return the cost of the path
     */
    public double calculateCost(List<String> path) {
        if (path == null || path.isEmpty()) return 0;
        double cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Node start = db.getNodeById(path.get(i));
            Node end = db.getNodeById(path.get(i + 1));

            if (start.getFloor().equals(end.getFloor()))
                cost += dist(start, end);
            else cost += 408.75625 * Math.abs(end.getFloorAsInt() - start.getFloorAsInt());
        }
        return cost;
    }

    /**
     * Verify's if the two nodes you are trying to connect are actually connected
     * @param startID NodeID of the first node
     * @param endID NodeID of the second node
     * @return boolean if the nodes are connected by an edge
     */
    public boolean verifyEdge(String startID, String endID){
        Map<String, Edge> edges = db.getEdges();
        Node n = db.getNodeById(startID);
        Node n2 = db.getNodeById(endID);
        return edges.containsKey(n.getNodeID() + "_" + n2.getNodeID()) || edges.containsKey(n2.getNodeID() + "_" + n.getNodeID());
    }

    /**
     * Calculates the estimated time it would take to walk a certain path
     *
     * @param path the given Path
     * @return the estimated time string to put in the box
     */
    public static String getEstimatedTime(Path path) {
        //bWALK00601,1738,1545,
        //bWALK01201,3373,1554
        //According to google maps ~500 ft:
        //double pixDist = 1635.025;

        double timeConst = (2 / 1635.025);
        double timeDec = path.getTotalPathCost() * timeConst;
        double secondsTime = timeDec * 60;

        int min = (int) Math.floor(timeDec);
        int sec = (int) secondsTime - min * 60;

        if (min == 0) return String.format("%02d", sec) + " sec";

        return min + ":" + String.format("%02d", sec) + " min";

    }
}