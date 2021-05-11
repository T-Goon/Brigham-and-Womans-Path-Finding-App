package edu.wpi.cs3733.D21.teamB.views.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AboutPageController extends BasePageController {

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnHome;

    @FXML
    public void handleButtonAction(ActionEvent e) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/misc/aboutPage.fxml";
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnEmergency":
                SceneSwitcher.switchScene(currentPath, "/edu/wpi/cs3733/D21/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnExit":
                Platform.exit();
                break;
            case "btnHome":
                SceneSwitcher.switchScene(currentPath,  "/edu/wpi/cs3733/D21/teamB/views/login/mainPage.fxml");
                break;
        }
    }
}