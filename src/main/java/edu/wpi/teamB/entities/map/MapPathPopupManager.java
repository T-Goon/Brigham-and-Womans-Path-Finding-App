package edu.wpi.teamB.entities.map;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.map.data.GraphicalInputData;
import edu.wpi.teamB.entities.map.data.Node;
import edu.wpi.teamB.entities.map.node.GraphicalInputPopup;
import edu.wpi.teamB.views.map.PathfindingMenuController;
import javafx.scene.layout.StackPane;
import net.kurobako.gesturefx.GesturePane;

public class MapPathPopupManager {

    private MapDrawer md;
    private JFXTextField txtStartLocation;
    private JFXTextField txtEndLocation;
    private PathfindingMenuController pfmc;
    private StackPane mapStack;
    private GesturePane gpane;

    public MapPathPopupManager(MapDrawer md, JFXTextField txtStartLocation, JFXTextField txtEndLocation,
                               StackPane mapStack, GesturePane gpane, PathfindingMenuController pfmc) {
        this.md = md;
        this.txtStartLocation = txtStartLocation;
        this.txtEndLocation = txtEndLocation;
        this.pfmc = pfmc;
        this.mapStack = mapStack;
        this.gpane =  gpane;
    }

    /**
     * Creates the popup for the graphical input.
     *
     * @param n Node to create the popup for
     */
    public void createGraphicalInputPopup(Node n) {

        GraphicalInputData giData = new GraphicalInputData(n.getLongName(), txtStartLocation, txtEndLocation, md, pfmc);

        GraphicalInputPopup giPopup = new GraphicalInputPopup(mapStack, giData, gpane);

        App.getPrimaryStage().setUserData(giPopup);

        giPopup.show();
    }

    public void removeAllPopups(){

    }
}
