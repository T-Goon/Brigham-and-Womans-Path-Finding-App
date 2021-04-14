package edu.wpi.teamB.views.mapEditor.edges;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
    private JFXComboBox<Label> startNode;

    @FXML
    private JFXComboBox<Label> endNode;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage stage = App.getPrimaryStage();
        Edge edge = (Edge) stage.getUserData();

        // Set default values
        edgeID.setText(edge.getEdgeID());
        startNode.setValue(new Label(edge.getStartNodeName()));
        endNode.setValue(new Label(edge.getEndNodeName()));

        // Set
        Map<String, Edge> edges = null;
        try {
             edges = DatabaseHandler.getDatabaseHandler("main.db").getEdges();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Edge e : edges.values()) {
            startNode.getItems().add(new Label(e.getStartNodeName()));
            endNode.getItems().add(new  Label(e.getEndNodeName()));
        }
    }

    @FXML
    private void validateButton(){
        btnUpdateEdge.setDisable(edgeID.getText().isEmpty());
    }

    public void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnCancel":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/edges/editEdgesListMenu.fxml");
                break;
            case "btnUpdateEdge":
                String edgeIdentifier = edgeID.getText();
                String startNodeName = startNode.getValue().getText();
                String endNodeName = endNode.getValue().getText();
                Edge edge = new Edge(edgeIdentifier, startNodeName, endNodeName);
                DatabaseHandler.getDatabaseHandler("main.db").updateEdge(edge);
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/edges/editEdgesListMenu.fxml");
                break;
        }
    }
}
