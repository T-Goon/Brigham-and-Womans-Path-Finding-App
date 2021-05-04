package edu.wpi.cs3733.D21.teamB.views.map.edgePopup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.entities.map.data.NodeType;
import edu.wpi.cs3733.D21.teamB.entities.map.edge.AddNodeFromEdgeWindow;
import edu.wpi.cs3733.D21.teamB.views.map.PathfindingMenuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Line;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;


public class AddNodeFromEdgeController implements Initializable {

    @FXML
    private JFXTextField floor, xCoord, yCoord, building, longName, shortName;

    @FXML
    private JFXComboBox<String> nodeType;

    @FXML
    private JFXRadioButton notRestricted, restricted;

    @FXML
    private ToggleGroup areaGroup;

    @FXML
    private JFXButton btnAddNode, btnCancel;

    private AddNodeFromEdgeWindow window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.window = (AddNodeFromEdgeWindow) App.getPrimaryStage().getUserData();

        // Fill in the category combobox values
        List<String> temp = new ArrayList<>(window.getData().getMc().getCategoryNameMap().values());
        Collections.sort(temp);
        nodeType.getItems().addAll(temp);

        // Covert line coordinates from the map to pixel coordinates
        Line line = window.getData().getLine();
        double sx = line.getStartX() * PathfindingMenuController.COORDINATE_SCALE;
        double sy = line.getStartY() * PathfindingMenuController.COORDINATE_SCALE;
        double ex = line.getEndX() * PathfindingMenuController.COORDINATE_SCALE;
        double ey = line.getEndY() * PathfindingMenuController.COORDINATE_SCALE;

        double slope =
                 (ey - sy) /
                 (ex - sx);
        if (Double.isNaN(slope)) slope = Double.MAX_VALUE;
        // b = y - mx
        double yIntercept =
                ((ey + sy)/2) -
                        (slope * ((ex + sx)/2));

        double perpendicularSlope = 0;

        // Closest point on the center of the line to where the mouse clicked
        if (slope != 0) perpendicularSlope = -1 / slope;
        double perpYIntercept = window.getData().getY() - (window.getData().getX() * perpendicularSlope);
        double endX = window.getData().getX();
        if (slope != 0) endX = (perpYIntercept - yIntercept) / (slope - perpendicularSlope);
        double endY = slope * endX + yIntercept;

        // Fill in current coordinates
        xCoord.setText(Long.toString(Math.round(endX)));
        xCoord.setDisable(true);
        yCoord.setText(Long.toString(Math.round(endY)));
        yCoord.setDisable(true);

        // Fill in current floor
        floor.setText(window.getData().getMc().getCurrentFloor());
        floor.setDisable(true);
    }

    @FXML
    private void handleButtonAction(ActionEvent event){
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()){
            case "btnAddNode":
                // Parse data from popup text fields
                String f = floor.getText().trim();
                String b = building.getText().trim();
                String aNodeType = nodeType.getValue().trim();
                String t = "ERROR!";
                for (String s : window.getData().getMc().getCategoryNameMap().keySet()) {
                    if (window.getData().getMc().getCategoryNameMap().get(s).equals(aNodeType)) {
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
                window.addNode(id, x, y, f, b, t, ln, sn);
                break;
            case "btnCancel":
                window.hide();
                break;
        }
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

}
