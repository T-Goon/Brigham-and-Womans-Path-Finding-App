package edu.wpi.cs3733.D21.teamB.entities.map;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;
import javafx.scene.control.TreeItem;
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
    private List<Line> edgesPlaced = new ArrayList<>();
    @Getter
    @Setter
    private List<javafx.scene.Node> nodePlaced = new ArrayList<>();
    @Getter
    @Setter
    private List<javafx.scene.Node> intermediateNodePlaced = new ArrayList<>();

    @Getter
    private final Map<String, List<Node>> floorNodes = new HashMap<>();
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
    private List<String> stopsList = new ArrayList<>();

    @Getter
    @Setter
    private Path finalPath;

    /**
     * Function that updates everything involved with the different locations on the map
     * - Tree View
     * - Floor Nodes
     */
    public void updateLocations() {
        mapLongToID = makeLongToIDMap();

        floorNodes.remove(currentFloor);
        for (Node n : Graph.getGraph().getNodes().values()) {
            if (!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL")|| n.getBuilding().equals("BTM") || n.getBuilding().equals("Shapiro"))) {
                //Populate Category map for TreeView

                //This if statement is temporary for iteration 1 where pathfinding is only needed for the first floor
                if(n.getFloor().equals(currentFloor)) {
                    if (!catNameMap.containsKey(n.getNodeType())) {
                        ArrayList<TreeItem<String>> tempList = new ArrayList<>();
                        TreeItem<String> tempItem = new TreeItem<>(n.getLongName());
                        tempList.add(tempItem);
                        catNameMap.put(n.getNodeType(), tempList);
                    } else {
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
