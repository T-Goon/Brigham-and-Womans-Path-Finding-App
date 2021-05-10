package edu.wpi.cs3733.D21.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.Coord;
import edu.wpi.cs3733.D21.teamB.entities.map.StartGamePopup;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.games.snake.Snake;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.beans.EventHandler;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StartGamePopupController implements Initializable {

    @FXML
    private JFXButton btnStartGame;

    @FXML
    private JFXButton btnCancel;

    private StartGamePopup popup;

    private Snake snake;

    private ImageView snakeImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (StartGamePopup) App.getPrimaryStage().getUserData();
        snake = new Snake(popup.getData().getMd(), popup.getData().getMc(), popup.getData().getAp(), popup.getData().getScore());
        try {
            snakeImage = FXMLLoader.load(Objects.requireNonNull(Snake.class.getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/snake.fxml")));
            popup.getData().getAp().getScene().addEventFilter(KeyEvent.KEY_PRESSED, e -> {

                if (e.getCode().equals(KeyCode.W)) {
                    snakeImage.setLayoutY(snakeImage.getLayoutY() - 10);
                    snake.getSnakeHeadLoc().setY((int) (snakeImage.getLayoutY() * PathfindingMenuController.COORDINATE_SCALE) - 10);
                } else if (e.getCode().equals(KeyCode.A)) {
                    snakeImage.setLayoutX(snakeImage.getLayoutX() - 10);
                    snake.getSnakeHeadLoc().setX((int) (snakeImage.getLayoutX() * PathfindingMenuController.COORDINATE_SCALE) - 10);
                } else if (e.getCode().equals(KeyCode.S)) {
                    snakeImage.setLayoutY(snakeImage.getLayoutY() + 10);
                    snake.getSnakeHeadLoc().setY((int) (snakeImage.getLayoutY() * PathfindingMenuController.COORDINATE_SCALE) + 10);
                } else if (e.getCode().equals(KeyCode.D)) {
                    snakeImage.setLayoutX(snakeImage.getLayoutX() + 10);
                    snake.getSnakeHeadLoc().setX((int) (snakeImage.getLayoutX() * PathfindingMenuController.COORDINATE_SCALE) + 10);
                }
                snake.checkApple();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void handleButtonAction(ActionEvent e) {
        JFXButton b = (JFXButton) e.getSource();

        switch (b.getId()) {
            case "btnStartGame":
                popup.getData().getMd().removeAllEdges();
                snake.initializeMap(snakeImage);
                popup.getData().getScore().setVisible(true);
                break;
            case "btnCancel":
                popup.getData().getMd().removeAllPopups();
                break;
        }
    }
}
