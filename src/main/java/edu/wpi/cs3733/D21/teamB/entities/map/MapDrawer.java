package edu.wpi.cs3733.D21.teamB.entities.map;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import edu.wpi.cs3733.D21.teamB.pathfinding.*;
import edu.wpi.cs3733.D21.teamB.util.Popup.PoppableManager;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.Getter;
import lombok.Setter;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.util.*;

public class MapDrawer implements PoppableManager {

    private final PathfindingMenuController pathfindingMenuController;
    private final MapCache mapCache;
    @Setter
    private MapPathPopupManager mapPathPopupManager;
    @Setter
    private MapEditorPopupManager mapEditorPopupManager;
    private final AnchorPane nodeHolder;
    private final AnchorPane mapHolder;
    private final AnchorPane intermediateNodeHolder;
    private final Label lblError;
    private final GesturePane gPane;
    private final StackPane mapStack;
    private ETAPopup etaPopup;

    private final DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");

    @Getter
    @Setter
    private boolean isEditing = false;

    public MapDrawer(PathfindingMenuController pathfindingMenuController, MapCache mapCache, AnchorPane nodeHolder, AnchorPane mapHolder, AnchorPane intermediateNodeHolder,
                     Label lblError, StackPane mapStack, GesturePane gPane) {
        this.pathfindingMenuController = pathfindingMenuController;
        this.mapCache = mapCache;
        this.nodeHolder = nodeHolder;
        this.mapHolder = mapHolder;
        this.intermediateNodeHolder = intermediateNodeHolder;
        this.lblError = lblError;
        this.mapStack = mapStack;
        this.gPane = gPane;
    }

    /**
     * Draw a path on the map fiven a Path object
     * @param path The path to draw
     */
    public void drawPath(Path path){
        Map<String, Node> nodes = Graph.getGraph().getNodes();

        if (mapCache.getFinalPath().getPath().isEmpty()) {
            lblError.setVisible(true);
        } else {
            //Draw the segment of the path that is on the current floor
            for (int i = 0; i < mapCache.getFinalPath().getFloorPathSegment(mapCache.getCurrentFloor()).size() - 1; i++) {
                placeEdge(nodes.get(mapCache.getFinalPath().getFloorPathSegment(mapCache.getCurrentFloor()).get(i)),
                        nodes.get(mapCache.getFinalPath().getFloorPathSegment(mapCache.getCurrentFloor()).get(i + 1)));
            }
        }

        if (etaPopup != null) {
            etaPopup.hide();
            etaPopup = null;
        }

        etaPopup = mapPathPopupManager.createETAPopup(mapCache.getFinalPath());
    }

    /**
     * Draws the path on the map given a start and an end node
     * @param start Long name of the start node
     * @param end Long name of the end node
     */
    public void drawPath(String start, String end) {
        Graph.getGraph().updateGraph();
        Map<String, String> longToIDMap = mapCache.getMapLongToID();

        Stack<String> allStops = new Stack<>();

        //Get the list of stops
        List<String> stopsList = mapCache.getStopsList();

        //Create the stack of nodeIDs for the pathfinder
        allStops.push(longToIDMap.get(end));

        for (int i = stopsList.size() - 1; i >= 0; i--) {
            allStops.push(longToIDMap.get(stopsList.get(i)));
        }
        allStops.push(longToIDMap.get(start));

        //Create the required pathfinder
        Pathfinder pathfinder;
        switch (pathfindingMenuController.getComboPathingType().getSelectionModel().getSelectedItem()) {
            case "A*":
                pathfinder = new AStar();
                break;
            case "DFS":
                pathfinder = new DFS();
                break;
            case "BFS":
                pathfinder = new BFS();
                break;
            default:
                throw new IllegalStateException("Extra option in combo box?");
        }

        //Set the final path in mapCache
        mapCache.setFinalPath(pathfinder.findPath(allStops));

        drawPath(mapCache.getFinalPath());
    }

