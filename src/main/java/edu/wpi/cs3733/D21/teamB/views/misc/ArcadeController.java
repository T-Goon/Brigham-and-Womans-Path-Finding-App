package edu.wpi.cs3733.D21.teamB.views.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.games.PacMan.Pacman;
import edu.wpi.cs3733.D21.teamB.games.breakout.Breakout;
import edu.wpi.cs3733.D21.teamB.games.snake.gameFrame;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.var;

import java.awt.*;

public class ArcadeController extends BasePageController implements Initializable {

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnSnake;

    @FXML
    private JFXButton btnPac;

    @FXML
    private JFXButton btnBreakout;

    @Override
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/misc/arcade.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnEmergency":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnBreakout":
                EventQueue.invokeLater(() -> {
                    var game = new Breakout();
                    game.setVisible(true);
                });
                break;
            case "btnSnake":
                gameFrame game = new gameFrame();
                break;
            case "btnPac":
                EventQueue.invokeLater(() -> {
                var pacman = new Pacman();
                pacman.setVisible(true);
                });
        }
    }
}
