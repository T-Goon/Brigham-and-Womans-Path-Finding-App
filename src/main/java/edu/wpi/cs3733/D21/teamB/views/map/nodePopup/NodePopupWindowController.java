package edu.wpi.cs3733.D21.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.data.DelEdgeBtwnFloorsWindow;
import edu.wpi.cs3733.D21.teamB.entities.map.node.DelNodeAYSWindow;
import edu.wpi.cs3733.D21.teamB.entities.map.node.EditNodeWindow;
import edu.wpi.cs3733.D21.teamB.entities.map.node.NodeInfoWindow;
import edu.wpi.cs3733.D21.teamB.entities.map.node.NodeMenuPopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

public class NodePopupWindowController implements Initializable {

    @FXML
    @Getter
    private VBox root;

    @FXML
    private VBox mainMenu;

    @FXML
    private Text nodeName;

    @FXML
    private JFXButton btnEditNode,
            btnAddEdge,
            btnDelete,
            btnCancel,
            btnDelEdgeBtwnFloor,
            btnInfo;

    private NodeMenuPopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (NodeMenuPopup) App.getPrimaryStage().getUserData();

        nodeName.setText(popup.getData().getLongName());

        // If coming from the tree view, automatically show edit menu
        if (popup.getData().isFromTree()) {
            root.getChildren().remove(mainMenu);

            EditNodeWindow enWindow = new EditNodeWindow(root, popup.getData(), null);

            // Pass data to new window
            App.getPrimaryStage().setUserData(enWindow);

            enWindow.show();
        }

    }

    @FXML
    private void handleButtonAction(ActionEvent event) {

        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()) {
            case "btnEditNode":
                root.getChildren().remove(mainMenu);

                EditNodeWindow enWindow = new EditNodeWindow(root, popup.getData(), mainMenu);

                // Pass data to the controller
                App.getPrimaryStage().setUserData(enWindow);

                enWindow.show();
                break;
            case "btnAddEdge":
                popup.setStartEdge();
                break;
            case "btnDelEdgeBtwnFloor":
                root.getChildren().remove(mainMenu);

                DelEdgeBtwnFloorsWindow delEdgeBtwnFloorsWindow = new DelEdgeBtwnFloorsWindow(root, popup.getData(), mainMenu);

                // Pass data to the controller
                App.getPrimaryStage().setUserData(delEdgeBtwnFloorsWindow);

                delEdgeBtwnFloorsWindow.show();
                break;
            case "btnDelete":
                root.getChildren().remove(mainMenu);

                DelNodeAYSWindow dnWindow = new DelNodeAYSWindow(root, popup.getData(), mainMenu);

                // Pass data to the controller
                App.getPrimaryStage().setUserData(dnWindow);

                dnWindow.show();
                break;
            case "btnCancel":
                popup.getData().getMd().removeAllPopups();
                break;
        }
    }
}
