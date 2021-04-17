package edu.wpi.teamB.views.mapEditor;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.util.GraphicalEditorNodeData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class NodePopupWindowController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private Text nodeName;

    @FXML
    private JFXButton btnEditNode;

    @FXML
    private JFXButton btnAddEdge;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnCancel;

    private GraphicalEditorNodeData data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (GraphicalEditorNodeData) App.getPrimaryStage().getUserData();

        nodeName.setText(data.getLongName());
    }

    @FXML
    private void handleButtonAction(ActionEvent e){

        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()){
            case "btnEditNode":
                break;
            case "btnAddEdge":
                break;
            case "btnDelete":
                break;
            case "btnCancel":
                data.getNodeHolder().getChildren().remove(root);
                break;
        }
    }
}
