package edu.wpi.teamB.views.mapEditor.edges;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class AddEdgeMenuController {

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnAddEdge;


    // EDGES

    @FXML
    private JFXTextField edgeID;

    @FXML
    private JFXTextField startNode;

    @FXML
    private JFXTextField endNode;

    @FXML
    private void validateButton(){
        btnAddEdge.setDisable(edgeID.getText().isEmpty() || startNode.getText().isEmpty() || endNode.getText().isEmpty());
    }

    public void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnCancel":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/edges/editEdgesListMenu.fxml");
                break;
            case "btnAddEdge":
                String edgeIdentifier = edgeID.getText();
                String startNodeName = startNode.getText();
                String endNodeName = endNode.getText();
                Edge edge = new Edge(edgeIdentifier, startNodeName, endNodeName);
                DatabaseHandler.getDatabaseHandler("main.db").addEdge(edge);
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/edges/editEdgesListMenu.fxml");
                break;
        }
    }
}
