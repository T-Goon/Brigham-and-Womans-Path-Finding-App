package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public abstract class DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXButton btnSubmit;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton btnHelp;

    @FXML
    private JFXButton btnEmergency;

    @FXML
    private AnchorPane basePane;

    @FXML
    private HBox helpHolder;

    private VBox helpPopup;
    private double x=0;
    private double y=0;
    private boolean justClicked = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.getPrimaryStage().getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Bounds helpButtonBounds = btnHelp.localToScene(btnHelp.getBoundsInLocal());
                if (helpPopup != null && !justClicked) {
                    helpHolder.getChildren().remove(helpPopup);
                    helpPopup = null;
                }

                justClicked = false;
            }
        });
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch (btn.getId()) {
            case "btnSubmit":
                SceneSwitcher.switchToTemp(getClass(), "/edu/wpi/teamB/views/requestForms/formSubmitted.fxml");
                break;
            case "btnCancel":
                SceneSwitcher.goBack(getClass(), 1);
                break;
            case "btnHelp":

                if(helpHolder != null) {
                    helpHolder.getChildren().remove(helpPopup);
                    helpPopup = null;
                }

                try{
                    helpPopup = FXMLLoader.load(Objects.requireNonNull(
                            getClass().getClassLoader().getResource("edu/wpi/teamB/views/requestForms/helpPopup.fxml")));
                } catch (IOException e){ e.printStackTrace(); }

                helpHolder.getChildren().add(helpPopup);
                justClicked = true;
                break;

            case "btnExit":
                Platform.exit();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml", "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }
}
