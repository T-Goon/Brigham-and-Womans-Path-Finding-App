package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.teamB.App;
import edu.wpi.teamB.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class DefaultServiceRequestFormController implements Initializable {

    @FXML
    protected JFXButton btnSubmit;

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

    @FXML
    private StackPane stackContainer;

    private VBox helpPopup;
    private double x = 0;
    private double y = 0;
    private boolean justClicked = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.getPrimaryStage().getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (helpPopup != null && !justClicked) {
                helpHolder.getChildren().remove(helpPopup);
                helpPopup = null;
            }
            justClicked = false;
        });

        btnSubmit.setDisable(true);
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
                loadHelpDialog();
                break;

            case "btnExit":
                Platform.exit();
                break;
            case "btnEmergency":
                SceneSwitcher.switchScene(getClass(), "/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml", "/edu/wpi/teamB/views/requestForms/emergencyForm.fxml");
                break;
        }
    }

    private void loadHelpDialog(){
        JFXDialogLayout helpLayout = new JFXDialogLayout();

        Text helpText = new Text("Please fill out this form completely. Once each field is full, you can submit the form and an employee will be assigned.\nIf you wish to end your request early, click 'Cancel'. If your request is an emergency please click 'Emergency'.");
        helpText.setFont(new Font("MS Reference Sans Serif", 14));

        Label headerLabel = new Label("Help");
        headerLabel.setFont(new Font("MS Reference Sans Serif", 18));

        helpLayout.setHeading(headerLabel);
        helpLayout.setBody(helpText);
        JFXDialog helpWindow = new JFXDialog(stackContainer, helpLayout, JFXDialog.DialogTransition.CENTER);

        JFXButton button = new JFXButton("Close");
        button.setOnAction(event -> helpWindow.close());
        helpLayout.setActions(button);

        helpWindow.show();

    }
}
