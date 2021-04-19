package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamB.util.HelpPopupController;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public abstract class DefaultServiceRequestFormController {

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
    public void clearPopup(ActionEvent actionEvent2) {
        basePane.getChildren().remove(helpPopup);
        helpPopup = null;
    }

    private void onMousePress(MouseEvent mouseEvent){
        x = mouseEvent.getX();
        y = mouseEvent.getY();
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
                try{
                    helpPopup = FXMLLoader.load(Objects.requireNonNull(
                            getClass().getClassLoader().getResource("edu/wpi/teamB/views/requestForms/helpPopup.fxml")));
                } catch (IOException e){ e.printStackTrace(); }
                x=1400;
                y=500;
                x= btn.getLayoutX();
                y=btn.getLayoutY();
                helpPopup.setLayoutX(x);
                helpPopup.setLayoutY(y);
                basePane.getChildren().add(helpPopup);/*
                //fix the path for the actual help screens
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/teamB/views/requestForms/helpPopup.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HelpPopupController controller = (HelpPopupController) loader.getController();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Help");
                stage.show();*/

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
