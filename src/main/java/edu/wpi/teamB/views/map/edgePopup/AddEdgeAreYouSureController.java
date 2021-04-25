package edu.wpi.teamB.views.map.edgePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.map.edge.AddEdgePopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEdgeAreYouSureController implements Initializable {

    @FXML
    private JFXButton btnYes;

    @FXML
    private JFXButton btnNo;

    private AddEdgePopup window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        window = (AddEdgePopup) App.getPrimaryStage().getUserData();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()) {
            case "btnYes":
                window.addEdge();
                break;
            case "btnNo":
                window.reset();
                window.getData().getMd().removeAllPopups();
                break;
        }
    }
}
