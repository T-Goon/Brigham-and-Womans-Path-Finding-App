package edu.wpi.teamB.views;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class BasePageController {

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnExit;

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
