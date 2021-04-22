package edu.wpi.teamB.entities.map;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.entities.map.data.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

public class MapPathPopupManager {

    private MapDrawer md;

    public MapPathPopupManager(MapDrawer md) {
        this.md = md;
    }

    /**
     * Creates the popup for the graphical input.
     *
     * @param n Node to create the popup for
     */
    public void createGraphicalInputPopup(Node n) {

        try {
            // Load fxml
            final VBox locInput = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/edu/wpi/teamB/views/map/misc/graphicalInput.fxml")));

            // Set up popup buttons
            for (javafx.scene.Node node : ((VBox) locInput.getChildren().get(0)).getChildren()) {
                javafx.scene.Node child = ((HBox) node).getChildren().get(0);
                switch (child.getId()) {
                    case "nodeName":
                        ((Text)child).setText(n.getLongName());
                        break;
                    case "btnStart":
                        showGraphicalSelection(txtStartLocation, child, n);
                        break;
                    case "btnEnd":
                        showGraphicalSelection(txtEndLocation, child, n);
                        break;
                    case "btnCancel":
                        ((JFXButton)child).setOnAction(event -> removeAllPopups());
                        break;
                }
            }

            if (selectionBox != null) {
                removeAllPopups();
            }

            selectionBox = locInput;
            placePopupOnMap(locInput);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * Shows the popup for the graphical input.
     *
     * @param textField TextField to set text for
     * @param node      javafx node that will show popup when clicked
     * @param n         map node the popup is for
     */
    private void showGraphicalSelection(JFXTextField textField, javafx.scene.Node node, Node n) {
        JFXButton tempButton = (JFXButton) node;

        tempButton.setOnAction(event -> {
            textField.setText(n.getLongName());
            md.removeAllPopups();
            validateFindPathButton();
        });

    }

    public void removeAllPopups(){

    }
}
