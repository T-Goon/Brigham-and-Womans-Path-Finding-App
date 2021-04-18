package edu.wpi.teamB.views.mapEditor;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;


public class EditorIntermediateMenuController {

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnNodes;

    @FXML
    private JFXButton btnEdges;
    @FXML
    private JFXButton btnEmergency;
    @FXML
    private JFXButton btnExit;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        try {
            switch (btn.getId()) {
                case "btnNodes":
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapEditor/nodes/editNodesListMenu.fxml");
                    break;
                case "btnEdges":
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapEditor/edges/editEdgesListMenu.fxml");
                    break;
                case "btnBack":
                    SceneSwitcher.switchScene(getClass(), SceneSwitcher.popScene());
                    break;
                case "btnExit":
                    Platform.exit();
                    break;
                case "btnEmergency":
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
