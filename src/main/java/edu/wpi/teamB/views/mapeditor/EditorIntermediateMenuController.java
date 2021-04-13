package edu.wpi.teamB.views.mapeditor;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.SceneSwitcher;
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
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        try {
            switch (btn.getId()) {
                case "btnNodes":
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/nodes/nodesEditorMenu.fxml");
                    break;
                case "btnEdges":
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/edges/edgesEditorMenu.fxml");
                    break;
                case "btnBack":
                    SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/patientDirectoryMenu.fxml");
                    break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
