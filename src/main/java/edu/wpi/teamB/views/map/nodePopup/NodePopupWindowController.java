package edu.wpi.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.map.node.AddEdgeWindow;
import edu.wpi.teamB.entities.map.node.DelNodeAYSWindow;
import edu.wpi.teamB.entities.map.node.EditNodeWindow;
import edu.wpi.teamB.entities.map.node.NodeMenuPopup;
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
    private JFXButton btnEditNode;

    @FXML
    private JFXButton btnAddEdge;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnCancel;

    private NodeMenuPopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (NodeMenuPopup) App.getPrimaryStage().getUserData();

        nodeName.setText(popup.getData().getLongName());

        // If coming from the tree view, automatically show edit menu
        if (popup.getData().isFromTree()) {
            root.getChildren().remove(mainMenu);

            EditNodeWindow enWindow =  new EditNodeWindow(root, popup.getData());

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

                EditNodeWindow enWindow =  new EditNodeWindow(root, popup.getData());

                // Pass data to new window
                App.getPrimaryStage().setUserData(enWindow);

                enWindow.show();
                break;
            case "btnAddEdge":
                root.getChildren().remove(mainMenu);

                AddEdgeWindow aeWindow = new AddEdgeWindow(root, popup.getData(), mainMenu);

                // Pass data to window
                App.getPrimaryStage().setUserData(aeWindow);

                aeWindow.show();
                break;
            case "btnDelete":
                root.getChildren().remove(mainMenu);

                DelNodeAYSWindow dnWindow = new DelNodeAYSWindow(root, popup.getData(), mainMenu);

                // Pass data to new window
                App.getPrimaryStage().setUserData(dnWindow);

                dnWindow.show();
                break;
            case "btnCancel":
                popup.hide();
                break;
        }
    }
}
