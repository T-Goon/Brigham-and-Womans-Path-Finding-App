package edu.wpi.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.map.GraphicalEditorNodeData;
import edu.wpi.teamB.entities.map.GraphicalNodePopupData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

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

    private GraphicalEditorNodeData data;

    private VBox areYouSureMenu;

    private VBox nodeEditMenu;

    private VBox addEdgeMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (GraphicalEditorNodeData) App.getPrimaryStage().getUserData();

        nodeName.setText(data.getLongName());

        // If coming from the tree view, go
        if (data.getCircle() == null) {
            root.getChildren().remove(mainMenu);

            // Pass data to new window
            App.getPrimaryStage().setUserData(new GraphicalNodePopupData(data, this));

            // Load window
            try {
                nodeEditMenu = FXMLLoader.load(Objects.requireNonNull(
                        getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/editNodePopup.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            root.getChildren().add(nodeEditMenu);
        }

    }

    @FXML
    private void handleButtonAction(ActionEvent event) {

        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()) {
            case "btnEditNode":
                root.getChildren().remove(mainMenu);

                // Pass data to new window
                App.getPrimaryStage().setUserData(new GraphicalNodePopupData(data, this));

                // Load window
                try {
                    nodeEditMenu = FXMLLoader.load(Objects.requireNonNull(
                            getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/editNodePopup.fxml")));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                root.getChildren().add(nodeEditMenu);
                break;
            case "btnAddEdge":
                root.getChildren().remove(mainMenu);

                // Pass data to window
                App.getPrimaryStage().setUserData(new GraphicalNodePopupData(data, this));

                // Load window
                try {
                    addEdgeMenu = FXMLLoader.load(Objects.requireNonNull(
                            getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/nodePopup/addEdgePopup.fxml")));
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
                data.getNodeHolder().getChildren().remove(root);
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
