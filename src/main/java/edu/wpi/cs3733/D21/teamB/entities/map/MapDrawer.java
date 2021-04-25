package edu.wpi.cs3733.D21.teamB.entities.map;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;

import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import edu.wpi.cs3733.D21.teamB.pathfinding.AStar;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapDrawer implements PoppableManager {

    private final MapCache mc;
    @Setter
    private MapPathPopupManager mppm;
    @Setter
    private MapEditorPopupManager mepm;
    private final AnchorPane nodeHolder;
    private final AnchorPane mapHolder;
    private final AnchorPane intermediateNodeHolder;
    private final Label lblError;
    private final GesturePane gpane;
    private final StackPane mapStack;
    private ETAPopup etaPopup;

    private final DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");

    @Getter
    @Setter
    private boolean isEditing = false;

    public MapDrawer(MapCache mc, AnchorPane nodeHolder, AnchorPane mapHolder, AnchorPane intermediateNodeHolder,
                     Label lblError, StackPane mapStack, GesturePane gpane) {
        this.mc = mc;
        this.nodeHolder = nodeHolder;
        this.mapHolder = mapHolder;
        this.intermediateNodeHolder = intermediateNodeHolder;
        this.lblError = lblError;
        this.mapStack = mapStack;
        this.gpane = gpane;
    }

    /**
     * Draws the path on the map
     */
    public void drawPath(String start, String end) {
        List<String> sl = mc.getStopsList();
        ArrayList<String> allStops = new ArrayList<>();
        allStops.add(start);
        allStops.addAll(sl);
        allStops.add(end);

        Path wholePath = new Path();
        wholePath.setPath(new ArrayList<>());

        for (int i = 0; i < allStops.size()-1; i++) {
            Map<String, Node> nodesId = Graph.getGraph().getNodes();
            Map<String, String> hmLongName = mc.makeLongToIDMap();
            Path aStarPath = AStar.findPath(hmLongName.get(allStops.get(i)), hmLongName.get(allStops.get(i+1)));

            List<String> AstarPath = aStarPath.getPath();
            wholePath.getPath().addAll(AstarPath);

            if (AstarPath.isEmpty()) {
                lblError.setVisible(true);
            } else {
                Node prev = null;
                for (String loc : AstarPath) {
                    if ((prev != null) && (loc != null)) {
                        Node curr = nodesId.get(loc);
                        placeEdge(prev, curr);
                    }
                    prev = nodesId.get(loc);
                }
            }
        }

        if (etaPopup != null) {
            etaPopup.hide();
            etaPopup = null;
        }

        etaPopup = mppm.createETAPopup(wholePath);
    }

    /**
     * Draws all the nodes on a given floor with the default graphic
     */
    public void drawNodesOnFloor() {
        Map<String, List<Node>> nodes = mc.getFloorNodes();
        // If the floor has no nodes, return
        if (!nodes.containsKey(mc.getCurrentFloor())) return;

        for (Node n : nodes.get(mc.getCurrentFloor())) {
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
                    n.getFloor().equals(mc.getCurrentFloor())) {
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
                    !n.getBuilding().equals("Shapiro")) && n.getFloor().equals(mc.getCurrentFloor())) {
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

            if (start.getFloor().equals(mc.getCurrentFloor()) &&
                    end.getFloor().equals(mc.getCurrentFloor()) &&
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
                mppm.createGraphicalInputPopup(n);
            });

            nodeHolder.getChildren().add(i);
            mc.getNodePlaced().add(i);

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
                if (mc.getStartNode() != null) {
                    mc.setNewEdgeEnd(n.getNodeID());
                    mepm.showAddEdgePopup(e);
                } else mepm.showEditNodePopup(n, e, false);
            });

            c.setOnMouseEntered(event -> {
                if (isEditing) c.setStroke(Color.GREEN);
            });
            c.setOnMouseExited(event -> {
                if (isEditing) c.setStroke(Color.BLACK);
            });

            nodeHolder.getChildren().add(c);
            mc.getNodePlaced().add(c);

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
                if (mc.getStartNode() != null) {
                    mc.setNewEdgeEnd(n.getNodeID());
                    mepm.showAddEdgePopup(event);
                } else mepm.showEditNodePopup(n, event, false);
            });

            c.setId(n.getNodeID() + "IntIcon");

            c.setOnMouseEntered(event -> {
                if (isEditing) c.setStroke(Color.GREEN);
            });
            c.setOnMouseExited(event -> {
                if (isEditing) c.setStroke(Color.BLACK);
            });

            intermediateNodeHolder.getChildren().add(c);
            mc.getIntermediateNodePlaced().add(c);

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
                    mepm.showDelEdgePopup(start, end, mapStack);
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
            mc.getEdgesPlaced().add(l);

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
        mc.updateLocations();
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
            mc.updateLocations();
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
        mepm.removeAllPopups();
        mppm.removeAllPopups();

        gpane.setGestureEnabled(true);
    }

    /**
     * Removes any edges drawn on the map
     */
    public void removeAllEdges() {
        mppm.removeETAPopup();
        lblError.setVisible(false);
        for (Line l : mc.getEdgesPlaced())
            mapHolder.getChildren().remove(l);

        mc.setEdgesPlaced(new ArrayList<>());
    }

    /**
     * Removes all nodes from the map
     */
    private void removeNodes() {
        for (javafx.scene.Node n : mc.getNodePlaced())
            nodeHolder.getChildren().remove(n);

        mc.setNodePlaced(new ArrayList<>());
    }

    /**
     * Removes all intermediate nodes from the map
     */
    private void removeIntermediateNodes() {
        for (javafx.scene.Node n : mc.getIntermediateNodePlaced())
            intermediateNodeHolder.getChildren().remove(n);

        mc.setIntermediateNodePlaced(new ArrayList<>());
    }
}
