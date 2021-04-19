package edu.wpi.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.Node;
import edu.wpi.teamB.entities.map.GraphicalNodePopupData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.*;

public class EditNodePopupController implements Initializable {

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnUpdate;

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

    private GraphicalNodePopupData data;

    private Map<String, String> categoryNameMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (GraphicalNodePopupData) App.getPrimaryStage().getUserData();

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
        categoryNameMap.put("BATH", "Bathroom");
        categoryNameMap.put("EXIT", "Entrances");
        categoryNameMap.put("STAI", "Stairs");
        categoryNameMap.put("PARK", "Parking Spots");
        categoryNameMap.put("HALL", "Hallway");
        categoryNameMap.put("WALK", "Sidewalk");

        List<String> temp = new ArrayList<>(categoryNameMap.values());
        Collections.sort(temp);
        nodeType.getItems().addAll(temp);

        // Fill in current node data
        nodeID.setText(data.getData().getNodeID());
        nodeID.setDisable(true);
        xCoord.setText(String.valueOf(Math.round(data.getData().getX())));
        yCoord.setText(String.valueOf(Math.round(data.getData().getY())));
        floor.setText(data.getData().getFloor());
        floor.setDisable(true);
        building.setText(data.getData().getBuilding());
        nodeType.getSelectionModel().select(categoryNameMap.get(data.getData().getNodeType()));
        longName.setText(data.getData().getLongName());
        shortName.setText(data.getData().getShortName());
    }

    /**
     * Make sure input is valid
     * @throws NumberFormatException when floor, xCoord, or yCoord is not a number
     */
    @FXML
    private void validateButton() throws NumberFormatException {
        btnUpdate.setDisable(nodeID.getText().trim().isEmpty() || building.getText().trim().isEmpty() || nodeType.getValue().trim().isEmpty()
                || longName.getText().trim().isEmpty() || shortName.getText().trim().isEmpty() || floor.getText().trim().isEmpty()
                || xCoord.getText().trim().isEmpty() || yCoord.getText().trim().isEmpty());

        try {
            Integer.parseInt(xCoord.getText().trim());
            Integer.parseInt(yCoord.getText().trim());
        } catch (NumberFormatException notInt) {
            btnUpdate.setDisable(true);
        }}

    @FXML
    private void handleButtonAction(ActionEvent event){
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()){
            case "btnUpdate":

                int aXCoord = Integer.parseInt(xCoord.getText().trim());
                int aYCoord = Integer.parseInt(yCoord.getText().trim());
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

                Node node = new Node(
                        data.getData().getNodeID(),
                        aXCoord,
                        aYCoord,
                        aFloor,
                        aBuilding,
                        actualNodeName,
                        aLongName,
                        aShortName);

                // Update database and graph
                DatabaseHandler.getDatabaseHandler("main.db").updateNode(node);

                // Remove popup from map and refresh map nodes
                data.getData().getPfmc().refreshEditor();

                data.getData().getNodeHolder().getChildren().remove(data.getParent().getRoot());
                break;
            case "btnCancel":
                if (data.getData().getCircle() == null)
                    data.getData().getNodeHolder().getChildren().remove(data.getParent().getRoot());
                data.getParent().editToMain();
                break;
        }
    }
}
