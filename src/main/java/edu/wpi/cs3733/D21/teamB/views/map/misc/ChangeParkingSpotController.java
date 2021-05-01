package edu.wpi.cs3733.D21.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.node.ChangeParkingSpotPopup;
import edu.wpi.cs3733.D21.teamB.entities.map.node.GraphicalInputPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChangeParkingSpotController implements Initializable {

    @FXML
    private Text parkingName;

    @FXML
    private JFXButton btnChangeParking;

    @FXML
    private JFXButton btnRemoveParking;

    @FXML
    private JFXButton btnCancel;

//    @FXML
//    private JFXButton btnAddFavorite;
//
//    @FXML
//    private JFXButton btnRemoveFavorite;

    private ChangeParkingSpotPopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (ChangeParkingSpotPopup) App.getPrimaryStage().getUserData();
        parkingName.setText(popup.getData().getNodeName());
//        FXMLLoader loader = new FXMLLoader();
//        GraphicalInputController graphicalInputController = (GraphicalInputController) loader.getController();
//        btnAddFavorite = graphicalInputController.getBtnAddFavorite();
//        btnRemoveFavorite = graphicalInputController.getBtnRemoveFavorite();
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()) {
            case "btnChangeParking":
                TreeView<String> treeView = popup.getData().getPfmc().getTreeLocations();
                TreeItem<String> newParking = treeView.getSelectionModel().getSelectedItem();
//                popup.getData().getNodeName();
                try {
                    DatabaseHandler.getHandler().updateFavoriteLocation(newParking.getValue());
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                break;
            case "btnRemoveParking":
                // Get tree item
                TreeItem<String> favorites = popup.getData().getPfmc().getFavorites();
                List<TreeItem<String>> toRemove = new ArrayList<>();

                // Find the location to remove
                for (TreeItem<String> item : favorites.getChildren()) {
                    if (item.getValue().equals(popup.getData().getNodeName())) {
                        toRemove.add(item);
                        try {
                            DatabaseHandler.getHandler().removeFavoriteLocation(item.getValue());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                favorites.getChildren().removeAll(toRemove);
//                btnAddFavorite.setDisable(false);
//                btnRemoveFavorite.setDisable(true);
                popup.hide();
                break;
            case "btnCancel":
                popup.hide();
                break;
        }
    }
}
