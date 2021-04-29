package edu.wpi.cs3733.D21.teamB.entities.map;

import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Edge;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Path;
import edu.wpi.cs3733.D21.teamB.pathfinding.*;
import edu.wpi.cs3733.D21.teamB.util.Popup.PoppableManager;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.sql.SQLException;
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
    @Getter
    private final Circle head = new Circle(5);
    private final DatabaseHandler db = DatabaseHandler.getHandler();
    private final List<LineDir> lines = new ArrayList<>();
    private boolean notFirst = true;

    @Getter
    @Setter
    private boolean isEditing = false;

    @Getter
    @Setter
    private boolean mobility = false;

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
     * Draw's the path stored in the mapCache
     */
    public void drawPath() {
        Map<String, Node> nodes = Graph.getGraph().getNodes();

        // Head color for the animation
        if (!nodeHolder.getChildren().contains(head)) {
            nodeHolder.getChildren().add(head);
        }
        head.setFill(Color.valueOf("#0067B1"));

        if (mapCache.getFinalPath().getPath().isEmpty()) {
            // There is no path
            lblError.setVisible(true);
            nodeHolder.getChildren().remove(head);
        } else {
            // There is a path
            List<String> currentFloorPath = mapCache.getFinalPath().getFloorPathSegment(mapCache.getCurrentFloor());

            colorStartEndNode();

            //Draw the segment of the path that is on the current floor
            for (int i = 0; i < currentFloorPath.size() - 1; i++) {

                //Test if the edge you are trying to place is valid
                if (Graph.getGraph().verifyEdge(currentFloorPath.get(i), currentFloorPath.get(i + 1))) {
                    placeEdge(nodes.get(currentFloorPath.get(i)),
                            nodes.get(currentFloorPath.get(i + 1)));
                }
            }

            //If there is a path segment on this floor
            if (!currentFloorPath.isEmpty()) {

                if (currentFloorPath.size() == 1) {
                    nodeHolder.getChildren().remove(head);
                }

                // Color nodes that indicate a floor swap in the path
                colorNodesOnPathFloorSwitch(currentFloorPath);

                // Animate the path
                animatePath(currentFloorPath);

            } else {
                nodeHolder.getChildren().remove(head);
            }

            if (etaPopup != null) {
                etaPopup.hide();
                etaPopup = null;
            }

            //Creates the eta popup for this floor only
            etaPopup = mapPathPopupManager.createETAPopup(new Path(currentFloorPath, Graph.getGraph().calculateCost(currentFloorPath)));
            redrawNodes();
        }

    }

    /**
     * Color the nodes on the path that indicate the user needs to go up or down a floor
     * @param currentFloorPath List of node ids for the current path on the floor
     */
    private void colorNodesOnPathFloorSwitch(List<String> currentFloorPath){
        List<String> floorChangeNodes = new ArrayList<>();

        //Populate list with all of the nodes on the floor where the user must change floors
        for (int i = 0; i < currentFloorPath.size() - 1; i++) {
            if (!Graph.getGraph().verifyEdge(currentFloorPath.get(i), currentFloorPath.get(i + 1))) {
                floorChangeNodes.add(currentFloorPath.get(i));
            }
        }
        floorChangeNodes.add(currentFloorPath.get(currentFloorPath.size() - 1));

        //Should update all the floor change nodes' color
        for (String node : floorChangeNodes) {
            //Get the last index of node on this floor
            int floorChangeNodeIndex = mapCache.getFinalPath().getPath().indexOf(node);

            //If there is another node after this floor
            if (floorChangeNodeIndex != mapCache.getFinalPath().getPath().size() - 1) {
                //Get the nodeID of the node that is on the next floor
                String nextNode = mapCache.getFinalPath().getPath().get(floorChangeNodeIndex + 1);

                //Get the floor that the next node is on
                String floorString = nextNode.substring(nextNode.length() - 2);


                //Convert the floor strings to int values
                int currentFloorVal = Node.floorAsInt(mapCache.getCurrentFloor());
                int nextFloorVal = Node.floorAsInt(floorString);

                //If going up or down
                if (nextFloorVal > currentFloorVal) {
                    //Going UP

                    //Update the node icon to green to indicate that the user must go up
                    String floorChangeNodeID = mapCache.getFinalPath().getPath().get(floorChangeNodeIndex);
                    for (Node n : mapCache.getFloorNodes().get(mapCache.getCurrentFloor())) {
                        if (n.getNodeID().equals(floorChangeNodeID.substring(0, 10))) {
                            n.setColor(Color.web("00ff00"));

                            mapCache.getEditedNodes().add(n);
                        }
                    }
                } else {
                    //Going Down

                    //Update the node icon to red to indicate that the user must go down
                    String floorChangeNodeID = mapCache.getFinalPath().getPath().get(floorChangeNodeIndex);

                    for (Node n : mapCache.getFloorNodes().get(mapCache.getCurrentFloor())) {
                        if (n.getNodeID().equals(floorChangeNodeID.substring(0, 10))) {
                            n.setColor(Color.web("ff0000"));

                            mapCache.getEditedNodes().add(n);
                        }
                    }
                }
            }

        }
    }

    /**
     * Color the start and end nodes of the path
     */
    private void colorStartEndNode() {
        Path path = mapCache.getFinalPath();

        for (String floor : mapCache.getFloorNodes().keySet()) {
            for (Node n : mapCache.getFloorNodes().get(floor)) {
                if(path.getPath().get(0).equals(n.getNodeID())){
                    mapCache.getEditedNodes().add(n);

                    n.setColor(Color.web("ffff00"));
                } else if(path.getPath().get(path.getPath().size()-1).equals(n.getNodeID())){
                    mapCache.getEditedNodes().add(n);

                    n.setColor(Color.web("ff00ff"));
                }
            }
        }
    }

    /**
     * Animates the head on the path
     *
     * @param currentFloorPath List of node ids for nodes on the floor
     */
    private void animatePath(List<String> currentFloorPath) {
        javafx.scene.shape.Path animationPath = new javafx.scene.shape.Path();
        int steps = 0;

        // Place head at first node
        animationPath.getElements().add(new MoveTo(
                Graph.getGraph().getNodes().get(currentFloorPath.get(0)).getXCoord() / PathfindingMenuController.COORDINATE_SCALE,
                Graph.getGraph().getNodes().get(currentFloorPath.get(0)).getYCoord() / PathfindingMenuController.COORDINATE_SCALE));

        for (int i = 0; i < currentFloorPath.size() - 1; i++) {
            steps++;
            double x = Graph.getGraph().getNodes().get(currentFloorPath.get(i + 1)).getXCoord() / PathfindingMenuController.COORDINATE_SCALE;
            double y = Graph.getGraph().getNodes().get(currentFloorPath.get(i + 1)).getYCoord() / PathfindingMenuController.COORDINATE_SCALE;

            if (Graph.getGraph().verifyEdge(currentFloorPath.get(i), currentFloorPath.get(i + 1))) {
                // Valid edge move along it
                animationPath.getElements().add(new LineTo(x, y));

            } else if (i != currentFloorPath.size() - 2) {
                // Teleport to the next section of the path
                animationPath.getElements().add(new MoveTo(x, y));
            }

        }

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(steps * 300));
        pathTransition.setNode(head);
        pathTransition.setPath(animationPath);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Animation.INDEFINITE);
        pathTransition.setAutoReverse(false);
        pathTransition.play();

    }


    /**
     * Draws the path on the map given a start and an end node
     *
     * @param start Long name of the start node
     * @param end   Long name of the end node
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
        mapCache.setFinalPath(pathfinder.findPath(allStops, mobility));

        drawPath();
    }

    /**
     * Draws all the nodes on a given floor with the default graphic
     */
    public void drawNodesOnFloor() {
        Map<String, List<Node>> nodes = mapCache.getFloorNodes();
        // If the floor has no nodes, return
        if (!nodes.containsKey(mapCache.getCurrentFloor())) return;

        for (Node n : nodes.get(mapCache.getCurrentFloor())) {
            if (!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL"))) {
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
            if ((!(n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL"))) &&
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
            if ((n.getNodeType().equals("WALK") || n.getNodeType().equals("HALL")) && n.getFloor().equals(mapCache.getCurrentFloor())) {
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
                    end.getFloor().equals(mapCache.getCurrentFloor())) {
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

            Image image = i.getImage();
            PixelReader reader = image.getPixelReader();
            int w = (int) image.getWidth();
            int h = (int) image.getHeight();
            WritableImage wImage = new WritableImage(w, h);
            PixelWriter writer = wImage.getPixelWriter();
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    Color color = reader.getColor(x, y);
                    if (!(color.hashCode() == 0x00000000)) {
                        writer.setColor(x, y, n.getColor());
                    }
                }
            }

            i.setImage(wImage);

            i.setLayoutX((n.getXCoord() / PathfindingMenuController.COORDINATE_SCALE) - (i.getFitWidth() / 4));
            i.setLayoutY((n.getYCoord() / PathfindingMenuController.COORDINATE_SCALE) - (i.getFitHeight()));

            i.setId(n.getNodeID() + "Icon");

            // Show graphical input for pathfinding when clicked
            i.setOnMouseClicked((MouseEvent e) -> {
                if (e.isStillSincePress()) {
                    removeAllPopups();
                    mapPathPopupManager.createGraphicalInputPopup(n);
                }
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

            c.setCenterX((n.getXCoord() / PathfindingMenuController.COORDINATE_SCALE));
            c.setCenterY((n.getYCoord() / PathfindingMenuController.COORDINATE_SCALE));

            c.setId(n.getNodeID() + "Icon");

            c.setOnMouseClicked((MouseEvent e) -> {
                if (e.isStillSincePress()) { // If it's a still click, open up the popup
                    if (mapCache.getStartNode() != null) {
                        mapCache.setNewEdgeEnd(n.getNodeID());
                        mapEditorPopupManager.showAddEdgePopup(e);
                    } else mapEditorPopupManager.showEditNodePopup(n, e, false);
                }
            });

            setUpDrag(c, n);

            c.setOnMouseEntered(event -> {
                if (isEditing && !(mapCache.getStartNode() != null && c.getId().equals(mapCache.getStartNode().getId())))
                    c.setStroke(Color.GREEN);
            });
            c.setOnMouseExited(event -> {
                if (isEditing && !(mapCache.getStartNode() != null && c.getId().equals(mapCache.getStartNode().getId())))
                    c.setStroke(Color.BLACK);
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

            c.setCenterX((n.getXCoord() / PathfindingMenuController.COORDINATE_SCALE));
            c.setCenterY((n.getYCoord() / PathfindingMenuController.COORDINATE_SCALE));

            c.setId(n.getNodeID() + "IntIcon");

            c.setOnMouseEntered(event -> {
                if (isEditing && !(mapCache.getStartNode() != null && c.getId().equals(mapCache.getStartNode().getId())))
                    c.setStroke(Color.GREEN);
            });
            c.setOnMouseExited(event -> {
                if (isEditing && !(mapCache.getStartNode() != null && c.getId().equals(mapCache.getStartNode().getId())))
                    c.setStroke(Color.BLACK);
            });

            c.setOnMouseClicked((MouseEvent e) -> {
                if (e.isStillSincePress()) {
                    if (mapCache.getStartNode() != null) { // If it's a still click, open up the popup
                        mapCache.setNewEdgeEnd(n.getNodeID());
                        mapEditorPopupManager.showAddEdgePopup(e);
                    } else mapEditorPopupManager.showEditNodePopup(n, e, false);
                }
            });

            setUpDrag(c, n);

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

            l.setStartX(start.getXCoord() / PathfindingMenuController.COORDINATE_SCALE);
            l.setStartY(start.getYCoord() / PathfindingMenuController.COORDINATE_SCALE);

            l.setEndX(end.getXCoord() / PathfindingMenuController.COORDINATE_SCALE);
            l.setEndY(end.getYCoord() / PathfindingMenuController.COORDINATE_SCALE);

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
            redrawHighlightedNode();
            nodeHolder.getChildren().remove(head);

        } else {
            mapCache.updateLocations();
            removeAllEdges();
            removeIntermediateNodes();
            removeNodes();
            drawNodesOnFloor();
        }
    }

    private void redrawHighlightedNode() {
        if (mapCache.getStartNode() != null) {
            List<javafx.scene.Node> nodes = new ArrayList<>();
            nodes.addAll(mapCache.getNodePlaced());
            nodes.addAll(mapCache.getIntermediateNodePlaced());
            for (javafx.scene.Node n : nodes) {
                if (n.getId().equals(mapCache.getStartNode().getId())) {
                    Circle c = (Circle) n;
                    c.setStroke(Color.RED);
                }
            }
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

        if (!mapCache.getEditedNodes().isEmpty()) {
            for (Node node : mapCache.getEditedNodes()) {
                node.setColor(Color.web("012D5A"));
            }
            mapCache.getEditedNodes().clear();
            redrawNodes();
        }

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

    /**
     * Redraws all nodes
     */
    public void redrawNodes() {
        removeNodes();
        drawNodesOnFloor();
    }

    /**
     * Sets up dragging for each node
     *
     * @param c the circle being dragged
     * @param n the node related to the circle
     */
    private void setUpDrag(Circle c, Node n) {
        // Update coordinates of circle and edges when dragged
        c.setOnMouseDragged(e -> {
            String nodeID = c.getId().substring(0, 10);
            if (notFirst) { // Creates the list of lines to update only the first time it's dragged
                lines.clear();
                for (javafx.scene.Node node : mapHolder.getChildren()) {
                    if (node.getId().contains(nodeID)) {
                        String startID = node.getId().substring(0, 10);
                        String endID = node.getId().substring(11, 21);
                        lines.add(new LineDir((Line) node, startID, endID));
                    }
                }
                notFirst = false;
            }

            // Update node
            gPane.setGestureEnabled(false);

            // Constrains the nodes to make sure they can't be above the max coordinate or below the min
            c.setCenterX(e.getX() > (PathfindingMenuController.MAX_X / PathfindingMenuController.COORDINATE_SCALE) ? (PathfindingMenuController.MAX_X / PathfindingMenuController.COORDINATE_SCALE) : e.getX() < 0 ? 0 : e.getX());
            c.setCenterY(e.getY() > (PathfindingMenuController.MAX_Y / PathfindingMenuController.COORDINATE_SCALE) ? (PathfindingMenuController.MAX_Y / PathfindingMenuController.COORDINATE_SCALE) : e.getY() < 0 ? 0 : e.getY());

            // Update lines
            for (LineDir line : lines) {
                if (line.getStartID().equals(nodeID)) {
                    line.getLine().setStartX(c.getCenterX());
                    line.getLine().setStartY(c.getCenterY());
                } else {
                    line.getLine().setEndX(c.getCenterX());
                    line.getLine().setEndY(c.getCenterY());
                }
            }
        });

        // Update coordinates of the actual node in the database when the drag is done
        c.setOnMouseReleased(e -> {
            if (!e.isStillSincePress()) {
                n.setXCoord((int) (c.getCenterX() * PathfindingMenuController.COORDINATE_SCALE));
                n.setYCoord((int) (c.getCenterY() * PathfindingMenuController.COORDINATE_SCALE));
                gPane.setGestureEnabled(true);
                try {
                    db.updateNode(n);
                } catch (SQLException err) {
                    err.printStackTrace();
                }
                refreshEditor();
                notFirst = true;
            }
        });
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class LineDir {
        private Line line;
        private String startID;
        private String endID;

        @Override
        public String toString() {
            return "LineDir{" +
                    "line=" + line +
                    ", startID='" + startID + '\'' +
                    ", endID='" + endID + '\'' +
                    '}';
        }
    }
}
