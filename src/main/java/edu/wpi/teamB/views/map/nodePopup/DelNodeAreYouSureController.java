package edu.wpi.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.node.DelNodeAYSWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.kurobako.gesturefx.GesturePane;

import java.net.URL;
import java.util.ResourceBundle;

public class DelNodeAreYouSureController implements Initializable {

    @FXML
    private JFXButton btnYes;

    @FXML
    private JFXButton btnNo;

    private DelNodeAYSWindow window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        window = (DelNodeAYSWindow) App.getPrimaryStage().getUserData();
    }

    @FXML
    private void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnYes":
                window.delNode();
                break;
            case "btnNo":
                window.hide();
                break;
        }
    }
}
