package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.teamB.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LaundryRequestFormController implements Initializable {
    @FXML
    private JFXButton btnCancel;

    @FXML
    private JFXButton SubmitB;

    @FXML
    private JFXComboBox<Label> comboTypeService = new JFXComboBox<>();

    @FXML
    private JFXComboBox<Label> comboSizeService = new JFXComboBox<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboTypeService.getItems().add(new Label("Regular Cycle"));
        comboTypeService.getItems().add(new Label("Delicate Cycle"));
        comboTypeService.getItems().add(new Label("Permanent Press"));

        comboSizeService.getItems().add(new Label("Small"));
        comboSizeService.getItems().add(new Label("Medium"));
        comboSizeService.getItems().add(new Label("Large"));
    }

    public void handleButtonAction(ActionEvent actionEvent) {
        JFXButton btn = (JFXButton) actionEvent.getSource();

        switch (btn.getId()) {
            case "btnCancel":
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/menus/serviceRequestMenu.fxml"));
                    App.getPrimaryStage().getScene().setRoot(root);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
            case "SubmitB":
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/teamB/views/requestForms/formSubmitted.fxml"));
                    App.getPrimaryStage().getScene().setRoot(root);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    break;
                }
        }
    }
}
