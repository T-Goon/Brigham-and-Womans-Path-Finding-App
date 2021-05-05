package edu.wpi.cs3733.D21.teamB.entities.map;

import edu.wpi.cs3733.D21.teamB.App;

import edu.wpi.cs3733.D21.teamB.entities.map.data.ETAPopupData;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.pathfinding.Graph;
import edu.wpi.cs3733.D21.teamB.util.Popup.Poppable;

import edu.wpi.cs3733.D21.teamB.util.Popup.Popup;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Objects;

public class ETAPopup extends Popup<VBox, ETAPopupData> implements Poppable {
    public ETAPopup(Pane parent, ETAPopupData data) {
        super(parent, data);
    }

    /**
     * Show the eta popup.
     */
    public void show() {

        VBox estimatedTimeBox = null;

        App.getPrimaryStage().setUserData(this);
        try {
            estimatedTimeBox = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/showEstimatedTime.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert estimatedTimeBox != null;
        estimatedTimeBox.setId("estimatedTimeDialog");
        double horizPosition, vertPosition;
        Graph graph = Graph.getGraph();
        Node secToLastNode = graph.getNodes().get(data.getPath().getPath().get(data.getPath().getPath().size() - 2));
        Node endNode = graph.getNodes().get(data.getPath().getPath().get(data.getPath().getPath().size() - 1));
        if ((secToLastNode.getXCoord() / PathfindingMenuController.COORDINATE_SCALE) < (endNode.getXCoord() / PathfindingMenuController.COORDINATE_SCALE)) {
            horizPosition = 0;
        } else {
            horizPosition = -90;
        }
        if ((secToLastNode.getYCoord() / PathfindingMenuController.COORDINATE_SCALE) < (endNode.getYCoord() / PathfindingMenuController.COORDINATE_SCALE)) {
            vertPosition = 0;

        } else {
            vertPosition = -50;
        }

        double popupX = (endNode.getXCoord() / PathfindingMenuController.COORDINATE_SCALE) + horizPosition;
        double popupY = (endNode.getYCoord() / PathfindingMenuController.COORDINATE_SCALE) + vertPosition;
        estimatedTimeBox.setLayoutX(popupX);
        estimatedTimeBox.setLayoutY(popupY);

        super.show(estimatedTimeBox);
    }
}
