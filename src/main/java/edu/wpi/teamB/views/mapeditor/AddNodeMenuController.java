package edu.wpi.teamB.views.mapeditor;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddNodeMenuController implements Initializable {

    // NODES

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnAddNode;

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


    // EDGES

    @FXML
    public JFXTextField edgeID;

    @FXML
    private JFXTextField startNode;

    @FXML
    private JFXTextField endNode;

    @FXML
    private JFXButton btnAddEdge;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notRestricted.setToggleGroup(areaGroup);
        restricted.setToggleGroup(areaGroup);
    }

    @FXML
    private void handleButtonAction(ActionEvent e) throws IOException {
        JFXButton btn = (JFXButton) e.getSource();

        switch (btn.getId()) {
            case "btnBack":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/nodesEditorMenu.fxml");
                break;

            case "btnAddNode":
                String aNodeId = nodeID.getText();
                int aFloor = Integer.parseInt(floor.getText());
                String aBuilding = building.getText();
                String aNodeType = nodeType.getText();
                String aLongName = longName.getText();
                String aShortName = shortName.getText();
                int aXCoord = Integer.parseInt(xCoord.getText());
                int aYCoord = Integer.parseInt(yCoord.getText());
                Node aNode = new Node(aNodeId, aXCoord, aYCoord, aFloor, aBuilding, aNodeType, aLongName, aShortName);
                DatabaseHandler.getDatabaseHandler("main.db").addNode(aNode);
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/mapeditor/nodesEditorMenu.fxml");
                break;
        }
    }


}
