package edu.wpi.cs3733.D21.teamB.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.util.tts.TextToSpeech;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BasePageController implements Initializable {

    public boolean firstFocused;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnExit;

    @FXML
    private StackPane stackPane;

    public TextToSpeech tts = new TextToSpeech();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstFocused = true;
        Platform.runLater(() -> stackPane.requestFocus());
        for (Node aNode : stackPane.lookupAll("*")) {
            aNode.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (DatabaseHandler.getHandler().getAuthenticationUser().getTtsEnabled().equals("T")) {
                    if (newValue) {
                        String speechOut = aNode.getAccessibleText();
                        if (firstFocused) {
                            speechOut = null;
                            firstFocused = false;
                        }
                        if (speechOut != null) {
                            tts.speak(speechOut, 1.0f, false, false);
                        }
                    }
                }
            });
        }
        tts.stopSpeaking();
    }

    public void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.goBack(1);
                break;
            case "btnExit":
                Platform.exit();
                break;
        }
    }


}
