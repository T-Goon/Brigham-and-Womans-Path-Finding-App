package edu.wpi.teamB.views.mapEditor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.util.GraphicalEditorNodeData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddNodePopupController implements Initializable{

    @FXML
    private VBox root;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXRadioButton notRestricted;

    @FXML
    private JFXRadioButton restricted;

    @FXML
    private ToggleGroup areaGroup;

    @FXML
    private JFXTextField nodeID;

    @FXML
    private JFXTextField xCoord;

    @FXML
    private JFXTextField yCoord;

    @FXML
    private JFXTextField floor;

    @FXML
    private JFXTextField building;

    @FXML
    private JFXTextField nodeType;

    @FXML
    private JFXTextField longName;

    @FXML
    private JFXTextField shortName;

    @FXML
    private JFXButton btnAddNode;

    private GraphicalEditorNodeData data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (GraphicalEditorNodeData) App.getPrimaryStage().getUserData();

        // Fill in current coordinates
        xCoord.setText(Long.toString(Math.round(data.getX())));
        yCoord.setText(Long.toString(Math.round(data.getY())));

        // Fill in current floor
        floor.setText(data.getFloor());
        floor.setDisable(true);
    }

    /**
     * Check if input is valid
     * @throws NumberFormatException when floor, xCoord, or yCoord is not a number.
     */
    @FXML
    private void validateButton() throws NumberFormatException {
        btnAddNode.setDisable(nodeID.getText().trim().isEmpty() || building.getText().trim().isEmpty() || nodeType.getText().trim().isEmpty()
                || longName.getText().trim().isEmpty() || shortName.getText().trim().isEmpty() || floor.getText().trim().isEmpty()
                || xCoord.getText().trim().isEmpty() || yCoord.getText().trim().isEmpty());
        try {
            Integer.parseInt(floor.getText().trim());
            Integer.parseInt(xCoord.getText().trim());
            Integer.parseInt(yCoord.getText().trim());
        } catch (NumberFormatException notInt) {
            btnAddNode.setDisable(true);
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnCancel":
                data.getNodeHolder().getChildren().remove(root);
                break;
            case "btnAddNode":
                String aNodeId = nodeID.getText().trim();
                String aFloor = floor.getText().trim();
                String aBuilding = building.getText().trim();
                String aNodeType = nodeType.getText().trim();
                String aLongName = longName.getText().trim();
                String aShortName = shortName.getText().trim();
                int aXCoord = Integer.parseInt(xCoord.getText().trim());
                int aYCoord = Integer.parseInt(yCoord.getText().trim());
                Node aNode = new Node(aNodeId, aXCoord, aYCoord, aFloor, aBuilding, aNodeType, aLongName, aShortName);
                DatabaseHandler.getDatabaseHandler("main.db").addNode(aNode);

                // Refresh map editor
                data.getPfmc().refreshEditor();

                // Remove popup from map
                data.getNodeHolder().getChildren().remove(root);
                break;
        }
    }
}
