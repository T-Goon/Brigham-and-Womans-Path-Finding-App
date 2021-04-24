package edu.wpi.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.map.node.GraphicalInputPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GraphicalInputController implements Initializable {

    @FXML
    private Text nodeName;

    @FXML
    private JFXButton btnStart;

    @FXML
    private JFXButton btnEnd;

    @FXML
    private JFXButton btnCancel;

    private GraphicalInputPopup p;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        p = (GraphicalInputPopup) App.getPrimaryStage().getUserData();
        nodeName.setText(p.getData().getNodeName());
    }

    @FXML
    private void handleButtonAction(ActionEvent event){
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()){
            case "btnStart":
                p.getData().getStartTxt().setText(
                        p.getData().getNodeName()
                );
                p.getData().getMd().removeAllPopups();
                p.getData().getPfmc().validateFindPathButton();
                break;
            case "btnEnd":
                p.getData().getEndTxt().setText(
                        p.getData().getNodeName()
                );
                p.getData().getMd().removeAllPopups();
                p.getData().getPfmc().validateFindPathButton();
                break;
            case "btnCancel":
                p.getData().getMd().removeAllPopups();
                break;
        }
    }
}
