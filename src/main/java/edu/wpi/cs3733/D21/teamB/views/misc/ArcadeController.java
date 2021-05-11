package edu.wpi.cs3733.D21.teamB.views.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.games.pong.PongApp;
import edu.wpi.cs3733.D21.teamB.games.snake.SnakeGame;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ArcadeController extends BasePageController implements Initializable {

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnSnake;

    @FXML
    private JFXButton btnPac;

    @FXML
    private JFXButton btnPong;

    @Override
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/misc/arcade.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnEmergency":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnPong":
                PongApp.main(null);
                break;
            case "btnSnake":
                SnakeGame.main(null);
                break;
        }
    }
}
