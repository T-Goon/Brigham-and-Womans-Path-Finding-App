package edu.wpi.teamB.entities.map;

import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.map.data.*;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.Popup.Popup;
import edu.wpi.teamB.util.Popup.PopupManager;
import edu.wpi.teamB.views.map.PathfindingMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.util.*;

public class MapEditorPopupManager {

    private MapDrawer md;
    private AddNodePopup anp;
    private GesturePane gPane;

    public MapEditorPopupManager(MapDrawer md, GesturePane gPane) {
        this.md = md;
        this.gPane = gPane;
    }

    /**
     * Show the popup to add a node
     * @param event The mouse event that triggered the popup to be shown.
     * @param currentFloor String of the current floor of the hospital.
     * @param parent The parent pane of the popup
     */
    private void showAddNodePopup(MouseEvent event, String currentFloor, Pane parent) {

        // Coordinates on the map
        double x = event.getX();
        double y = event.getY();


        // Only one window open at a time;
        md.removeAllPopups();

        AddNodePopupData data = new AddNodePopupData(null,
                x * PathfindingMenuController.coordinateScale,
                y * PathfindingMenuController.coordinateScale,
                currentFloor,
                null,
                null,
                null,
                null,
                null,
                md,
                gPane);

        AddNodePopup anp = new AddNodePopup(parent, data);

        App.getPrimaryStage().setUserData(anp);

        anp.show();
    }

    /**
     * Shows the popup to delete an edge.
     * @param start The start node of the edge.
     * @param end The end node of the edge.
     */
    public void showDelEdgePopup(Node start, Node end, Pane parent) {
        // Make sure there is only one editNodePopup at one time
        removeAllPopups();

        DelEdgePopupData delData = new DelEdgePopupData(start, end, gPane);

        DelEdgePopup dePopup = new DelEdgePopup(parent, delData);

        // Pass window data
        App.getPrimaryStage().setUserData(delData);


        dePopup.show();
    }

    /**
     * Shows the edit node popup filled in with the information from n.
     *
     * @param n Node that is to be edited.
     */
    public void showEditNodePopup(Node n, MouseEvent event, boolean fromTreeView) {
        Circle c;
        if (fromTreeView) c = null;
        else c = (Circle) event.getSource();

        // Make sure there is only one editNodePopup at one time
        removeAllPopups();

        // Data to pass to popup
        App.getPrimaryStage().setUserData(new GraphicalEditorNodeData(
                n.getNodeID(),
                n.getXCoord(),
                n.getYCoord(),
                currentFloor,
                n.getBuilding(),
                n.getNodeType(),
                n.getLongName(),
                n.getShortName(),
                null,
                mapStack,
                PathfindingMenuController.this,
                c));

        // Load popup
        try {
            editNodePopup = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/nodePopupWindow.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        placePopupOnMap(editNodePopup);
    }

    public void removeAllPopups(){

    }
}
