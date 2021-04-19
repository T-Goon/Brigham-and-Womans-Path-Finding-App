package edu.wpi.teamB.views.map.edgePopup;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.entities.map.GraphicalEdgePopupData;
import edu.wpi.teamB.entities.map.GraphicalEditorEdgeData;
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

    private GraphicalEditorEdgeData data;

    private VBox areYouSureWindow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = (GraphicalEditorEdgeData) App.getPrimaryStage().getUserData();

        startName.setText(data.getStart().getLongName());
        endName.setText(data.getEnd().getLongName());
    }

    @FXML
    private void handleButtonAction(ActionEvent event){
        JFXButton btn = (JFXButton) event.getSource();

        switch (btn.getId()){
            case "btnDelete":
                root.getChildren().remove(mainMenu);

                // Pass data to next window
                App.getPrimaryStage().setUserData(new GraphicalEdgePopupData(data, this));

                // Load window
                try{
                    areYouSureWindow = FXMLLoader.load(Objects.requireNonNull(
                            getClass().getClassLoader().getResource("edu/wpi/teamB/views/map/edgePopup/delEdgeAreYouSure.fxml")));
                } catch (IOException e){
                    e.printStackTrace();
                }

                root.getChildren().add(areYouSureWindow);

                break;
            case "btnCancel":
                data.getNodeHolder().getChildren().remove(root);
                break;
        }
    }

    void areYouSureToMain(){
        root.getChildren().remove(areYouSureWindow);
        areYouSureWindow = null;

        root.getChildren().add(mainMenu);
    }
}
