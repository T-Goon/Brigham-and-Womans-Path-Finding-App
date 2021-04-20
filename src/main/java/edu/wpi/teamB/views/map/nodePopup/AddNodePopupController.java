package edu.wpi.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.Node;
import edu.wpi.teamB.entities.map.GraphicalEditorNodeData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public class AddNodePopupController implements Initializable {

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
    private JFXComboBox<String> nodeType;

    @FXML
    private JFXTextField longName;

    @FXML
    private JFXTextField shortName;

    @FXML
    private JFXButton btnAddNode;

    private GraphicalEditorNodeData data;

    private Map<String, String> categoryNameMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (GraphicalEditorNodeData) App.getPrimaryStage().getUserData();

        categoryNameMap = new HashMap<>();

        //Add better category names to a hash map
        categoryNameMap.put("SERV", "Services");
        categoryNameMap.put("REST", "Restrooms");
        categoryNameMap.put("LABS", "Lab Rooms");
        categoryNameMap.put("ELEV", "Elevators");
        categoryNameMap.put("DEPT", "Departments");
        categoryNameMap.put("CONF", "Conference Rooms");
        categoryNameMap.put("INFO", "Information Locations");
        categoryNameMap.put("RETL", "Retail Locations");
        categoryNameMap.put("BATH", "Bathrooms");
        categoryNameMap.put("EXIT", "Entrances");
        categoryNameMap.put("STAI", "Stairs");
        categoryNameMap.put("PARK", "Parking Spots");
        categoryNameMap.put("HALL", "Hallway");
        categoryNameMap.put("WALK", "Sidewalk");

        List<String> temp = new ArrayList<>(categoryNameMap.values());
        Collections.sort(temp);
        nodeType.getItems().addAll(temp);

        // Fill in current coordinates
        xCoord.setText(Long.toString(Math.round(data.getX())));
        yCoord.setText(Long.toString(Math.round(data.getY())));

        // Fill in current floor
        floor.setText(data.getFloor());
        floor.setDisable(true);
    }

    /**
     * Check if input is valid
     *
     * @throws NumberFormatException when floor, xCoord, or yCoord is not a number.
     */
    @FXML
    private void validateButton() throws NumberFormatException {
        btnAddNode.setDisable(nodeID.getText().trim().isEmpty() || building.getText().trim().isEmpty() || (nodeType.getValue() == null || nodeType.getValue().trim().isEmpty())
                || longName.getText().trim().isEmpty() || shortName.getText().trim().isEmpty() || floor.getText().trim().isEmpty()
                || xCoord.getText().trim().isEmpty() || yCoord.getText().trim().isEmpty());
        try {
            Integer.parseInt(xCoord.getText().trim());
            Integer.parseInt(yCoord.getText().trim());
        } catch (NumberFormatException notInt) {
            btnAddNode.setDisable(true);
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnCancel":
                data.getNodeHolder().getChildren().remove(root);
                break;
            case "btnAddNode":
                String aNodeId = nodeID.getText().trim();
                String aFloor = floor.getText().trim();
                String aBuilding = building.getText().trim();
                String aNodeType = nodeType.getValue().trim();
                String actualNodeName = "ERROR!";
                for (String s : categoryNameMap.keySet()) {
                    if (categoryNameMap.get(s).equals(aNodeType)) {
                        actualNodeName = s;
                        break;
                    }
                }
                String aLongName = longName.getText().trim();
                String aShortName = shortName.getText().trim();
                int aXCoord = Integer.parseInt(xCoord.getText().trim());
                int aYCoord = Integer.parseInt(yCoord.getText().trim());
                Node aNode = new Node(aNodeId, aXCoord, aYCoord, aFloor, aBuilding, actualNodeName, aLongName, aShortName);
                DatabaseHandler.getDatabaseHandler("main.db").addNode(aNode);

                // Refresh map editor
                data.getPfmc().refreshEditor();

                // Remove popup from map
                data.getNodeHolder().getChildren().remove(root);
                break;
        }
    }
}
