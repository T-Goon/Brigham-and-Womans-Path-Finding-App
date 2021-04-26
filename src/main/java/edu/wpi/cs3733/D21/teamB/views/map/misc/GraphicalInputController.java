package edu.wpi.cs3733.D21.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.node.GraphicalInputPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GraphicalInputController implements Initializable {

    @FXML
    private Text nodeName;

    @FXML
    private JFXButton btnStart;

    @FXML
    private JFXButton btnEnd;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnAddStop;

    private GraphicalInputPopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (GraphicalInputPopup) App.getPrimaryStage().getUserData();
        nodeName.setText(popup.getData().getNodeName());
    }

    @FXML
    private void handleButtonAction(ActionEvent event){
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()){
            case "btnStart":
                popup.setStart();
                break;
            case "btnAddStop":
                popup.addStop();
                break;
            case "btnEnd":
                popup.setEnd();
                break;
            case "btnCancel":
                popup.getData().getMd().removeAllPopups();
                break;
        }
    }
}
