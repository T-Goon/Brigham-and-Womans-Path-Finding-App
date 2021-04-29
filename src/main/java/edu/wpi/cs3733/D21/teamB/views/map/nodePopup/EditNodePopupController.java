package edu.wpi.cs3733.D21.teamB.views.map.nodePopup;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.node.EditNodeWindow;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

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
    private JFXColorPicker clrPicker;

    private EditNodeWindow window;

    private Map<String, String> categoryNameMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        window = (EditNodeWindow) App.getPrimaryStage().getUserData();

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
        xCoord.setText(String.valueOf(Math.round(window.getData().getX())));
        yCoord.setText(String.valueOf(Math.round(window.getData().getY())));
        floor.setText(window.getData().getFloor());
        floor.setDisable(true);
        building.setText(window.getData().getBuilding());
        nodeType.getSelectionModel().select(categoryNameMap.get(window.getData().getNodeType()));
        longName.setText(window.getData().getLongName());
        shortName.setText(window.getData().getShortName());
        clrPicker.setValue(window.getData().getColor());
    }

    /**
     * Make sure input is valid
     *
     * @throws NumberFormatException when xCoord or yCoord is not a number
     */
    @FXML
    private void validateButton() throws NumberFormatException {
        boolean disable = building.getText().trim().isEmpty() || (nodeType.getValue() == null || nodeType.getValue().trim().isEmpty())
                || longName.getText().trim().isEmpty() || shortName.getText().trim().isEmpty() || floor.getText().trim().isEmpty()
                || xCoord.getText().trim().isEmpty() || yCoord.getText().trim().isEmpty();

        if (disable) {
            btnUpdate.setDisable(true);
            return;
        }

        try {
            int xCoord = Integer.parseInt(this.xCoord.getText().trim());
            int yCoord = Integer.parseInt(this.yCoord.getText().trim());
            btnUpdate.setDisable(xCoord < 0 || yCoord < 0 || xCoord > PathfindingMenuController.MAX_X || yCoord > PathfindingMenuController.MAX_Y);
        } catch (NumberFormatException notInt) {
            btnUpdate.setDisable(true);
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()) {
            case "btnUpdate":

                int x = Integer.parseInt(xCoord.getText().trim());
                int y = Integer.parseInt(yCoord.getText().trim());
                String f = floor.getText().trim();
                String b = building.getText().trim();
                String aNodeType = nodeType.getValue().trim();
                String t = "ERROR!";
                for (String s : categoryNameMap.keySet()) {
                    if (categoryNameMap.get(s).equals(aNodeType)) {
                        t = s;
                        break;
                    }
                }
                String l = longName.getText().trim();
                String s = shortName.getText().trim();
                Color c = clrPicker.getValue();

                window.updateNode(x, y, f, b, t, l, s, c);
                window.getData().getMd().removeAllPopups();

                break;
            case "btnCancel":
                if (window.getData().isFromTree())
                    window.getData().getMd().removeAllPopups();
                window.hide();
                break;
        }
    }


}
