package edu.wpi.teamB.views.map.edgePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.map.edge.DelEdgeAYSWindow;
import edu.wpi.teamB.entities.map.edge.DelEdgePopup;
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

    private DelEdgePopup data;

    private DelEdgeAYSWindow ays;

    private VBox areYouSureWindow;

    private DelEdgeAYSWindow delWindow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (DelEdgePopup) App.getPrimaryStage().getUserData();

        startName.setText(data.getData().getStart().getLongName());
        endName.setText(data.getData().getEnd().getLongName());
    }

    @FXML
    private void handleButtonAction(ActionEvent event){
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()){
            case "btnDelete":
                root.getChildren().remove(mainMenu);

                ays = new DelEdgeAYSWindow(
                        root, data.getData(), mainMenu);

                // Pass data to next window
                App.getPrimaryStage().setUserData(ays);

                // Load window
                try{
                    areYouSureWindow = FXMLLoader.load(Objects.requireNonNull(
                            getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/edgePopup/delEdgeAreYouSure.fxml")));
                } catch (IOException e){
                    e.printStackTrace();
                }

                ays.show();

                break;
            case "btnCancel":
                data.hide();
                break;
        }
    }

}
