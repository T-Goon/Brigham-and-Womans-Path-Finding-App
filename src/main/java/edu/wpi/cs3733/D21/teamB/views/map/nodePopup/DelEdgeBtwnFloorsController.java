package edu.wpi.cs3733.D21.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.data.DelEdgeBtwnFloorsWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DelEdgeBtwnFloorsController implements Initializable {

    @FXML
    private JFXButton btnDelete,
                    btnCancel;

    @FXML
    private JFXComboBox<String> nodeName;

    private DelEdgeBtwnFloorsWindow window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        window = (DelEdgeBtwnFloorsWindow) App.getPrimaryStage().getUserData();

        // Populate combobox
        nodeName.getItems().addAll(window.getEdgesBtwnFloors());
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()){
            case "btnDelete":
                window.deleteEdge(nodeName.getSelectionModel().getSelectedIndex());
                break;
            case "btnCancel":
                window.hide();
                break;
        }
    }
}
