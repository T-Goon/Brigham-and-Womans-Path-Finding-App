package edu.wpi.teamB.entities.map;

import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.map.data.*;
import edu.wpi.teamB.entities.map.edge.DelEdgePopup;
import edu.wpi.teamB.entities.map.node.AddNodePopup;
import edu.wpi.teamB.entities.map.node.NodeMenuPopup;
import edu.wpi.teamB.views.map.PathfindingMenuController;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import net.kurobako.gesturefx.GesturePane;

public class MapEditorPopupManager {

    private MapDrawer md;
    private MapCache mc;
    private AddNodePopup anp;
    private GesturePane gPane;
    private StackPane mapStack;

    public MapEditorPopupManager(MapDrawer md, MapCache mc, GesturePane gPane, StackPane mapStack) {
        this.md = md;
        this.mc = mc;
        this.gPane = gPane;
        this.mapStack = mapStack;
    }

    /**
     * Show the popup to add a node
     * @param event The mouse event that triggered the popup to be shown.
     */
    private void showAddNodePopup(MouseEvent event) {

        // Coordinates on the map
        double x = event.getX();
        double y = event.getY();


        // Only one window open at a time
        md.removeAllPopups();

        AddNodePopupData data = new AddNodePopupData(
                x * PathfindingMenuController.coordinateScale,
                y * PathfindingMenuController.coordinateScale,
                mc.getCurrentFloor(),
                md,
                gPane);

        AddNodePopup anp = new AddNodePopup(mapStack, data);

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

        md.removeAllPopups();

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

        NodeMenuPopupData npData = new NodeMenuPopupData(
                n.getNodeID(),
                n.getXCoord(),
                n.getYCoord(),
                n.getFloor(),
                n.getBuilding(),
                n.getNodeType(),
                n.getLongName(),
                n.getShortName(),
                fromTreeView,
                md
        );

        NodeMenuPopup nmPopup = new NodeMenuPopup(mapStack, npData, gPane);

        // Make sure there is only one editNodePopup at one time
        md.removeAllPopups();

        // Data to pass to popup
        App.getPrimaryStage().setUserData(nmPopup);

        nmPopup.show();
    }

    public void removeAllPopups(){

    }
}
