package edu.wpi.cs3733.D21.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.node.AlignNodePopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AlignNodePopupController implements Initializable {

    @FXML
    public VBox mainMenu;

    @FXML
    public JFXRadioButton optionBestFit;

    @FXML
    public JFXRadioButton optionHorizontal;

    @FXML
    public JFXRadioButton optionVertical;

    @FXML
    public JFXButton btnYes;

    @FXML
    public JFXButton btnNo;

    private AlignNodePopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (AlignNodePopup) App.getPrimaryStage().getUserData();

        optionBestFit.setSelected(true);
        final ToggleGroup group = new ToggleGroup();
        optionBestFit.setToggleGroup(group);
        optionHorizontal.setToggleGroup(group);
        optionVertical.setToggleGroup(group);
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton button = (JFXButton) actionEvent.getSource();
        switch (button.getId()) {
            case "btnYes":
                popup.alignNodes(getOptionSelected());
                popup.hide();
                break;
            case "btnNo":
                popup.hide();
                break;
        }
    }

    private AlignOptions getOptionSelected() {
        if (optionBestFit.isSelected()) return AlignOptions.BEST_FIT;
        else if (optionHorizontal.isSelected()) return AlignOptions.HORIZONTAL;
        else return AlignOptions.VERTICAL;
    }

    public enum AlignOptions {
        BEST_FIT,
        HORIZONTAL,
        VERTICAL
    }
}
