package edu.wpi.cs3733.D21.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.node.NodeInfoWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.*;

public class NodeInfoWindowController implements Initializable {
    @FXML
    private JFXButton btnBack;

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
    private JFXTextField type;

    @FXML
    private JFXTextField longName;

    @FXML
    private JFXTextField shortName;

    @FXML
    private JFXColorPicker clrPicker;

    private NodeInfoWindow window;

    private Map<String, String> categoryNameMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        window = (NodeInfoWindow) App.getPrimaryStage().getUserData();

        // Fill in current node data
        xCoord.setText(String.valueOf(Math.round(window.getData().getX())));
        yCoord.setText(String.valueOf(Math.round(window.getData().getY())));
        floor.setText(window.getData().getFloor());
        building.setText(window.getData().getBuilding());
        type.setText(window.getData().getMc().getCategoryNameMap().get(window.getData().getNodeType()));
        longName.setText(window.getData().getLongName());
        shortName.setText(window.getData().getShortName());
        clrPicker.setValue(window.getData().getColor());
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()){
            case "btnBack":
                window.hide();
                break;
            default:
                throw new IllegalArgumentException("Unregistered UI element");
        }
    }
}
