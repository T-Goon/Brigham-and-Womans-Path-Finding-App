package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class FoodDeliveryRequestFormController extends DefaultServiceRequestFormController {

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField roomNum;

    @FXML
    private JFXTextField mealChoice;
}