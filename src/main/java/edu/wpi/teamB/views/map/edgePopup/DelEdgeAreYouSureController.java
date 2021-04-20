package edu.wpi.teamB.views.map.edgePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.entities.map.GraphicalEdgePopupData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DelEdgeAreYouSureController implements Initializable {
    @FXML
    private JFXButton btnYes;

    @FXML
    private JFXButton btnNo;

    private GraphicalEdgePopupData data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (GraphicalEdgePopupData) App.getPrimaryStage().getUserData();
    }

    @FXML
    private void handleButtonAction(ActionEvent event){
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()){
            case "btnYes":
                DatabaseHandler.getDatabaseHandler("main.db").removeEdge(
                        data.getData().getStart().getNodeID() + "_" +
                                data.getData().getEnd().getNodeID()
                );
                Graph.getGraph().updateGraph();

                // Remove popup from map and refresh the nodes
                data.getData().getNodeHolder().getChildren().remove(data.getParent().getRoot());
                data.getData().getPmfc().refreshEditor();
                break;
            case "btnNo":
                data.getParent().areYouSureToMain();
                break;
        }
    }
}
