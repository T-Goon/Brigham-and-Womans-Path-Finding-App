package edu.wpi.teamB.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class addNodeMenuController implements Initializable {
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnAddNode;
    @FXML
    private JFXRadioButton notRestricted;
    @FXML
    private JFXRadioButton restricted;
    @FXML
    private ToggleGroup areaGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notRestricted.setToggleGroup(areaGroup);
        restricted.setToggleGroup(areaGroup);
    }

    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()){
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/nodesEditorMenu.fxml");
                break;
            case "btnAddNode":

                break;
        }
    }



}
