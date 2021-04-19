package edu.wpi.teamB.views.mapEditor.graphical.nodePopup;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamB.App;
import edu.wpi.teamB.database.DatabaseHandler;
import edu.wpi.teamB.entities.Node;
import edu.wpi.teamB.pathfinding.Graph;
import edu.wpi.teamB.util.GraphicalNodePopupData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

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
    private JFXComboBox<Label> nodeType;

    @FXML
    private JFXTextField longName;

    @FXML
    private JFXTextField shortName;

    private GraphicalNodePopupData data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (GraphicalNodePopupData) App.getPrimaryStage().getUserData();

        // Populate the combobox
        nodeType.getItems().add(new Label("CONF"));
        nodeType.getItems().add(new Label("BATH"));
        nodeType.getItems().add(new Label("DEPT"));
        nodeType.getItems().add(new Label("ELEV"));
        nodeType.getItems().add(new Label("EXIT"));
        nodeType.getItems().add(new Label("HALL"));
        nodeType.getItems().add(new Label("INFO"));
        nodeType.getItems().add(new Label("LABS"));
        nodeType.getItems().add(new Label("PARK"));
        nodeType.getItems().add(new Label("REST"));
        nodeType.getItems().add(new Label("RETL"));
        nodeType.getItems().add(new Label("WALK"));
        nodeType.getItems().add(new Label("STAI"));
        nodeType.getItems().add(new Label("SERV"));

        // Fill in current node data
        nodeID.setText(data.getData().getNodeID());
        nodeID.setDisable(true);
        xCoord.setText(String.valueOf(Math.round(data.getData().getX())));
        yCoord.setText(String.valueOf(Math.round(data.getData().getY())));
        floor.setText(data.getData().getFloor());
        floor.setDisable(true);
        building.setText(data.getData().getBuilding());
        for(int i=0; i<nodeType.getItems().size(); i++){
            if(nodeType.getItems().get(i).getText().equals(data.getData().getNodeType()))
                nodeType.getSelectionModel().select(i);
        }
        longName.setText(data.getData().getLongName());
        shortName.setText(data.getData().getShortName());
    }

    /**
     * Make sure input is valid
     * @throws NumberFormatException when floor, xCoord, or yCoord is not a number
     */
    @FXML
    private void validateButton() throws NumberFormatException {
        btnUpdate.setDisable(nodeID.getText().trim().isEmpty() || building.getText().trim().isEmpty() || nodeType.getValue().getText().isEmpty()
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
    private void handleButtonAction(ActionEvent event){
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()){
            case "btnUpdate":

                int aXCoord = Integer.parseInt(xCoord.getText().trim());
                int aYCoord = Integer.parseInt(yCoord.getText().trim());
                String aFloor = floor.getText().trim();
                String aBuilding = building.getText().trim();
                String aNodeType = nodeType.getValue().getText();
                String aLongName = longName.getText().trim();
                String aShortName = shortName.getText().trim();

                Node node = new Node(
                        data.getData().getNodeID(),
                        aXCoord,
                        aYCoord,
                        aFloor,
                        aBuilding,
                        aNodeType,
                        aLongName,
                        aShortName);

                // Update database and graph
                DatabaseHandler.getDatabaseHandler("main.db").updateNode(node);
                Graph.getGraph().updateGraph();

                // Remove popup from map and refresh map nodes
                data.getData().getPfmc().refreshEditor();

                data.getData().getNodeHolder().getChildren().remove(data.getParent().getRoot());
                break;
            case "btnCancel":
                data.getParent().editToMain();
                break;
        }
    }
}
