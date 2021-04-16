package edu.wpi.teamB.views.mapEditor;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;


public class EditorIntermediateMenuController {

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnNodes;

    @FXML
    private JFXButton btnEdges;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();
//        SceneSwitcher.pushScene("/edu/wpi/teamB/views/mapEditor/editorIntermediateMenu.fxml");

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
                case "btnEmergency":
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
