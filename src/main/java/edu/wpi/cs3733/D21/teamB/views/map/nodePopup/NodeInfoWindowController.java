package edu.wpi.cs3733.D21.teamB.views.map.nodePopup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.node.NodeInfoWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.*;

public class NodeInfoWindowController implements Initializable {
    @FXML
    private JFXButton btnBack;

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

    private NodeInfoWindow window;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        window = (NodeInfoWindow) App.getPrimaryStage().getUserData();

        // Fill in current node data
        floor.setText(window.getData().getNode().getFloor());
        building.setText(window.getData().getNode().getBuilding());
        type.setText(window.getData().getMc().getCategoryNameMap().get(window.getData().getNode().getNodeType()));
        longName.setText(window.getData().getNode().getLongName());
        shortName.setText(window.getData().getNode().getShortName());
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        JFXButton btn = (JFXButton) event.getSource();

        if ("btnBack".equals(btn.getId())) {
            window.hide();
        } else {
            throw new IllegalArgumentException("Unregistered UI element");
        }
    }
}
