package edu.wpi.cs3733.D21.teamB.entities.map;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.data.*;
import edu.wpi.cs3733.D21.teamB.entities.map.node.GraphicalInputPopup;
import edu.wpi.cs3733.D21.teamB.entities.map.node.TxtDirPopup;
import edu.wpi.cs3733.D21.teamB.pathfinding.AStar;
import edu.wpi.cs3733.D21.teamB.pathfinding.Directions;
import edu.wpi.cs3733.D21.teamB.util.Popup.PoppableManager;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import net.kurobako.gesturefx.GesturePane;

import java.util.List;

public class MapPathPopupManager implements PoppableManager {

    private final MapDrawer md;
    private final JFXTextField txtStartLocation;
    private final JFXTextField txtEndLocation;
    private final PathfindingMenuController pfmc;
    private final StackPane mapStack;
    private final AnchorPane nodeHolder;
    private final GesturePane gpane;
    private final StackPane textDirectionsHolder;

    private GraphicalInputPopup giPopup;
    private ETAPopup etaPopup;
    private TxtDirPopup txtDirPopup;

    public MapPathPopupManager(MapDrawer md, JFXTextField txtStartLocation, JFXTextField txtEndLocation,
                               StackPane mapStack, GesturePane gpane, PathfindingMenuController pfmc,
                               AnchorPane nodeHolder, StackPane textDirectionsHolder) {
        this.md = md;
        this.txtStartLocation = txtStartLocation;
        this.txtEndLocation = txtEndLocation;
        this.pfmc = pfmc;
        this.mapStack = mapStack;
        this.gpane =  gpane;
        this.nodeHolder = nodeHolder;
        this.textDirectionsHolder = textDirectionsHolder;
    }

    /**
     * Creates the popup for the graphical input.
     *
     * @param n Node to create the popup for
     */
    public void createGraphicalInputPopup(Node n) {

        GraphicalInputData giData = new GraphicalInputData(n.getLongName(), txtStartLocation, txtEndLocation, md, pfmc);

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

        String estimatedTime = AStar.getEstimatedTime(path);

        ETAPopupData etaPopupData = new ETAPopupData(estimatedTime, path);
        etaPopup = new ETAPopup(nodeHolder, etaPopupData);


        etaPopup.show();

        return etaPopup;
    }

    public TxtDirPopup createTxtDirPopup(Path path){

        List<String> instructions = Directions.instructions(path);

        TxtDirPopupData txtDirPopupData = new TxtDirPopupData(instructions);
        txtDirPopup = new TxtDirPopup(textDirectionsHolder, txtDirPopupData, gpane);

        App.getPrimaryStage().setUserData(txtDirPopup);

        txtDirPopup.show();
        return txtDirPopup;
    }

    /**
     * Remove the etaPopup from the map
     */
    public void removeETAPopup(){
        if(etaPopup != null){
            etaPopup.hide();
            etaPopup = null;
        }
    }

    /**
     * Remove all popups managed my this class.
     */
    public void removeAllPopups(){

        if(giPopup != null){
            giPopup.hide();
            giPopup = null;
        }
    }

    public void removeTxtDirPopup(){
        if(txtDirPopup != null){
            txtDirPopup.hide();
            txtDirPopup = null;
        }
    }
}
