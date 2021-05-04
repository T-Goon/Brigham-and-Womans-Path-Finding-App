package edu.wpi.cs3733.D21.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.map.node.ChangeParkingSpotPopup;
import edu.wpi.cs3733.D21.teamB.entities.map.node.GraphicalInputPopup;
import edu.wpi.cs3733.D21.teamB.entities.map.node.NodeInfoWindow;
import edu.wpi.cs3733.D21.teamB.util.IObserver;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Getter
public class GraphicalInputController implements Initializable, IObserver {

    @FXML
    private Text nodeName;

    @FXML
    private JFXButton btnStart,
            btnEnd,
            btnCancel,
            btnAddStop,
            btnAddFavorite,
            btnRemoveFavorite,
            btnInfo;


    @FXML
    private VBox root;

    @FXML
    private VBox mainMenu;

    @FXML
    private HBox favoriteBox;

    private GraphicalInputPopup popup;
    private ChangeParkingSpotPopup cpsPopup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (GraphicalInputPopup) App.getPrimaryStage().getUserData();
        nodeName.setText(popup.getData().getNode().getLongName());

        // Don't show the favorite buttons if no one is logged in
        if (!DatabaseHandler.getHandler().getAuthenticationUser().isAtLeast(User.AuthenticationLevel.PATIENT)) {
            mainMenu.getChildren().remove(favoriteBox);
        }

        boolean isFavorite = false;
        for (TreeItem<String> item : popup.getData().getPfmc().getFavorites().getChildren()) {
            if (popup.isFavorite(item.getValue())) {
                isFavorite = true;
            }
        }

        if (!isFavorite) {
            btnRemoveFavorite.setDisable(true);
            btnAddFavorite.setDisable(false);
        } else {
            btnRemoveFavorite.setDisable(false);
            btnAddFavorite.setDisable(true);
        }

        // Disable favorite buttons for guest users
        if (DatabaseHandler.getHandler().getAuthenticationUser().getAuthenticationLevel().equals(User.AuthenticationLevel.GUEST)) {
            btnAddFavorite.setDisable(true);
            btnRemoveFavorite.setDisable(true);
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()) {
            case "btnInfo":
                root.getChildren().remove(mainMenu);
                NodeInfoWindow niWindow = new NodeInfoWindow(root, popup.getData(), mainMenu);

                niWindow.show();
                break;
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
                    root.getChildren().remove(mainMenu);
                    cpsPopup = popup.getData().getMppm().createChangeParkingSpotPopup(root, popup.getData(), mainMenu);
                    cpsPopup.attach(this);
                } else {
                    btnAddFavorite.setDisable(true);
                    btnRemoveFavorite.setDisable(false);
                }
                break;
            case "btnRemoveFavorite":
                popup.removeFavorite();
                btnAddFavorite.setDisable(false);
                btnRemoveFavorite.setDisable(true);
                break;
            case "btnCancel":
                popup.getData().getMd().removeAllPopups();
                break;
        }
    }

    /**
     * Change the parking spot
     */
    @Override
    public void update() {
        // Get tree view, parking spot to add, and tree item
        TreeView<String> treeView = cpsPopup.getData().getPfmc().getTreeLocations();
        String newParking = popup.getData().getNode().getLongName();
        TreeItem<String> favorites = cpsPopup.getData().getPfmc().getFavorites();

        List<TreeItem<String>> toRemove = new ArrayList<>();

        // Find parking spot in favorites and remove it
        for (TreeItem<String> favorite : favorites.getChildren()) {
            if (favorite.getValue().contains("Park")) {
                toRemove.add(favorite);
            }
        }
        favorites.getChildren().removeAll(toRemove);

        // Add updated parking spot to favorites and update database
        favorites.getChildren().add(new TreeItem<String>(newParking));
        try {
            DatabaseHandler.getHandler().updateParkingSpot(newParking);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        btnAddFavorite.setDisable(true);
        btnRemoveFavorite.setDisable(false);
    }
}
