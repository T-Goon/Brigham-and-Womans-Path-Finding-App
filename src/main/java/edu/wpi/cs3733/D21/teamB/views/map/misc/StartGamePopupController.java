package edu.wpi.cs3733.D21.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.StartGamePopup;
import edu.wpi.cs3733.D21.teamB.games.snake.Snake;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class StartGamePopupController implements Initializable {

    @FXML
    private JFXButton btnStartGame;

    @FXML
    private JFXButton btnCancel;

    private StartGamePopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (StartGamePopup) App.getPrimaryStage().getUserData();
    }

    @FXML
    public void handleButtonAction(ActionEvent e) {
        JFXButton b = (JFXButton) e.getSource();

        switch (b.getId()) {
            case "btnStartGame":
                popup.getData().getMd().removeAllEdges();
                Snake snake = new Snake(popup.getData().getMd(), popup.getData().getMc(), popup.getData().getAp());
                snake.initializeMap();
                break;
            case "btnCancel":
                popup.getData().getMd().removeAllPopups();
                break;
        }
    }
}
