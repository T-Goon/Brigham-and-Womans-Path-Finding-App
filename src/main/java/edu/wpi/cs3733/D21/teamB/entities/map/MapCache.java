package edu.wpi.cs3733.D21.teamB.entities.map;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapCache {

    @Getter
    @Setter
    private List<Line> edgesPlaced = new ArrayList<>(); // All of the lines currently placed on the map

    @Getter
    @Setter
    private Map<String, List<Line>> instructionsToEdges = new HashMap<>(); // Instructions to lists of lines involved with that instruction

    @Getter
    @Setter
    private List<javafx.scene.Node> nodePlaced = new ArrayList<>(); // All of the nodes currently placed on the map

    @Getter
    private final List<Node> editedNodes = new ArrayList<>(); // All nodes that have had color changes due to pathfinding
    @Getter
    private final Map<String, Color> editedNodesColor = new HashMap<>(); // NodeID -> old color of nodes in edited nodes list

    @Getter
    private final List<VBox> floorIndicators = new ArrayList<>(); // The text next to the nodes that indicate up and down
    // floor movements

    @Getter
    @Setter
    private List<javafx.scene.Node> intermediateNodePlaced = new ArrayList<>(); // All of the intermediate nodes placed
    // on the map

    @Getter
    private final Map<String, List<Node>> floorNodes = new HashMap<>(); // map of floorid to list of nodes on that floor

    @Getter
    @Setter
    private String currentFloor = FloorSwitcher.floor1ID;

    @Getter
    private Map<String, String> mapLongToID = new HashMap<>();
    @Getter
    private final Map<String, List<TreeItem<String>>> catNameMap = new HashMap<>();

    @Setter
    @Getter
    private String newEdgeStart;
    @Setter
    @Getter
    private Circle startNode;
    @Setter
    @Getter
    private String newEdgeEnd;

    @Getter
    @Setter
    private List<String> stopsList = new ArrayList<>(); // List of node stops in the pathfinding

    @Getter
    @Setter
    private Path finalPath;

    @Getter
    @Setter
    private double avgX, avgY, scaleAmount;

    @Getter
    private final Map<String, String> categoryNameMap = new HashMap<>(); // Map of category short name -> category long name

    public MapCache() {
        categoryNameMap.put("SERV", "Services");
        categoryNameMap.put("REST", "Restrooms");
        categoryNameMap.put("LABS", "Lab Rooms");
        categoryNameMap.put("ELEV", "Elevators");
        categoryNameMap.put("DEPT", "Departments");
        categoryNameMap.put("CONF", "Conference Rooms");
        categoryNameMap.put("INFO", "Information Locations");
        categoryNameMap.put("RETL", "Retail Locations");
        categoryNameMap.put("EXIT", "Entrances");
        categoryNameMap.put("STAI", "Stairs");
        categoryNameMap.put("PARK", "Parking Spots");
        categoryNameMap.put("HALL", "Hallway");
        categoryNameMap.put("WALK", "Sidewalk");
    }

    /**
     * Function that updates everything involved with the different locations on the map
     * - Tree View
     * - Floor Nodes
     */
    public void updateLocations() {
        mapLongToID = makeLongToIDMap();

        floorNodes.remove(currentFloor);
        for (Node n : Graph.getGraph().getNodes().values()) {
            if (!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL"))) {
                //Populate Category map for TreeView

                //This if statement is temporary for iteration 1 where pathfinding is only needed for the first floor
                if (!catNameMap.containsKey(n.getNodeType())) {
                    ArrayList<TreeItem<String>> tempList = new ArrayList<>();
                    TreeItem<String> tempItem = new TreeItem<>(n.getLongName());
                    tempList.add(tempItem);
                    catNameMap.put(n.getNodeType(), tempList);
                } else {
                    if (catNameMap.get(n.getNodeType()).stream().noneMatch(item -> item.getValue().equals(n.getLongName()))) {

                        catNameMap.get(n.getNodeType()).add(new TreeItem<>(n.getLongName()));
                    }
                }

            }

            //Populate the floorNodes Map to know which nodes belong to each floor
            if (floorNodes.containsKey(n.getFloor())) {
                floorNodes.get(n.getFloor()).add(n);
            } else {
                ArrayList<Node> tempList = new ArrayList<>();
                tempList.add(n);
                floorNodes.put(n.getFloor(), tempList);
            }
        }
    }

    /**
     * @return a map of long names to node IDs
     */
    public Map<String, String> makeLongToIDMap() {
        Map<String, Node> nodesId = Graph.getGraph().getNodes();
        Map<String, String> longName = new HashMap<>();

        for (Node node : nodesId.values()) {
            longName.put(node.getLongName(), node.getNodeID());
        }

        mapLongToID = longName;
        return mapLongToID;
    }
}
