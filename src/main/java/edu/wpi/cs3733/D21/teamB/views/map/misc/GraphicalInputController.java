package edu.wpi.cs3733.D21.teamB.views.map.misc;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.node.GraphicalInputPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
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

    private GraphicalInputPopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (GraphicalInputPopup) App.getPrimaryStage().getUserData();
        nodeName.setText(popup.getData().getNodeName());

        boolean isFavorite = false;
        for (TreeItem<String> item : popup.getData().getPfmc().getFavorites().getChildren()) {
            if (popup.isFavorite(item.getValue())) {
                isFavorite = true;
                break;
            }
        }

        if (!isFavorite) {
            btnRemoveFavorite.setDisable(true);
            btnAddFavorite.setDisable(false);
        } else {
            btnRemoveFavorite.setDisable(false);
            btnAddFavorite.setDisable(true);
        }
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
                popup.addFavorite();
                btnAddFavorite.setDisable(true);
                btnRemoveFavorite.setDisable(false);
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
}
