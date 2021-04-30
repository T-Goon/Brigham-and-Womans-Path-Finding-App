package edu.wpi.cs3733.D21.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.node.AlignNodePopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AlignNodePopupController implements Initializable {

    @FXML
    public VBox mainMenu;

    @FXML
    public JFXButton btnYes;

    @FXML
    public JFXButton btnNo;

    private AlignNodePopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (AlignNodePopup) App.getPrimaryStage().getUserData();
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton button = (JFXButton) actionEvent.getSource();
        switch (button.getId()) {
            case "btnYes":
                popup.alignNodes();
                break;
            case "btnNo":
                popup.hide();
                break;
        }
    }
}
