package edu.wpi.cs3733.D21.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.NodeType;
import edu.wpi.cs3733.D21.teamB.entities.map.node.AddNodePopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
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

    private AddNodePopup popup;

    private Map<String, String> categoryNameMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (AddNodePopup) App.getPrimaryStage().getUserData();

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
        categoryNameMap.put("EXIT", "Entrances");
        categoryNameMap.put("STAI", "Stairs");
        categoryNameMap.put("PARK", "Parking Spots");
        categoryNameMap.put("HALL", "Hallway");
        categoryNameMap.put("WALK", "Sidewalk");

        List<String> temp = new ArrayList<>(categoryNameMap.values());
        Collections.sort(temp);
        nodeType.getItems().addAll(temp);

        // Fill in current coordinates
        xCoord.setText(Long.toString(Math.round(popup.getData().getX())));
        yCoord.setText(Long.toString(Math.round(popup.getData().getY())));

        // Fill in current floor
        floor.setText(popup.getData().getFloor());
        floor.setDisable(true);
    }

    /**
     * Check if input is valid
     *
     * @throws NumberFormatException when floor, xCoord, or yCoord is not a number.
     */
    @FXML
    private void validateButton() throws NumberFormatException {
        btnAddNode.setDisable(building.getText().trim().isEmpty() || (nodeType.getValue() == null || nodeType.getValue().trim().isEmpty())
                || longName.getText().trim().isEmpty() || shortName.getText().trim().isEmpty() || floor.getText().trim().isEmpty()
                || xCoord.getText().trim().isEmpty() || yCoord.getText().trim().isEmpty());
        try {
            int xCoord = Integer.parseInt(this.xCoord.getText().trim());
            int yCoord = Integer.parseInt(this.yCoord.getText().trim());
            btnAddNode.setDisable(xCoord < 0 || yCoord < 0);
        } catch (NumberFormatException notInt) {
            btnAddNode.setDisable(true);
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()) {
            case "btnCancel":
                popup.getData().getMd().removeAllPopups();
                break;
            case "btnAddNode":
                // Parse data from popup text fields
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
                String ln = longName.getText().trim();
                String sn = shortName.getText().trim();
                int x = Integer.parseInt(xCoord.getText().trim());
                int y = Integer.parseInt(yCoord.getText().trim());

                // Figure out what the index should be
                List<Node> nodes = null;

                try {
                    nodes = DatabaseHandler.getHandler().getNodesByCategory(NodeType.valueOf(t));
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                List<Integer> indexes = new ArrayList<>();

                assert nodes != null;
                nodes.forEach(node -> {
                    if (node.getNodeID().startsWith("b"))
                        indexes.add(
                                Integer.parseInt(
                                        node.getNodeID().substring(5, 8)));
                });

                Collections.sort(indexes);
                int index = 1;

                for (Integer i : indexes)
                    if (i != index++) break;

                String id = "b" + t + String.format("%3s", index).replace(' ', '0') +
                        String.format("%2s", f).replace(' ', '0');

                // Add node to database
                popup.addNode(id, x, y, f, b, t, ln, sn);
                break;
        }
    }
}
