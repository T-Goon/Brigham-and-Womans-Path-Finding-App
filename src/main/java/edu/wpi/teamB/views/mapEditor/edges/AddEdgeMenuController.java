package edu.wpi.teamB.views.mapEditor.edges;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Edge;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class AddEdgeMenuController implements Initializable {

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnAddEdge;

    @FXML
    private JFXTextField edgeID;

    @FXML
    private JFXComboBox<String> startNode;

    @FXML
    private JFXComboBox<String> endNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Give the combo boxes all the values
        Map<String, Node> nodes = DatabaseHandler.getDatabaseHandler("main.db").getNodes();
        for (Node n : nodes.values()) {
            startNode.getItems().add(n.getNodeID());
            endNode.getItems().add(n.getNodeID());
        }
    }

    @FXML
    private void generateEdgeID() {
        if (!startNode.getSelectionModel().isEmpty() && !endNode.getSelectionModel().isEmpty()) {
            edgeID.setText(startNode.getValue() + "_" + endNode.getValue());
            btnAddEdge.setDisable(false);
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnCancel":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapEditor/edges/editEdgesListMenu.fxml");
                break;
            case "btnAddEdge":
                String edgeIdentifier = edgeID.getText();
                String startNodeName = startNode.getValue();
                String endNodeName = endNode.getValue();
                Edge edge = new Edge(edgeIdentifier, startNodeName, endNodeName);
                DatabaseHandler.getDatabaseHandler("main.db").addEdge(edge);
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapEditor/edges/editEdgesListMenu.fxml");
                break;
        }
    }
}
