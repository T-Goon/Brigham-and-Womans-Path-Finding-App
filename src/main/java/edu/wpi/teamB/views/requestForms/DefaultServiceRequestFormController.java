package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.App;
import edu.wpi.teamB.util.HelpPopupController;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
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

    private VBox helpPopup;
    private double x=0;
    private double y=0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.getPrimaryStage().getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Bounds helpButtonBounds = btnHelp.localToScene(btnHelp.getBoundsInLocal());
                if (!(helpButtonBounds.getMinX()<event.getX() && event.getX()<helpButtonBounds.getMaxX() &&
                        helpButtonBounds.getMinY()<event.getY() && event.getY()<helpButtonBounds.getMaxY())) {
                    basePane.getChildren().remove(helpPopup);
                    helpPopup = null;
                }
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
                basePane.getChildren().remove(helpPopup);
                helpPopup = null;
                try{
                    helpPopup = FXMLLoader.load(Objects.requireNonNull(
                            getClass().getClassLoader().getResource("edu/wpi/teamB/views/requestForms/helpPopup.fxml")));
                } catch (IOException e){ e.printStackTrace(); }

                Bounds boundsInScene = btn.localToScene(btn.getBoundsInLocal());
                x= boundsInScene.getMinX()-375;
                y= boundsInScene.getMinY()+65;
                helpPopup.setLayoutX(x);
                helpPopup.setLayoutY(y);
                basePane.getChildren().add(helpPopup);
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
