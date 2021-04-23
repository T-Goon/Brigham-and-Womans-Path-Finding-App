package edu.wpi.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;

import edu.wpi.teamB.entities.map.node.AddEdgeWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEdgePopupController implements Initializable {
    @FXML
    private JFXButton btnStart;

    @FXML
    private JFXButton btnEnd;

    @FXML
    private JFXButton btnDone;

    @FXML
    private JFXButton btnReset;

    @FXML
    private JFXButton btnCancel;

    private AddEdgeWindow window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        window = (AddEdgeWindow) App.getPrimaryStage().getUserData();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()) {
            case "btnStart":
                window.setStart();
                break;
            case "btnEnd":
                window.setEnd();
                break;
            case "btnDone":
                window.addEdge();

            case "btnReset":
                window.resetEdges();
                break;
            case "btnCancel":
                window.hide();
                break;
        }
    }
}
