package edu.wpi.cs3733.D21.teamB.views.map.edgePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.edge.AddNodeFromEdgeWindow;
import edu.wpi.cs3733.D21.teamB.entities.map.edge.DelEdgeAYSWindow;
import edu.wpi.cs3733.D21.teamB.entities.map.edge.DelEdgePopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

public class DelEdgePopupController implements Initializable {
    @FXML
    @Getter
    private VBox root, mainMenu;

    @FXML
    private Text startName, endName;

    @FXML
    private JFXButton btnDelete, btnCancel, btnAddNode;

    private DelEdgePopup popup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        popup = (DelEdgePopup) App.getPrimaryStage().getUserData();

        startName.setText(popup.getData().getStart().getLongName());
        endName.setText(popup.getData().getEnd().getLongName());
    }

    @FXML
    private void handleButtonAction(ActionEvent event){
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()){
            case "btnAddNode":
                root.getChildren().remove(mainMenu);

                AddNodeFromEdgeWindow addNodeFromEdgeWindow = new AddNodeFromEdgeWindow(
                        root, popup.getData(), mainMenu);

                // Pass data to next window
                App.getPrimaryStage().setUserData(addNodeFromEdgeWindow);

                addNodeFromEdgeWindow.show();

                break;
            case "btnDelete":
                root.getChildren().remove(mainMenu);

                DelEdgeAYSWindow ays = new DelEdgeAYSWindow(
                        root, popup.getData(), mainMenu);

                // Pass data to next window
                App.getPrimaryStage().setUserData(ays);

                ays.show();

                break;
            case "btnCancel":
                popup.getData().getMd().removeAllPopups();
                break;
        }
    }

}
