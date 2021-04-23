package edu.wpi.teamB.views.map.edgePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.map.edge.DelEdgeAYSWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DelEdgeAreYouSureController implements Initializable {
    @FXML
    private JFXButton btnYes;

    @FXML
    private JFXButton btnNo;

    private DelEdgeAYSWindow data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (DelEdgeAYSWindow) App.getPrimaryStage().getUserData();
    }

    @FXML
    private void handleButtonAction(ActionEvent event){
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()){
            case "btnYes":
                data.deleteEdge();
                break;
            case "btnNo":
                data.hide();
                break;
        }
    }
}
