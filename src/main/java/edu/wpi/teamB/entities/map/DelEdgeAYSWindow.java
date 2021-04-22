package edu.wpi.teamB.entities.map;

import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.data.DelEdgePopupData;
import edu.wpi.teamB.entities.map.data.GraphicalEdgePopupData;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.Popup.Popup;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class DelEdgeAYSWindow extends Popup<VBox, DelEdgePopupData> {

    private DelEdgePopup container;
    private MapDrawer md;

    public DelEdgeAYSWindow(Pane parent, DelEdgePopupData data) {
        super(parent, data);
    }

    public void deleteEdge(){
        DatabaseHandler.getDatabaseHandler("main.db").removeEdge(
                data.getStart().getNodeID() + "_" +
                        data.getEnd().getNodeID()
        );
        Graph.getGraph().updateGraph();

        // Remove popup from map and refresh the nodes

        container.hide();
        md.refreshEditor();
    }

    public void show() {

        // Pass data to next window
        App.getPrimaryStage().setUserData(this);

        VBox node = null;
        // Load window
        try{
            node = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/edgePopup/delEdgeAreYouSure.fxml")));
        } catch (IOException e){
            e.printStackTrace();
        }

        super.show(node);
    }

    @Override
    public void hide() {
        super.hide();
    }
}
