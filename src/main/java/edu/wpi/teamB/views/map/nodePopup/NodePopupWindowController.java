package edu.wpi.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.map.node.EditNodeWindow;
import edu.wpi.teamB.entities.map.node.NodeMenuPopup;
import edu.wpi.teamB.entities.map.data.GraphicalNodePopupData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
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

    private VBox areYouSureMenu;

    private VBox nodeEditMenu;

    private VBox addEdgeMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (NodeMenuPopup) App.getPrimaryStage().getUserData();

        nodeName.setText(popup.getData().getLongName());

        // If coming from the tree view, automatically show edit menu
        if (popup.getData().isFromTree()) {
            root.getChildren().remove(mainMenu);

            EditNodeWindow enWindow =  new EditNodeWindow(root, popup.getData(), popup);

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

                EditNodeWindow enWindow =  new EditNodeWindow(root, popup.getData(), popup);

                // Pass data to new window
                App.getPrimaryStage().setUserData(enWindow);

                enWindow.show();
                break;
            case "btnAddEdge":
                root.getChildren().remove(mainMenu);

                // Pass data to window
                App.getPrimaryStage().setUserData(null);

                root.getChildren().add(addEdgeMenu);
                break;
            case "btnDelete":
                root.getChildren().remove(mainMenu);

                // Pass data to new window
                App.getPrimaryStage().setUserData(new GraphicalNodePopupData(
                        data,
                        this));

                // Load new window
                try {
                    areYouSureMenu = FXMLLoader.load(Objects.requireNonNull(
                            getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/delNodeAreYouSure.fxml")));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                root.getChildren().add(areYouSureMenu);

                break;
            case "btnCancel":
                data.getMapStack().getChildren().remove(root);
                GesturePane thePane = (GesturePane) data.getMapStack().getChildren().get(0);
                thePane.setGestureEnabled(true);
                break;
        }
    }

    /**
     * Swaps windows from the areYouSure window to the main menu.
     */
    void areYouSureToMain() {
        root.getChildren().remove(areYouSureMenu);
        areYouSureMenu = null;

        root.getChildren().add(mainMenu);
    }

    /**
     * Swaps windows from the edit node window to the main menu.
     */
    void editToMain() {
        root.getChildren().remove(nodeEditMenu);
        nodeEditMenu = null;

        root.getChildren().add(mainMenu);
    }

    /**
     * Swaps windows from the add edge window to the main menu.
     */
    void addEdgeToMain() {
        root.getChildren().remove(addEdgeMenu);
        addEdgeMenu = null;

        root.getChildren().add(mainMenu);
    }
}
