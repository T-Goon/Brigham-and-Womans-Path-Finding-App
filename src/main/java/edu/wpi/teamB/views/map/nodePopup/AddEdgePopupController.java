package edu.wpi.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;

import edu.wpi.teamB.entities.map.Edge;
import edu.wpi.teamB.entities.map.GraphicalNodePopupData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEdgePopupController implements Initializable {
    @FXML
    private JFXButton btnStart;

    @FXML
    private JFXButton btnEnd;

    @FXML
    private JFXButton btnDone;

    @FXML
    private JFXButton btnReset;

    @FXML
    private JFXButton btnCancel;

    private GraphicalNodePopupData data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (GraphicalNodePopupData) App.getPrimaryStage().getUserData();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()) {
            case "btnStart":
                // Set current start back to black
                String start = data.getData().getPfmc().getNewEdgeStart();
                Circle startC = data.getData().getPfmc().getStartNode();
                if (start != null && startC != null) {
                    startC.setStroke(Color.BLACK);
                }

                data.getData().getPfmc().setNewEdgeStart(data.getData().getNodeID());
                data.getData().getPfmc().setStartNode(data.getData().getCircle());

                data.getData().getCircle().setStroke(Color.RED);
                break;
            case "btnEnd":
                // Set current start back to black
                String end = data.getData().getPfmc().getNewEdgeEnd();
                Circle endC = data.getData().getPfmc().getEndNode();
                if (end != null && endC != null) {
                    endC.setStroke(Color.BLACK);
                }

                data.getData().getPfmc().setNewEdgeEnd(data.getData().getNodeID());
                data.getData().getPfmc().setEndNode(data.getData().getCircle());

                data.getData().getCircle().setStroke(Color.GREEN);
                break;
            case "btnDone":
                String startNodeName = data.getData().getPfmc().getNewEdgeStart();
                String endNodeName = data.getData().getPfmc().getNewEdgeEnd();

                if (startNodeName == null || endNodeName == null || startNodeName.equals(endNodeName)) return;

                String edgeIdentifier = startNodeName + "_" + endNodeName;

                Edge edge = new Edge(edgeIdentifier, startNodeName, endNodeName);

                // Update database and graph
                DatabaseHandler.getDatabaseHandler("main.db").addEdge(edge);

                // Remove popup from map and refresh map nodes
                data.getData().getPfmc().refreshEditor();

                data.getData().getNodeHolder().getChildren().remove(data.getParent().getRoot());

            case "btnReset":
                // Reset Node Colors
                if(data.getData().getPfmc().getStartNode() != null)
                    data.getData().getPfmc().getStartNode().setStroke(Color.BLACK);
                if(data.getData().getPfmc().getEndNode() != null)
                    data.getData().getPfmc().getEndNode().setStroke(Color.BLACK);

                data.getData().getPfmc().setStartNode(null);
                data.getData().getPfmc().setEndNode(null);

                // Reset start and end nodes
                data.getData().getPfmc().setNewEdgeStart(null);
                data.getData().getPfmc().setNewEdgeEnd(null);
                break;
            case "btnCancel":
                data.getParent().addEdgeToMain();
                break;
        }
    }
}
