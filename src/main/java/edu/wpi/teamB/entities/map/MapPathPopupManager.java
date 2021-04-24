package edu.wpi.teamB.entities.map;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.map.data.ETAPopupData;
import edu.wpi.teamB.entities.map.data.GraphicalInputData;
import edu.wpi.teamB.entities.map.data.Node;
import edu.wpi.teamB.entities.map.data.Path;
import edu.wpi.teamB.entities.map.node.GraphicalInputPopup;
import edu.wpi.teamB.pathfinding.AStar;
import edu.wpi.teamB.views.map.PathfindingMenuController;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import net.kurobako.gesturefx.GesturePane;

public class MapPathPopupManager {

    private final MapDrawer md;
    private final JFXTextField txtStartLocation;
    private final JFXTextField txtEndLocation;
    private final PathfindingMenuController pfmc;
    private final StackPane mapStack;
    private final AnchorPane nodeHolder;
    private final GesturePane gpane;

    private GraphicalInputPopup giPopup;
    private ETAPopup etaPopup;

    public MapPathPopupManager(MapDrawer md, JFXTextField txtStartLocation, JFXTextField txtEndLocation,
                               StackPane mapStack, GesturePane gpane, PathfindingMenuController pfmc,
                               AnchorPane nodeHolder) {
        this.md = md;
        this.txtStartLocation = txtStartLocation;
        this.txtEndLocation = txtEndLocation;
        this.pfmc = pfmc;
        this.mapStack = mapStack;
        this.gpane =  gpane;
        this.nodeHolder = nodeHolder;
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
}
