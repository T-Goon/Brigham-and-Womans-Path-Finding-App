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
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.text.Text;
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
    @Getter
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

    @Getter
    private final List<Circle> aligned = new ArrayList<>();
    private boolean notFirst = true;

    @Getter
    @Setter
    private boolean isEditing = false;

    @Getter
    @Setter
    private boolean mobility = false;

    @Setter
    private FloorSwitcher floorSwitcher;

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
     * Calculates the path
     *
     * @param start Long name of the start node
     * @param end   Long name of the end node
     */
    public void calculatePath(String start, String end) {
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
            case "BestFS":
                pathfinder = new BestFS();
                break;
            case "Dijkstra":
                pathfinder = new Dijkstra();
                break;
            default:
                throw new IllegalStateException("Extra option in combo box?");
        }

        //Set the final path in mapCache
        mapCache.setFinalPath(pathfinder.findPath(allStops, mobility));

        drawPath();
    }

    /**
     * Draws the path stored in the mapCache
     */
    public void drawPath() {
        Map<String, Node> nodes = Graph.getGraph().getNodes();
        removeAllEdges();

        // Head color for the animation
        if (!nodeHolder.getChildren().contains(head)) {
            nodeHolder.getChildren().add(head);
        }
        head.setFill(Color.valueOf("#0067B1"));

        if (mapCache.getFinalPath().getPath().isEmpty()) {
            // There is no path
            lblError.setText("Path could not be found between the selected locations!");
            lblError.setVisible(true);
            nodeHolder.getChildren().remove(head);
        } else {
            // There is a path
            List<String> currentFloorPath = mapCache.getFinalPath().getFloorPathSegment(mapCache.getCurrentFloor());

            colorStartStopEndNodes();

            //Draw the segment of the path that is on the current floor
            if (currentFloorPath.size() > 0) {
                Node n = db.getNodeById(currentFloorPath.get(0));
                double minX = n.getXCoord(), minY = n.getYCoord(), maxX = n.getXCoord(), maxY = n.getYCoord();
                for (int i = 0; i < currentFloorPath.size() - 1; i++) {

                    Node current = db.getNodeById(currentFloorPath.get(i));
                    if (current.getXCoord() < minX) minX = current.getXCoord();
                    if (current.getYCoord() < minY) minY = current.getYCoord();
                    if (current.getXCoord() > maxX) maxX = current.getXCoord();
                    if (current.getYCoord() > maxY) maxY = current.getYCoord();

                    //Test if the edge you are trying to place is valid
                    if (Graph.getGraph().verifyEdge(currentFloorPath.get(i), currentFloorPath.get(i + 1))) {
                        placeEdge(nodes.get(currentFloorPath.get(i)),
                                nodes.get(currentFloorPath.get(i + 1)));
                    }
                }

                // Adjust coordinate scales
                int padding = 75;
                minX = (minX / PathfindingMenuController.COORDINATE_SCALE) - padding;
                minY = (minY / PathfindingMenuController.COORDINATE_SCALE) - padding;
                maxX = (maxX / PathfindingMenuController.COORDINATE_SCALE) + padding;
                maxY = (maxY / PathfindingMenuController.COORDINATE_SCALE) + padding;

                double avgX = (minX + maxX) / 2;
                double avgY = (minY + maxY) / 2;

                // Figure out how much to zoom
                double scaleX = gPane.getWidth() / (maxX - minX);
                double scaleY = gPane.getHeight() / (maxY - minY);
                double scaleAmount = Math.min(scaleX, scaleY);

                mapCache.setAvgX(avgX);
                mapCache.setAvgY(avgY);
                mapCache.setScaleAmount(scaleAmount);

                gPane.zoomTo(scaleAmount, new Point2D(gPane.getWidth() / 2, gPane.getHeight() / 2));
                gPane.centreOn(new Point2D(avgX, avgY));
            }

            //If there is a path segment on this floor
            if (!currentFloorPath.isEmpty()) {

                if (currentFloorPath.size() == 1) {
                    nodeHolder.getChildren().remove(head);
                }

                // Animate the path
                animatePath(currentFloorPath);

            } else {
                nodeHolder.getChildren().remove(head);
            }

            if (etaPopup != null) {
                etaPopup.hide();
                etaPopup = null;
            }
            redrawNodes();
            //Creates the eta popup for this floor only
            //etaPopup = mapPathPopupManager.createETAPopup(new Path(currentFloorPath, Graph.getGraph().calculateCost(currentFloorPath)));
            etaPopup = mapPathPopupManager.createETAPopup(mapCache.getFinalPath(), new Path(currentFloorPath, Graph.getGraph().calculateCost(currentFloorPath)));

            // Color nodes that indicate a floor swap in the path
            colorNodesOnPathFloorSwitch(currentFloorPath);

        }

    }

    /**
     * Color the nodes on the path that indicate the user needs to go up or down a floor
     *
     * @param currentFloorPath List of node ids for the current path on the floor
     */
    private void colorNodesOnPathFloorSwitch(List<String> currentFloorPath) {
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
                String floorChangeNodeID = mapCache.getFinalPath().getPath().get(floorChangeNodeIndex);
                if (nextFloorVal > currentFloorVal) {
                    //Going UP

                    //Update the node icon to green to indicate that the user must go up
                    for (Node n : mapCache.getFloorNodes().get(mapCache.getCurrentFloor())) {
                        if (n.getNodeID().equals(floorChangeNodeID.substring(0, 10))) {
                            mapCache.getEditedNodes().add(n);
                            mapCache.getEditedNodesColor().put(n.getNodeID(), n.getColor());

                            n.setColor(Color.web("00ff00"));

                            placeUpDownPathIndicatorText(floorString, n);
                        }
                    }
                } else {
                    //Going Down

                    //Update the node icon to red to indicate that the user must go down

                    for (Node n : mapCache.getFloorNodes().get(mapCache.getCurrentFloor())) {
                        if (n.getNodeID().equals(floorChangeNodeID.substring(0, 10))) {
                            mapCache.getEditedNodes().add(n);
                            mapCache.getEditedNodesColor().put(n.getNodeID(), n.getColor());

                            n.setColor(Color.web("ff0000"));

                            placeUpDownPathIndicatorText(floorString, n);
                        }
                    }
                }
            }

        }
    }

    /**
     * Place the text on the map to indicate how many floors the path moves up
     *
     * @param floorString String of the floor the path goes to
     * @param n           The node the text should be attached to
     */
    private void placeUpDownPathIndicatorText(String floorString, Node n) {
        VBox floorIndicator = null;

        try {
            floorIndicator = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/floorText.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert floorIndicator != null;
        ((Text) floorIndicator.getChildren().get(0)).setText("Floor " + floorString);
        mapCache.getFloorIndicators().add(floorIndicator);

        floorIndicator.setLayoutX((n.getXCoord() / PathfindingMenuController.COORDINATE_SCALE) - 15);
        floorIndicator.setLayoutY((n.getYCoord() / PathfindingMenuController.COORDINATE_SCALE) - 30);

        floorIndicator.setOnMouseClicked(event -> {
            switch (floorString.replaceAll("0", "")) {
                case FloorSwitcher.floor3ID:
                    removeAllEdges();
                    floorSwitcher.switchFloor(FloorSwitcher.floor3ID);
                    break;
                case FloorSwitcher.floor2ID:
                    removeAllEdges();
                    floorSwitcher.switchFloor(FloorSwitcher.floor2ID);
                    break;
                case FloorSwitcher.floor1ID:
                    removeAllEdges();
                    floorSwitcher.switchFloor(FloorSwitcher.floor1ID);
                    break;
                case FloorSwitcher.floorL1ID:
                    removeAllEdges();
                    floorSwitcher.switchFloor(FloorSwitcher.floorL1ID);
                    break;
                case FloorSwitcher.floorL2ID:
                    removeAllEdges();
                    floorSwitcher.switchFloor(FloorSwitcher.floorL2ID);
                    break;
                default:
                    throw new IllegalArgumentException("Bad Floor id");
            }
        });

        nodeHolder.getChildren().add(floorIndicator);
    }

    /**
     * Color the start and end nodes of the path
     */
    private void colorStartStopEndNodes() {
        // Deal with the start and end nodes
        Path path = mapCache.getFinalPath();
        List<String> stops = mapCache.getStopsList();
        for (String floor : mapCache.getFloorNodes().keySet()) {
            for (Node n : mapCache.getFloorNodes().get(floor)) {
                if (path.getPath().get(0).equals(n.getNodeID())) {
                    mapCache.getEditedNodes().add(n);
                    mapCache.getEditedNodesColor().put(n.getNodeID(), n.getColor());

                    n.setColor(Color.web("ffff00"));
                } else if (path.getPath().get(path.getPath().size() - 1).equals(n.getNodeID())) {
                    mapCache.getEditedNodes().add(n);
                    mapCache.getEditedNodesColor().put(n.getNodeID(), n.getColor());

                    n.setColor(Color.web("ff00ff"));
                } else if (stops.contains(n.getLongName())) {
                    mapCache.getEditedNodes().add(n);
                    mapCache.getEditedNodesColor().put(n.getNodeID(), n.getColor());

                    n.setColor(Color.web("85461e"));
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
        double x, y, oldX, oldY;
        int steps = 0;
        int pathLength = 0;
        // Place head at first node
        x = Graph.getGraph().getNodes().get(currentFloorPath.get(0)).getXCoord() / PathfindingMenuController.COORDINATE_SCALE;
        y = Graph.getGraph().getNodes().get(currentFloorPath.get(0)).getYCoord() / PathfindingMenuController.COORDINATE_SCALE;
        animationPath.getElements().add(new MoveTo(x, y));

        for (int i = 0; i < currentFloorPath.size() - 1; i++) {
            steps++;
            oldX = x;
            oldY = y;
            x = Graph.getGraph().getNodes().get(currentFloorPath.get(i + 1)).getXCoord() / PathfindingMenuController.COORDINATE_SCALE;
            y = Graph.getGraph().getNodes().get(currentFloorPath.get(i + 1)).getYCoord() / PathfindingMenuController.COORDINATE_SCALE;
            pathLength += Math.sqrt(Math.pow((x - oldX), 2) + Math.pow((y - oldY), 2));
            if (Graph.getGraph().verifyEdge(currentFloorPath.get(i), currentFloorPath.get(i + 1))) {
                // Valid edge move along it
                animationPath.getElements().add(new LineTo(x, y));

            } else if (i != currentFloorPath.size() - 2) {
                // Teleport to the next section of the path
                animationPath.getElements().add(new MoveTo(x, y));
            }

        }
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(pathLength * 10));
        pathTransition.setNode(head);
        pathTransition.setPath(animationPath);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Animation.INDEFINITE);
        pathTransition.setAutoReverse(false);
        pathTransition.play();

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
    public void drawEdgesOnFloor() {
        Map<String, Edge> edges = Graph.getGraph().getEdges();
        for (Edge e : edges.values()) {
            Node start = db.getNodeById(e.getStartNodeID());
            Node end = db.getNodeById(e.getEndNodeID());

            if (start != null && end != null) {
                if (start.getFloor().equals(mapCache.getCurrentFloor()) &&
                        end.getFloor().equals(mapCache.getCurrentFloor())) {
                    placeEdge(start, end);
                }
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

            // Enlarge nodes when hovering over them
            i.setOnMouseEntered(event -> {
                if (!isEditing) {
                    i.setScaleX(2);
                    i.setScaleY(2);
                }
            });
            i.setOnMouseExited(event -> {
                if (!isEditing) {
                    i.setScaleX(1);
                    i.setScaleY(1);
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

            setUpNode(c, n);
            setUpDrag(c, n);

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

            setUpNode(c, n);
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
                if (isEditing && !e.isControlDown()) {
                    mapEditorPopupManager.showDelEdgePopup(start, end, mapStack, e, l);
                } else if (mapPathPopupManager.hasTxtDirPopup()) {
                    // Find the list of lines that the edge clicked on belongs to
                    List<Line> linesToSet = null;
                    out:
                    for (List<Line> lines : mapCache.getInstructionsToEdges().values()) {
                        for (Line line : lines) {
                            if (line.getId().equals(l.getId())) {
                                linesToSet = lines;
                                break out;
                            }
                        }
                    }
                    if (linesToSet == null) return;

                    // Find the instruction that the list of lines corresponds to
                    String instruction = null;
                    for (Map.Entry<String, List<Line>> entry : mapCache.getInstructionsToEdges().entrySet()) {
                        if (Objects.equals(linesToSet, entry.getValue())) {
                            instruction = entry.getKey();
                            break;
                        }
                    }

                    // Now figure out what the index should be based the highlight and update it
                    TxtDirPopup popup = mapPathPopupManager.getTxtDirPopup();
                    int index = -1;
                    for (int i = 0; i < popup.getDirections().size(); i++) {
                        if (popup.getDirections().get(i).getInstruction().equals(instruction)) {
                            index = i;
                            break;
                        }
                    }
                    popup.setIndex(index);
                    popup.highlight(true);
                }
            });

            l.setOnMouseEntered(event -> {
                if (isEditing) {
                    l.setStroke(Color.RED);
                    l.setOpacity(1);
                }
            });
            l.setOnMouseExited(event -> {
                if (isEditing) l.setStroke(Color.rgb(0, 103, 177, 0.5));
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

    /**
     * Redraws the highlighted node on the map
     */
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

        // Remove all edges placed
        for (Line l : mapCache.getEdgesPlaced())
            mapHolder.getChildren().remove(l);

        // Revert nodes that were colored for pathfinding to their orginal color
        if (!mapCache.getEditedNodes().isEmpty()) {
            for (Node node : mapCache.getEditedNodes()) {
                Color oldColor = mapCache.getEditedNodesColor().get(node.getNodeID());
                node.setColor(oldColor);
            }
            mapCache.getEditedNodes().clear();
            mapCache.getEditedNodesColor().clear();
            redrawNodes();
        }

        // Remove any floor text indicators from the map
        for (VBox vBox : mapCache.getFloorIndicators()) {
            nodeHolder.getChildren().remove(vBox);
        }
        mapCache.getFloorIndicators().clear();

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
     * Sets up the color changing and being clicked on properties for each circle
     *
     * @param c the circle
     * @param n the node associated with the circle
     */
    private void setUpNode(Circle c, Node n) {
        c.setOnMouseEntered(event -> {
            if (isEditing && !aligned.contains(c) && !(mapCache.getStartNode() != null && c.getId().equals(mapCache.getStartNode().getId())))
                c.setStroke(Color.GREEN);
        });
        c.setOnMouseExited(event -> {
            if (isEditing && !aligned.contains(c) && !(mapCache.getStartNode() != null && c.getId().equals(mapCache.getStartNode().getId())))
                c.setStroke(Color.BLACK);
        });

        c.setOnMouseClicked((MouseEvent e) -> {
            if (e.isStillSincePress()) {
                if (e.isControlDown()) { // if control clicking, add it to the list of highlighted circles
                    if (!aligned.contains(c)) {
                        c.setStroke(Color.RED);
                        aligned.add(c);
                    } else {
                        c.setStroke(Color.BLACK);
                        aligned.remove(c);
                    }
                } else if (mapCache.getStartNode() != null) {
                    mapCache.setNewEdgeEnd(n.getNodeID());
                    mapEditorPopupManager.showAddEdgePopup(e);
                } else mapEditorPopupManager.showEditNodePopup(n, e, false);
            }
        });
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
            // If control is pressed, don't drag
            if (e.isControlDown()) return;

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
            if (!e.isStillSincePress() && !e.isControlDown()) {
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
