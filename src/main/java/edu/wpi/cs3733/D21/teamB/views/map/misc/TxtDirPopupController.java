package edu.wpi.cs3733.D21.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.node.TxtDirPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class TxtDirPopupController  implements Initializable {

    @FXML
    private JFXButton btnClose;

    @FXML
    private Text text;

    private TxtDirPopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (TxtDirPopup) App.getPrimaryStage().getUserData();

        text.setText(popup.getDirections());
    }

    @FXML
    private void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();

        if ("btnClose".equals(btn.getId())) {
            popup.hide();
        }
    }

}
