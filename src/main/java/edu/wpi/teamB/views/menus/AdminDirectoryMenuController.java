package edu.wpi.teamB.views.menus;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminDirectoryMenuController {

    @FXML
    private JFXButton btnDatabase;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnMapEditor;

    @FXML
    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch (btn.getId()) {
            case "btnMapEditor":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/adminDirectoryMenu.fxml", "/edu/wpi/teamB/views/mapEditor/editorIntermediateMenu.fxml");
                break;
            case "btnDatabase":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/adminDirectoryMenu.fxml", "/edu/wpi/teamB/views/requestForms/serviceRequestDatabase.fxml");
                break;
            case "btnBack":
                SceneSwitcher.goBack(getClass());
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/adminDirectoryMenu.fxml", "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnExit":
                Platform.exit();
                break;
        }

    }
}
