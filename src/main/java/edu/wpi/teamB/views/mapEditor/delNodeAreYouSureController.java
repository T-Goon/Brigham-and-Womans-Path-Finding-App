package edu.wpi.teamB.views.mapEditor;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.GraphicalEditorNodeData;
import edu.wpi.teamB.util.GraphicalNodeDelData;
import edu.wpi.teamB.util.NodeWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class delNodeAreYouSureController implements Initializable {

    @FXML
    private JFXButton btnYes;

    @FXML
    private JFXButton btnNo;

    private GraphicalNodeDelData data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (GraphicalNodeDelData) App.getPrimaryStage().getUserData();
    }

    @FXML
    private void handleButtonAction(ActionEvent e){
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()){
            case "btnYes":
                DatabaseHandler.getDatabaseHandler("main.db").removeNode(data.getData().getNodeID());
                Graph.getGraph().updateGraph();
                data.getData().getPfmc().refreshEditor();

                data.getData().getNodeHolder().getChildren().remove(data.getParent().getRoot());
                break;
            case "btnNo":
                data.getParent().areYouSureToMain();
                break;
        }
    }
}
