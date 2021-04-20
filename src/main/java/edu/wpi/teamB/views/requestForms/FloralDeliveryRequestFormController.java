package edu.wpi.teamB.views.requestForms;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.math.RoundingMode;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class FloralDeliveryRequestFormController extends DefaultServiceRequestFormController  {

    @FXML
    private JFXTextField patientName;

    @FXML
    private JFXTextField roomNumber;

    @FXML
    private JFXDatePicker deliveryDate;

    @FXML
    private JFXTimePicker startTime;

    @FXML
    private JFXTimePicker endTime;

    @FXML
    private JFXTextArea message;

    @FXML
    private Label totalPrice;

    @FXML
    private JFXCheckBox roses;

    @FXML
    private JFXCheckBox tulips;

    @FXML
    private JFXCheckBox daisies;

    @FXML
    private JFXCheckBox lilies;

    @FXML
    private JFXCheckBox sunflowers;

    @FXML
    private JFXCheckBox carnations;

    @FXML
    private JFXCheckBox orchids;


    @FXML
    private void updatePrice() {
        double currentPrice = 0;
        if (roses.isSelected()) currentPrice += 2.99;
        if (tulips.isSelected()) currentPrice += 2.99;
        if (daisies.isSelected()) currentPrice += 2.99;
        if (lilies.isSelected()) currentPrice += 2.99;
        if (sunflowers.isSelected()) currentPrice += 2.99;
        if (carnations.isSelected()) currentPrice += 2.99;
        if (orchids.isSelected()) currentPrice += 2.99;

        NumberFormat nf= NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        totalPrice.setText("Total Price: $" + nf.format(currentPrice));
    }

    @FXML
    private void validateButton(){
        btnSubmit.setDisable(
                patientName.getText().isEmpty() || roomNumber.getText().isEmpty() ||
                deliveryDate.getValue() == null || startTime.getValue() == null ||
                endTime.getValue() == null || message.getText().isEmpty()
        );
    }
}