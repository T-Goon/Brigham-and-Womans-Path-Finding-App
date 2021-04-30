package edu.wpi.cs3733.D21.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.node.GraphicalInputPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;

import javax.xml.crypto.Data;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChangeParkingSpotController implements Initializable {

    @FXML
    private Text parkingName;

    @FXML
    private JFXButton btnChangeParking;

    @FXML
    private JFXButton btnCancel;

    private GraphicalInputPopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (GraphicalInputPopup) App.getPrimaryStage().getUserData();
        parkingName.setText(popup.getData().getNodeName());
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()) {
            case "btnChangeParking":
                TreeView<String> treeView = popup.getData().getPfmc().getTreeLocations();
                TreeItem<String> newParking = treeView.getSelectionModel().getSelectedItem();
                try {
                    DatabaseHandler.getHandler().updateFavoriteLocation(newParking.getValue());
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                break;
            case "btnCancel":
                break;
        }
    }
}
