package edu.wpi.teamB.views.mapEditor.nodes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.map.Node;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditNodeMenuController implements Initializable {

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnBack;

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
    private JFXTextField nodeType;

    @FXML
    private JFXTextField longName;

    @FXML
    private JFXTextField shortName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage stage = App.getPrimaryStage();
        Node node = (Node) stage.getUserData();

        nodeID.setText(node.getNodeID());
        nodeID.setDisable(true);
        xCoord.setText(String.valueOf(node.getXCoord()));
        yCoord.setText(String.valueOf(node.getYCoord()));
        floor.setText(String.valueOf(node.getFloor()));
        building.setText(node.getBuilding());
        nodeType.setText(node.getNodeType());
        longName.setText(node.getLongName());
        shortName.setText(node.getShortName());
    }

    @FXML
    private void validateButton() throws NumberFormatException {
        btnUpdate.setDisable(nodeID.getText().trim().isEmpty() || building.getText().trim().isEmpty() || nodeType.getText().trim().isEmpty()
                || longName.getText().trim().isEmpty() || shortName.getText().trim().isEmpty() || floor.getText().trim().isEmpty()
                || xCoord.getText().trim().isEmpty() || yCoord.getText().trim().isEmpty());
        try {
            Integer.parseInt(floor.getText().trim());
            Integer.parseInt(xCoord.getText().trim());
            Integer.parseInt(yCoord.getText().trim());
        } catch (NumberFormatException notInt) {
            btnUpdate.setDisable(true);
        }}

    @FXML
    private void handleButtonAction(ActionEvent e) {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.goBack(getClass(), 1);
                break;
            case "btnExit":
                Platform.exit();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapEditor/nodes/editNodeMenu.fxml", "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
            case "btnUpdate":
                Stage stage = App.getPrimaryStage();
                Node node = (Node) stage.getUserData();

                int aXCoord = Integer.parseInt(xCoord.getText().trim());
                int aYCoord = Integer.parseInt(yCoord.getText().trim());
                String aFloor = floor.getText().trim();
                String aBuilding = building.getText().trim();
                String aNodeType = nodeType.getText().trim();
                String aLongName = longName.getText().trim();
                String aShortName = shortName.getText().trim();

                node = new Node(node.getNodeID(), aXCoord, aYCoord, aFloor, aBuilding, aNodeType, aLongName, aShortName);

                DatabaseHandler.getDatabaseHandler("main.db").updateNode(node);
                SceneSwitcher.goBack(getClass(), 1);
                break;
        }
    }
}
