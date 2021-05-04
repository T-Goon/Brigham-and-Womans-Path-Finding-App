package edu.wpi.cs3733.D21.teamB.views.map.nodePopup;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.NodeType;
import edu.wpi.cs3733.D21.teamB.entities.map.node.AddNodePopup;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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

    @FXML
    private JFXColorPicker clrPicker;

    private AddNodePopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (AddNodePopup) App.getPrimaryStage().getUserData();

        List<String> temp = new ArrayList<>(popup.getData().getMc().getCategoryNameMap().values());
        Collections.sort(temp);
        nodeType.getItems().addAll(temp);

        // Fill in current coordinates
        xCoord.setText(Long.toString(Math.round(popup.getData().getX())));
        yCoord.setText(Long.toString(Math.round(popup.getData().getY())));

        // Fill in current floor
        floor.setText(popup.getData().getMc().getCurrentFloor());
        floor.setDisable(true);

        clrPicker.setValue(Color.web("012D5A"));
    }

    /**
     * Check if input is valid
     *
     * @throws NumberFormatException when xCoord or yCoord is not a number.
     */
    @FXML
    private void validateButton() throws NumberFormatException {
        boolean disable = building.getText().trim().isEmpty() || (nodeType.getValue() == null || nodeType.getValue().trim().isEmpty())
                || longName.getText().trim().isEmpty() || shortName.getText().trim().isEmpty() || floor.getText().trim().isEmpty()
                || xCoord.getText().trim().isEmpty() || yCoord.getText().trim().isEmpty();

        if (disable) {
            btnAddNode.setDisable(true);
            return;
        }

        try {
            int xCoord = Integer.parseInt(this.xCoord.getText().trim());
            int yCoord = Integer.parseInt(this.yCoord.getText().trim());
            btnAddNode.setDisable(xCoord < 0 || yCoord < 0 || xCoord > PathfindingMenuController.MAX_X || yCoord > PathfindingMenuController.MAX_Y);
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
                for (String s : popup.getData().getMc().getCategoryNameMap().keySet()) {
                    if (popup.getData().getMc().getCategoryNameMap().get(s).equals(aNodeType)) {
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

                Color c = clrPicker.getValue();

                // Add node to database
                popup.addNode(id, x, y, f, b, t, ln, sn, c);
                break;
        }
    }
}
