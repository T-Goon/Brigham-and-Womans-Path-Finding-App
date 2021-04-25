package edu.wpi.cs3733.D21.teamB.views.map.edgePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.map.edge.DelEdgeAYSWindow;
import edu.wpi.cs3733.D21.teamB.entities.map.edge.DelEdgePopup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class DelEdgePopupController implements Initializable {
    @FXML
    @Getter
    private VBox root;

    @FXML
    private VBox mainMenu;

    @FXML
    private Text startName;

    @FXML
    private Text endName;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnCancel;

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
            case "btnDelete":
                root.getChildren().remove(mainMenu);

                DelEdgeAYSWindow ays = new DelEdgeAYSWindow(
                        root, popup.getData(), mainMenu);

                // Pass data to next window
                App.getPrimaryStage().setUserData(ays);

                // Load window
                try{
                    FXMLLoader.load(Objects.requireNonNull(
                            getClass().getClassLoader().getResource("edu/wpi/cs3733/D21/teamB/views/map/edgePopup/delEdgeAreYouSure.fxml")));
                } catch (IOException e){
                    e.printStackTrace();
                }

                ays.show();

                break;
            case "btnCancel":
                popup.getData().getMd().removeAllPopups();
                break;
        }
    }

}
