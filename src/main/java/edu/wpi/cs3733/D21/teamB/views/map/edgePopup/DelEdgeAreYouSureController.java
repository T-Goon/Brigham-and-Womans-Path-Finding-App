package edu.wpi.cs3733.D21.teamB.views.map.edgePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.edge.DelEdgeAYSWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DelEdgeAreYouSureController implements Initializable {

    @FXML
    private JFXButton btnYes, btnNo;

    private DelEdgeAYSWindow window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        window = (DelEdgeAYSWindow) App.getPrimaryStage().getUserData();
    }

    @FXML
    private void handleButtonAction(ActionEvent event){
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()) {
            case "btnYes":
                window.deleteEdge();
                break;
            case "btnNo":
                window.hide();
                break;
        }
    }
}