    /**
     * Draws all the nodes on a given floor with the default graphic
     */
    public void drawNodesOnFloor() {
        Map<String, List<Node>> nodes = mapCache.getFloorNodes();
        // If the floor has no nodes, return
        if (!nodes.containsKey(mapCache.getCurrentFloor())) return;

        for (Node n : nodes.get(mapCache.getCurrentFloor())) {
            if (!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL") || n.getBuilding().equals("BTM") || n.getBuilding().equals("Shapiro"))) {
                placeNode(n);
            }
        }
    }

    /**
     * Draws all the nodes on a given floor with the alternate graphic
     */
    public void drawAltNodesOnFloor() {

        Map<String, Node> nodes = db.getNodes();

        if (nodes.isEmpty()) return;

        for (Node n : nodes.values()) {
            if ((!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL"))) && (!n.getBuilding().equals("BTM") && !n.getBuilding().equals("Shapiro")) &&
                    n.getFloor().equals(mapCache.getCurrentFloor())) {
                placeAltNode(n);
            }
        }
    }

    /**
     * Draws all the intermediate nodes on a floor
     */
    private void drawIntermediateNodesOnFloor() {
        Map<String, Node> nodes = db.getNodes();

        if (nodes.isEmpty()) return;

        for (Node n : nodes.values()) {
            if ((n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL")) && (!n.getBuilding().equals("BTM") &&
                    !n.getBuilding().equals("Shapiro")) && n.getFloor().equals(mapCache.getCurrentFloor())) {
                placeIntermediateNode(n);
            }
        }
    }

    /**
     * Draws all edges on a floor
     */
    private void drawEdgesOnFloor() {
        Map<String, Edge> edges = Graph.getGraph().getEdges();
        for (Edge e : edges.values()) {
            Node start = db.getNodeById(e.getStartNodeID());
            Node end = db.getNodeById(e.getEndNodeID());

            if (start.getFloor().equals(mapCache.getCurrentFloor()) &&
                    end.getFloor().equals(mapCache.getCurrentFloor()) &&
                    (
                            !start.getBuilding().equals("BTM") &&
                                    !start.getBuilding().equals("Shapiro")) &&
                    (
                            !end.getBuilding().equals("BTM") &&
                                    !start.getBuilding().equals("Shapiro")
                    )) {
                placeEdge(start, end);
            }
        }
    }

    /**
     * Places an image for a node on the map at the given pixel coordinates.
     *
     * @param n Node object to place on the map
     */
    private void placeNode(Node n) {
        try {
            ImageView i = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/node.fxml")));

            i.setLayoutX((n.getXCoord() / PathfindingMenuController.coordinateScale) - (i.getFitWidth() / 4));
            i.setLayoutY((n.getYCoord() / PathfindingMenuController.coordinateScale) - (i.getFitHeight()));

            i.setId(n.getNodeID() + "Icon");

            // Show graphical input for pathfinding when clicked
            i.setOnMouseClicked((MouseEvent e) -> {
                removeAllPopups();
                mapPathPopupManager.createGraphicalInputPopup(n);
            });

            nodeHolder.getChildren().add(i);
            mapCache.getNodePlaced().add(i);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Place alternate node type on the map
     *
     * @param n the Node object to place
     */
    private void placeAltNode(Node n) {
        try {
            Circle c = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/nodeAlt.fxml")));

            c.setLayoutX((n.getXCoord() / PathfindingMenuController.coordinateScale));
            c.setLayoutY((n.getYCoord() / PathfindingMenuController.coordinateScale));

            c.setId(n.getNodeID() + "Icon");

            c.setOnMouseClicked((MouseEvent e) -> {
                if (mapCache.getStartNode() != null) {
                    mapCache.setNewEdgeEnd(n.getNodeID());
                    mapEditorPopupManager.showAddEdgePopup(e);
                } else mapEditorPopupManager.showEditNodePopup(n, e, false);
            });

            c.setOnMouseEntered(event -> {
                if (isEditing) c.setStroke(Color.GREEN);
            });
            c.setOnMouseExited(event -> {
                if (isEditing) c.setStroke(Color.BLACK);
            });

            nodeHolder.getChildren().add(c);
            mapCache.getNodePlaced().add(c);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Places an image for a node on the map at the given pixel coordinates.
     *
     * @param n Node object to place on the map
     */
    public void placeIntermediateNode(Node n) {
        try {
            Circle c = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/intermediateNode.fxml")));

            c.setCenterX((n.getXCoord() / PathfindingMenuController.coordinateScale));
            c.setCenterY((n.getYCoord() / PathfindingMenuController.coordinateScale));

            c.setOnMouseClicked(event -> {
                if (mapCache.getStartNode() != null) {
                    mapCache.setNewEdgeEnd(n.getNodeID());
                    mapEditorPopupManager.showAddEdgePopup(event);
                } else mapEditorPopupManager.showEditNodePopup(n, event, false);
            });

            c.setId(n.getNodeID() + "IntIcon");

            c.setOnMouseEntered(event -> {
                if (isEditing) c.setStroke(Color.GREEN);
            });
            c.setOnMouseExited(event -> {
                if (isEditing) c.setStroke(Color.BLACK);
            });

            intermediateNodeHolder.getChildren().add(c);
            mapCache.getIntermediateNodePlaced().add(c);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws an edge between 2 points on the map.
     *
     * @param start start node
     * @param end   end node
     */
    public void placeEdge(Node start, Node end) {
        try {
            Line l = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/edge.fxml")));

            l.setStartX(start.getXCoord() / PathfindingMenuController.coordinateScale);
            l.setStartY(start.getYCoord() / PathfindingMenuController.coordinateScale);

            l.setEndX(end.getXCoord() / PathfindingMenuController.coordinateScale);
            l.setEndY(end.getYCoord() / PathfindingMenuController.coordinateScale);

            l.setOnMouseClicked(e -> {
                if (isEditing) {
                    mapEditorPopupManager.showDelEdgePopup(start, end, mapStack);
                }
            });

            l.setOnMouseEntered(event -> {
                if (isEditing) l.setStroke(Color.RED);
            });
            l.setOnMouseExited(event -> {
                if (isEditing) l.setStroke(Color.rgb(0, 103, 177));
            });

            l.setId(start.getNodeID() + "_" + end.getNodeID() + "Icon");

            mapHolder.getChildren().add(l);
            mapCache.getEdgesPlaced().add(l);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refresh the nodes on the map.
     * <p>
     * FOR MAP EDITOR MODE ONLY!!!
     * <p>
     */
    public void refreshEditor() {
        mapCache.updateLocations();
        removeAllEdges();
        removeIntermediateNodes();
        removeNodes();
        drawEdgesOnFloor();
        drawAltNodesOnFloor();
        drawIntermediateNodesOnFloor();
    }

    /**
     * Draws all the elements of the map base on direction or map edit mode.
     */
    public void drawAllElements() {
        removeAllPopups();

        if (isEditing) {
            removeAllEdges();
            removeNodes();
            removeIntermediateNodes();
            drawEdgesOnFloor();
            drawAltNodesOnFloor();
            drawIntermediateNodesOnFloor();
        } else {
            mapCache.updateLocations();
            removeAllEdges();
            removeIntermediateNodes();
            removeNodes();
            drawNodesOnFloor();
        }
    }

    /**
     * Remove all the popups on the map
     */
    public void removeAllPopups() {
        mapEditorPopupManager.removeAllPopups();
        mapPathPopupManager.removeAllPopups();

        gPane.setGestureEnabled(true);
    }

    /**
     * Removes any edges drawn on the map
     */
    public void removeAllEdges() {
        mapPathPopupManager.removeETAPopup();
        lblError.setVisible(false);
        for (Line l : mapCache.getEdgesPlaced())
            mapHolder.getChildren().remove(l);

        mapCache.setEdgesPlaced(new ArrayList<>());
    }

    /**
     * Removes all nodes from the map
     */
    private void removeNodes() {
        for (javafx.scene.Node n : mapCache.getNodePlaced())
            nodeHolder.getChildren().remove(n);

        mapCache.setNodePlaced(new ArrayList<>());
    }

    /**
     * Removes all intermediate nodes from the map
     */
    private void removeIntermediateNodes() {
        for (javafx.scene.Node n : mapCache.getIntermediateNodePlaced())
            intermediateNodeHolder.getChildren().remove(n);

        mapCache.setIntermediateNodePlaced(new ArrayList<>());
    }
}
