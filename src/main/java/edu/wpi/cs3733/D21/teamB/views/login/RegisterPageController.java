package edu.wpi.cs3733.D21.teamB.views.login;

import ai.djl.modality.cv.BufferedImageFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.Embedding;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.util.ExternalCommunication;
import edu.wpi.cs3733.D21.teamB.util.PageCache;
import edu.wpi.cs3733.D21.teamB.util.SceneSwitcher;
import edu.wpi.cs3733.D21.teamB.views.BasePageController;
import edu.wpi.cs3733.D21.teamB.views.face.Camera;
import edu.wpi.cs3733.D21.teamB.views.face.EmbeddingModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPageController extends BasePageController implements Initializable {

    @FXML
    public JFXTextField username;

    @FXML
    public JFXTextField email;

    @FXML
    public JFXTextField firstName;

    @FXML
    public JFXTextField lastName;

    @FXML
    public JFXPasswordField password;

    @FXML
    public JFXPasswordField retypePassword;

    @FXML
    public JFXToggleButton ttsEnabled;

    @FXML
    public Label error;

    @FXML
    public JFXButton btnRegister;

    @FXML
    private JFXButton btnLoginPage;

    @FXML
    private StackPane stackPane;

    private final Pattern emailPattern = Pattern.compile(".+@.+");

    @FXML
    private ImageView pictureImage,
                    cameraImage;
    @FXML
    private JFXButton btnTakePicture;

    private Camera camera;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Camera.stopAcquisition();

        super.initialize(location, resources);
        username.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty())
                handleRegisterSubmit();
        });

        email.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty())
                handleRegisterSubmit();
        });

        firstName.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty())
                handleRegisterSubmit();
        });

        lastName.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty())
                handleRegisterSubmit();
        });

        password.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty())
                handleRegisterSubmit();
        });

        retypePassword.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !areFormsEmpty())
                handleRegisterSubmit();
        });

        camera = new Camera(pictureImage, cameraImage, btnTakePicture, false);
        camera.toggleCamera();

        Platform.runLater(() -> {
            if (!PageCache.isTextfieldFocused())
                username.requestFocus();
        });
    }

    @FXML
    public void handleButtonAction(ActionEvent actionEvent) {
        final String currentPath = "/edu/wpi/cs3733/D21/teamB/views/login/registerPage.fxml";
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch (btn.getId()) {
            case "btnRegister":
                handleRegisterSubmit();
                break;
            case "btnEmergency":
            case "btnBack":
                Camera.stopAcquisition();
                break;
            case "btnLoginPage":
                SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/login/loginPage.fxml");
                break;
        }

        super.handleButtonAction(actionEvent);
    }

    private void handleRegisterSubmit() {
        DatabaseHandler db = DatabaseHandler.getHandler();
        if (db.getUserByUsername(username.getText()) != null) {
            error.setText("Username is already taken!");
            error.setVisible(true);
            return;
        }

        // Check if the email address is already associated with an account
        if (db.getUserByEmail(email.getText()) != null) {
            error.setText("Email address already has an account!");
            error.setVisible(true);
            return;
        }

        // Check if the email address is valid
        Matcher matcher = emailPattern.matcher(email.getText());
        if (!matcher.matches()) {
            error.setText("Email address must be valid!");
            error.setVisible(true);
            return;
        }

        // Check to make sure the passwords are the same
        if (!password.getText().equals(retypePassword.getText())) {
            error.setText("Passwords do not match!");
            error.setVisible(true);
            return;
        }

        // Add the user to the database
        User newUser = new User(username.getText(), email.getText(), firstName.getText(), lastName.getText(), User.AuthenticationLevel.PATIENT, ttsEnabled.isSelected() ? "T" : "F", new ArrayList<>());
        try {
            db.addUser(newUser, password.getText());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Store in db
        Thread addEmbedding = new Thread(()->{
            try {
                ArrayList<Double> embeddingArray = new ArrayList<>();
                try {

                    double [] temp = EmbeddingModel.getModel().embedding((new BufferedImageFactory()).fromImage(Camera.MatConvert(camera.getPictureTaken())));

                    for(double d : temp){
                        embeddingArray.add(d);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Embedding embedding = new Embedding(username.getText(), embeddingArray);
                db.addEmbedding(embedding);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        addEmbedding.start();

        Camera.stopAcquisition();
        ExternalCommunication.sendConfirmation(email.getText(), firstName.getText());
        SceneSwitcher.switchFromTemp("/edu/wpi/cs3733/D21/teamB/views/login/successfulRegistration.fxml");
    }

    public void validateButton(KeyEvent keyEvent) {
        btnRegister.setDisable(areFormsEmpty());
    }

    /**
     * @return whether any of the fields are empty
     */
    private boolean areFormsEmpty() {
        return username.getText().isEmpty() || email.getText().isEmpty() || firstName.getText().isEmpty() || lastName.getText().isEmpty()
                || password.getText().isEmpty() || retypePassword.getText().isEmpty()|| !camera.isPictureTaken();
    }
}
