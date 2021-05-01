package edu.wpi.cs3733.D21.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.map.node.GraphicalInputPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

@Getter
public class GraphicalInputController implements Initializable {

    @FXML
    private Text nodeName;

    @FXML
    private JFXButton btnStart;

    @FXML
    private JFXButton btnEnd;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnAddStop;

    @FXML
    private JFXButton btnAddFavorite;

    @FXML
    private JFXButton btnRemoveFavorite;

    @FXML
    private VBox SelectionPopUp;

    @FXML
    private VBox mainMenu;

    private GraphicalInputPopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (GraphicalInputPopup) App.getPrimaryStage().getUserData();
        nodeName.setText(popup.getData().getNodeName());

        boolean isFavorite = false;
        boolean hasParking = false;
        for (TreeItem<String> item : popup.getData().getPfmc().getFavorites().getChildren()) {
            if (popup.isFavorite(item.getValue())) {
                isFavorite = true;
            }
            if (popup.isParking(item.getValue())) {
                hasParking = true;
            }
        }

        if (!isFavorite) {
            btnRemoveFavorite.setDisable(true);
            btnAddFavorite.setDisable(false);
        } else {
            btnRemoveFavorite.setDisable(false);
            btnAddFavorite.setDisable(true);
        }

        if (DatabaseHandler.getHandler().getAuthenticationUser().getAuthenticationLevel().equals(User.AuthenticationLevel.GUEST)) {
            btnAddFavorite.setDisable(true);
            btnRemoveFavorite.setDisable(true);
        }

//        if (popup.isParking(popup.getData().getNodeName()) && hasParking) {
//            btnAddFavorite.setDisable(true);
//        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()) {
            case "btnStart":
                popup.setStart();
                break;
            case "btnAddStop":
                popup.addStop();
                break;
            case "btnEnd":
                popup.setEnd();
                break;
            case "btnAddFavorite":
                boolean hasParking = popup.addFavorite();
                if (hasParking) {
                    SelectionPopUp.getChildren().remove(mainMenu);
                    popup.getData().getMppm().createChangeParkingSpotPopup(SelectionPopUp, popup.getData(), mainMenu);
                } else {
                    btnAddFavorite.setDisable(true);
                    btnRemoveFavorite.setDisable(false);
                }
                break;
            case "btnRemoveFavorite":
//                if (popup.getData().getNodeName().contains("Park")) {
//                    SelectionPopUp.getChildren().remove(mainMenu);
//                    popup.getData().getMppm().createChangeParkingSpotPopup(SelectionPopUp, popup.getData(), mainMenu);
//                } else {
                    popup.removeFavorite();
//                }
                btnAddFavorite.setDisable(false);
                btnRemoveFavorite.setDisable(true);
                // loop through favorites to see if there is a parking spot
//                TreeView<String> treeView = popup.getData().getPfmc().getTreeLocations();
//                TreeItem<String> newParking = treeView.getSelectionModel().getSelectedItem();
                break;
            case "btnCancel":
//                btnRemoveFavorite.setDisable(false);
                popup.getData().getMd().removeAllPopups();
                break;
        }
    }
}
