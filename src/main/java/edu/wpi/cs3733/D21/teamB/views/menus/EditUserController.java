package edu.wpi.cs3733.D21.teamB.views.menus;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import edu.wpi.cs3733.D21.teamB.views.requestForms.DefaultServiceRequestFormController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class EditUserController extends BasePageController implements Initializable {

    @FXML
    private JFXTextField username;

    @FXML
    private JFXTextField firstName;

    @FXML
    private JFXTextField lastName;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXComboBox<Label> authenticationLevel;

    @FXML
    private JFXComboBox<Label> job;

    @FXML
    private JFXButton btnBack;

    @FXML
    private JFXButton btnHelp;

    @FXML
    private JFXButton btnSubmit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (User.AuthenticationLevel level : User.AuthenticationLevel.values()) {
            authenticationLevel.getItems().add(new Label(level.toString()));
        }
        for (Request.RequestType r : Request.RequestType.values()) {
            job.getItems().add(new Label(Request.RequestType.prettify(r)));
        }

        User u = (User) App.getPrimaryStage().getUserData();
        username.setText(u.getUsername());
        firstName.setText(u.getFirstName());
        lastName.setText(u.getLastName());
        int i = 0;
        if (!u.getJobs().isEmpty()) {
            for (Label label : job.getItems()) {
                if (label.getText() == Request.RequestType.prettify(u.getJobs().get(0))) {
                    job.getSelectionModel().select(i);
                }
                i++;
            }
        }
        i = 0;
        for (Label label : authenticationLevel.getItems()) {
            if (label.getText() == u.getAuthenticationLevel().toString()) {
                authenticationLevel.getSelectionModel().select(i);
            }
            i++;
        }

        validateButtons();

    }

    public void handleButtonAction(ActionEvent actionEvent) {
        super.handleButtonAction(actionEvent);

        JFXButton btn = (JFXButton) actionEvent.getSource();
        if (btn.getId().equals("btnSubmit")) {

            String uUsername = username.getText();
            String uFirstName = firstName.getText();
            String uLastName = lastName.getText();
            User.AuthenticationLevel uAuthLevel = User.AuthenticationLevel.valueOf(authenticationLevel.getValue().getText());
            List<Request.RequestType> uJobs = new ArrayList<Request.RequestType>();
            if (job.getValue() != null) {
                Request.RequestType uJob = Request.RequestType.uglify(job.getValue().getText());
                uJobs.add(uJob);
            }

            User uUpdated = new User(uUsername, uFirstName, uLastName, uAuthLevel, uJobs);

            try {
                if (SceneSwitcher.addingUser) {
                    DatabaseHandler.getDatabaseHandler("main.db").addUser(uUpdated,password.getText());
                } else {
                    DatabaseHandler.getDatabaseHandler("main.db").updateUser(uUpdated);
                }
            } catch (SQLException throwables) {
                throw new IllegalStateException("Username not found when updating; This should never happen");
            }
            SceneSwitcher.goBack(getClass(), 1);
        } else if (btn.getId().equals("btnCancel")) {
            SceneSwitcher.goBack(getClass(), 1);
        }
    }

    @FXML
    private void validateButtons() {
        btnSubmit.setDisable(
                username.getText().isEmpty() || firstName.getText().isEmpty() || lastName.getText().isEmpty() ||
                        authenticationLevel.getValue() == null
        );

        username.setDisable(!SceneSwitcher.addingUser);
        password.setDisable(!SceneSwitcher.addingUser);
    }
}
