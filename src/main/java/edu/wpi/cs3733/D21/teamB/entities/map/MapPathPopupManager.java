package edu.wpi.cs3733.D21.teamB.entities.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.data.*;
import edu.wpi.cs3733.D21.teamB.entities.map.node.ChangeParkingSpotPopup;
import edu.wpi.cs3733.D21.teamB.entities.map.node.GraphicalInputPopup;
import edu.wpi.cs3733.D21.teamB.pathfinding.Directions;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamB.util.Popup.PoppableManager;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import net.kurobako.gesturefx.GesturePane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapPathPopupManager implements PoppableManager {

    private final MapDrawer md;
    private final MapCache mc;
    private final JFXTextField txtStartLocation;
    private final JFXTextField txtEndLocation;
    private final JFXButton btnRemoveStop;
    private final PathfindingMenuController pfmc;
    private final StackPane mapStack;
    private final AnchorPane nodeHolder;
    private final GesturePane gpane;
    private final StackPane textDirectionsHolder;

    private GraphicalInputPopup giPopup;
    private ETAPopup etaPopup;
    @Getter
    private TxtDirPopup txtDirPopup;
    private ChangeParkingSpotPopup cpsPopup;

    public MapPathPopupManager(MapDrawer md, MapCache mc, JFXTextField txtStartLocation, JFXTextField txtEndLocation,
                               JFXButton btnRmStop, StackPane mapStack, GesturePane gpane,
                               PathfindingMenuController pfmc, AnchorPane nodeHolder, StackPane textDirectionsHolder) {
        this.md = md;
        this.mc = mc;
        this.txtStartLocation = txtStartLocation;
        this.txtEndLocation = txtEndLocation;
        this.btnRemoveStop = btnRmStop;
        this.pfmc = pfmc;
        this.mapStack = mapStack;
        this.gpane = gpane;
        this.nodeHolder = nodeHolder;
        this.textDirectionsHolder = textDirectionsHolder;
    }

    /**
     * Creates the popup for the graphical input.
     *
     * @param n Node to create the popup for
     */
    public void createGraphicalInputPopup(Node n) {

        GraphicalInputData giData = new GraphicalInputData(n, txtStartLocation, txtEndLocation,
                btnRemoveStop, md, mc, pfmc, this);

        giPopup = new GraphicalInputPopup(mapStack, giData, gpane);

        App.getPrimaryStage().setUserData(giPopup);

        giPopup.show();
    }


    /**
     * Draw the estimated time dialog box
     *
     * @param path the path to draw the box on
     */
    public ETAPopup createETAPopup(Path path) {

        // No path
        if (path.getPath().size() == 0) return null;

        String estimatedTime = Graph.getEstimatedTime(path);

        ETAPopupData etaPopupData = new ETAPopupData(estimatedTime, path);
        etaPopup = new ETAPopup(nodeHolder, etaPopupData);

        etaPopup.show();

        return etaPopup;
    }

    public TxtDirPopup createTxtDirPopup(Path path) {

        Map<String, String> longToId = mc.makeLongToIDMap();

        List<String> ids = new ArrayList<>();

        for(String longName : mc.getStopsList()){
            ids.add(longToId.get(longName));
        }

        List<Directions.Direction> instructions = Directions.instructions(path, ids);
        if (instructions == null) return null;
        TxtDirPopupData txtDirPopupData = new TxtDirPopupData(instructions, md, mc, pfmc.getFloorSwitcher(), pfmc.getGpane());
        txtDirPopup = new TxtDirPopup(textDirectionsHolder, txtDirPopupData);
        App.getPrimaryStage().setUserData(txtDirPopup);
        txtDirPopup.show();

        return txtDirPopup;
    }

    /**
     * Creates the popup for changing parking spot.
     *
     * @param parent the outer VBox of the popup
     * @param data the data for the graphical input
     * @param previous the inner VBox of the popup
     * @return the popup for changing the parking spot
     */
    public ChangeParkingSpotPopup createChangeParkingSpotPopup(VBox parent, GraphicalInputData data, VBox previous) {

        cpsPopup = new ChangeParkingSpotPopup(parent, data, previous);

        App.getPrimaryStage().setUserData(cpsPopup);

        cpsPopup.show();

        return cpsPopup;
    }


    public boolean hasTxtDirPopup() {
        return txtDirPopup != null;
    }


    /**
     * Remove the etaPopup from the map
     */
    public void removeETAPopup() {
        if (etaPopup != null) {
            etaPopup.hide();
            etaPopup = null;
        }
    }

    /**
     * Returns the text direction popup from the map
     */
    public void removeTxtDirPopup() {
        if (txtDirPopup != null) {
            txtDirPopup.hide();
            txtDirPopup = null;
        }
    }

    /**
     * Remove all popups managed my this class.
     */
    public void removeAllPopups() {
        if (giPopup != null) {
            giPopup.hide();
            giPopup = null;
        }
    }
}
