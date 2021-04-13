package edu.wpi.teamB.views.mapeditor.edges;

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
    public JFXButton btnEmergency;

    @FXML
    public JFXButton btnCancel;

    @FXML
    public JFXButton btnAddEdge;


    // EDGES

    @FXML
    public JFXTextField edgeID;

    @FXML
    public JFXTextField startNode;

    @FXML
    public JFXTextField endNode;

    @FXML
    private void validateButton(){
        if (!edgeID.getText().isEmpty() && !startNode.getText().isEmpty() && !endNode.getText().isEmpty()) {
            btnAddEdge.setDisable(false);
        }
        else {
            btnAddEdge.setDisable(true);
        }}

    public void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnCancel":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/edges/edgeEditorMenu.fxml");
                break;
            case "btnAddEdge":
                String edgeIdentifier = edgeID.getText();
                String startNodeName = startNode.getText();
                String endNodeName = endNode.getText();
                Edge edge = new Edge(edgeIdentifier, startNodeName, endNodeName);
                DatabaseHandler.getDatabaseHandler("main.db").addEdge(edge);
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/edges/edgeEditorMenu.fxml");
                break;
        }
    }
}
