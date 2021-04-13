package edu.wpi.teamB.views.mapeditor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditNodeMenuController implements Initializable {

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
    private TextField nodeID;

    @FXML
    private TextField xCoord;

    @FXML
    private TextField yCoord;

    @FXML
    private TextField floor;

    @FXML
    private TextField building;

    @FXML
    private TextField nodeType;

    @FXML
    private TextField longName;

    @FXML
    private TextField shortName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Stage stage = App.getPrimaryStage();
        Node node = (Node) stage.getUserData();

        nodeID.setText(node.getNodeID());
        nodeID.setDisable(true);
        xCoord.setText(String.valueOf(node.getXCoord()));
        yCoord.setText(String.valueOf(node.getXCoord()));
        floor.setText(String.valueOf(node.getFloor()));
        building.setText(node.getBuilding());
        nodeType.setText(node.getNodeType());
        longName.setText(node.getLongName());
        shortName.setText(node.getShortName());
    }

    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/nodesEditorMenu.fxml");
                break;
            case "btnUpdate":
                Stage stage = App.getPrimaryStage();
                Node node = (Node) stage.getUserData();

                int aXCoord = Integer.parseInt(xCoord.getText());
                int aYCoord = Integer.parseInt(yCoord.getText());
                int aFloor = Integer.parseInt(floor.getText());
                String aBuilding = building.getText();
                String aNodeType = nodeType.getText();
                String aLongName = longName.getText();
                String aShortName = shortName.getText();

                node = new Node(node.getNodeID(), aXCoord, aYCoord, aFloor, aBuilding, aNodeType, aLongName, aShortName);

                DatabaseHandler.getDatabaseHandler("main.db").updateNode(node);
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/nodesEditorMenu.fxml");
                break;
        }
    }
}
