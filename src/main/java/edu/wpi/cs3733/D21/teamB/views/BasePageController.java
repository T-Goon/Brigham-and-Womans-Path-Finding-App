package edu.wpi.cs3733.D21.teamB.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.util.tts.TextToSpeech;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class BasePageController implements Initializable {

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnExit;

    public TextToSpeech tts = new TextToSpeech();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventHandler<MouseEvent> onClick = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                tts.speak(event.getPickResult().getIntersectedNode().getAccessibleText(), 1.0f, false, false);
            }
        };

    }

    public void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.goBack(getClass(), 1);
                break;
            case "btnExit":
                Platform.exit();
                break;
        }
    }





}
