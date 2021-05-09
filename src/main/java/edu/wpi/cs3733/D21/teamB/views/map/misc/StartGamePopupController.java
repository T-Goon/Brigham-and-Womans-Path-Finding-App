package edu.wpi.cs3733.D21.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.StartGamePopup;
import edu.wpi.cs3733.D21.teamB.games.snake.Snake;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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

    private ImageView snakeImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (StartGamePopup) App.getPrimaryStage().getUserData();
        try {
            snakeImage = FXMLLoader.load(Objects.requireNonNull(Snake.class.getResource("/edu/wpi/cs3733/D21/teamB/views/map/misc/snake.fxml")));
            System.out.println(App.getPrimaryStage().getScene().getFocusOwner().toString());
            popup.getData().getAp().getScene().addEventFilter(KeyEvent.KEY_PRESSED, e -> {

                if (e.getCode().equals(KeyCode.W)) {
                    snakeImage.setLayoutY(snakeImage.getLayoutY() - 10);
                } else if (e.getCode().equals(KeyCode.A)) {
                    snakeImage.setLayoutX(snakeImage.getLayoutX() - 10);
                } else if (e.getCode().equals(KeyCode.S)) {
                    snakeImage.setLayoutY(snakeImage.getLayoutY() + 10);
                } else if (e.getCode().equals(KeyCode.D)) {
                    snakeImage.setLayoutX(snakeImage.getLayoutX() + 10);
                }

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
                Snake snake = new Snake(popup.getData().getMd(), popup.getData().getMc(), popup.getData().getAp());
                snake.initializeMap(snakeImage);
                break;
            case "btnCancel":
                popup.getData().getMd().removeAllPopups();
                break;
        }
    }
}
