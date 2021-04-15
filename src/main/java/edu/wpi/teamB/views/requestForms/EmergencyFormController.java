package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;


public class EmergencyFormController extends DefaultServiceRequestFormController{

    @FXML
    private JFXTextField wing;

    @FXML
    private JFXTextField roomNum;

    @FXML
    private JFXTextArea description;



}
