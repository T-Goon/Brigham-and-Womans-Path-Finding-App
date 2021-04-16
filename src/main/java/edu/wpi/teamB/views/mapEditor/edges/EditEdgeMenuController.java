package edu.wpi.teamB.views.mapEditor.edges;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.Edge;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class EditEdgeMenuController implements Initializable {

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnUpdateEdge;

    @FXML
    private JFXTextField edgeID;

    @FXML
    private JFXComboBox<String> startNode;

    @FXML
    private JFXComboBox<String> endNode;

    private String oldEdgeID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage stage = App.getPrimaryStage();
        Edge edge = (Edge) stage.getUserData();

        // Set default values of strings
        oldEdgeID = edge.getEdgeID();
        edgeID.setText(edge.getEdgeID());

        // Give the combo boxes all the values
        Map<String, Edge> edges = DatabaseHandler.getDatabaseHandler("main.db").getEdges();
        for (Edge e : edges.values()) {
            startNode.getItems().add(e.getStartNodeID());
            endNode.getItems().add(e.getEndNodeID());
        }

        // Set default values of combo box
        startNode.getSelectionModel().select(edge.getStartNodeID());
        endNode.getSelectionModel().select(edge.getEndNodeID());
    }

    @FXML
    private void generateEdgeID() {
        if (!startNode.getSelectionModel().isEmpty() && !endNode.getSelectionModel().isEmpty())
            edgeID.setText(startNode.getValue() + "_" + endNode.getValue());
    }

    @FXML
    private void validateButton() {
        btnUpdateEdge.setDisable(edgeID.getText().isEmpty() || startNode.getSelectionModel().isEmpty() || endNode.getSelectionModel().isEmpty());
    }

    public void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnCancel":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapEditor/edges/editEdgesListMenu.fxml");
                break;
            case "btnUpdateEdge":
                String edgeIdentifier = edgeID.getText();
                String startNodeName = startNode.getValue();
                String endNodeName = endNode.getValue();
                Edge edge = new Edge(edgeIdentifier, startNodeName, endNodeName);

                // Remove the old edge out of the database and put in the new one because the primary key changes
                DatabaseHandler db = DatabaseHandler.getDatabaseHandler("main.db");
                db.removeEdge(oldEdgeID);
                db.addEdge(edge);
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapEditor/edges/editEdgesListMenu.fxml");
                break;
        }
    }
}
