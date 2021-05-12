package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.map.data.Node;
import edu.wpi.cs3733.D21.teamB.util.HelpDialog;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.util.AutoCompleteComboBoxListener;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

public abstract class DefaultServiceRequestFormController extends BasePageController implements Initializable {

    @FXML
    protected JFXButton btnSubmit;

    @FXML
    private JFXButton btnHelp;

    @FXML
    private AnchorPane basePane;

    @FXML
    private HBox helpHolder;

    @FXML
    private StackPane stackPane;

    @FXML
    protected JFXComboBox<String> loc;

    private VBox helpPopup;
    private boolean justClicked = false;
    protected final ArrayList<Node> nodesList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);
        App.getPrimaryStage().getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (helpPopup != null && !justClicked) {
                helpHolder.getChildren().remove(helpPopup);
                helpPopup = null;
            }
            justClicked = false;
        });

        List<Node> nodes = new ArrayList<>(DatabaseHandler.getHandler().getNodes().values());

        nodes.sort(Comparator.comparing(Node::getLongName));

        for (Node n : nodes) {
            loc.getItems().add(n.getLongName());
            nodesList.add(n);
        }

        //implement searchable combo box
        loc.setVisibleRowCount(5);
        new AutoCompleteComboBoxListener<>(loc);

        btnSubmit.setDisable(true);
    }

    protected void getLocationIndex(String nodeID) {
        for (int i = 0; i < nodesList.size(); i++) {
            if (nodesList.get(i).getNodeID().equals(nodeID)) {
                loc.getSelectionModel().select(i);
                return;
            }
        }
    }

    protected String getLocation() {
        if(nodesList.stream().anyMatch(node -> node.getLongName().equals(loc.getEditor().getText()))){
            return nodesList.stream().filter(node -> node.getLongName().equals(loc.getEditor().getText())).findFirst().get().getNodeID();
        }
        else{
            return null;
        }
    }

    public void handleButtonAction(ActionEvent e) {
        super.handleButtonAction(e);
        JFXButton btn = (JFXButton) e.getSource();
        switch (btn.getId()) {
            case "btnSubmit":
                SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/requestForms/formSubmitted.fxml");
                break;
            case "btnHelp":
                HelpDialog.loadHelpDialog(stackPane, "Please fill out this form completely. Once each field is full, you can submit the form and an employee will be assigned.\nIf you wish to end your request early, click 'Cancel'. If your request is an emergency please click 'Emergency'.");
                break;
        }
    }

    /**
     * Validation for common stuff on all request forms
     * @return true if invalid
     */
    protected boolean validateCommon(){
        try {
            getLocation();
            return false;
        } catch (IndexOutOfBoundsException e){
            return true;
        }
    }
}
