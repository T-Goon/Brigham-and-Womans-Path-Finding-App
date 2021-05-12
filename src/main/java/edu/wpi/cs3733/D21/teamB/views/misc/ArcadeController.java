package edu.wpi.cs3733.D21.teamB.views.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.games.pacman.Pacman;
import edu.wpi.cs3733.D21.teamB.games.breakout.Breakout;
import edu.wpi.cs3733.D21.teamB.games.snake.GameFrame;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.awt.*;

public class ArcadeController extends BasePageController implements Initializable {

    @FXML
    private JFXButton btnSnake;

    @FXML
    private JFXButton btnPac;

    @FXML
    private JFXButton btnBreakout;

    private Stage stage;

    @Override
    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnBreakout":
                EventQueue.invokeLater(() -> {
                    Breakout game = new Breakout();
                    game.setVisible(true);
                });
                break;
            case "btnSnake":
                GameFrame game = new GameFrame();
                break;
            case "btnPac":
                EventQueue.invokeLater(() -> {
                    Pacman pacman = new Pacman();
                    pacman.setVisible(true);
                });
                break;
        }
    }
}
