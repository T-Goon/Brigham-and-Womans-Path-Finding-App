package edu.wpi.teamB.views.mapEditor.edges;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    private JFXTextField startNode;

    @FXML
    private JFXTextField endNode;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage stage = App.getPrimaryStage();
        Edge edge = (Edge) stage.getUserData();

        edgeID.setText(edge.getEdgeID());
        startNode.setText(edge.getStartNodeName());
        endNode.setText(edge.getEndNodeName());
    }

    @FXML
    private void validateButton(){
        btnUpdateEdge.setDisable(edgeID.getText().isEmpty() || startNode.getText().isEmpty() || endNode.getText().isEmpty());
    }

    public void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnCancel":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/edges/editEdgesListMenu.fxml");
                break;
            case "btnUpdateEdge":
                String edgeIdentifier = edgeID.getText();
                String startNodeName = startNode.getText();
                String endNodeName = endNode.getText();
                Edge edge = new Edge(edgeIdentifier, startNodeName, endNodeName);
                DatabaseHandler.getDatabaseHandler("main.db").updateEdge(edge);
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/edges/editEdgesListMenu.fxml");
                break;
        }
    }
}
