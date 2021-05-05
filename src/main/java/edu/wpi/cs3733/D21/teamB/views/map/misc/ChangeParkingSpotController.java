package edu.wpi.cs3733.D21.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.node.ChangeParkingSpotPopup;
import edu.wpi.cs3733.D21.teamB.util.IObserver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangeParkingSpotController implements Initializable {

    @FXML
    private Text parkingName;

    @FXML
    private JFXButton btnChangeParking;

    @FXML
    private JFXButton btnCancel;

    private ChangeParkingSpotPopup popup;

    private IObserver observer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (ChangeParkingSpotPopup) App.getPrimaryStage().getUserData();
        parkingName.setText(popup.getData().getNode().getLongName());
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()) {
            case "btnChangeParking":
                popup.setParkingSpot(popup.getData().getNode().getLongName());
                popup.notifyObserver();
                popup.hide();
                break;
            case "btnCancel":
                popup.hide();
                break;
        }
    }
}
