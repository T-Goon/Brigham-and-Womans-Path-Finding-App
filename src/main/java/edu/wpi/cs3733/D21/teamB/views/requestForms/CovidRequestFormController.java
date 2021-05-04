package edu.wpi.cs3733.D21.teamB.views.requestForms;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.requests.CovidSurveyRequest;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CovidRequestFormController extends DefaultServiceRequestFormController implements Initializable {

    @FXML
    private JFXComboBox<Label> reviewStatus;

    @FXML
    private Text symptomTxt;

    String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id = (String) App.getPrimaryStage().getUserData();
        CovidSurveyRequest request = null;
        try {
            request = (CovidSurveyRequest) DatabaseHandler.getHandler().getSpecificRequestById(id, Request.RequestType.COVID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String symptoms = "";
        assert request != null;
        if(request.getSymptomFever().equals("T")) symptoms += " - Fever\n";
        if(request.getSymptomChills().equals("T")) symptoms += " - Chills\n";
        if(request.getSymptomCough().equals("T")) symptoms += " - Cough\n";
        if(request.getSymptomShortBreath().equals("T")) symptoms += " - Shortness of Breath\n";
        if(request.getSymptomSoreTht().equals("T")) symptoms += " - Sore Throat\n";
        if(request.getSymptomHeadache().equals("T")) symptoms += " - Headache\n";
        if(request.getSymptomAches().equals("T")) symptoms += " - Muscle/Body Ache\n";
        if(request.getSymptomNose().equals("T")) symptoms += " - Runny Node/Congestion\n";
        if(request.getSymptomLostTaste().equals("T")) symptoms += " - Loss of Taste/Smell\n";
        if(request.getSymptomNausea().equals("T")) symptoms += " - Nausea/Vomiting\n";
        if(symptoms.isEmpty()) symptoms = "- No Symptoms";
        if(request.getHadCloseContact().equals("T")){
            symptoms += "\n The patient has had close contact with Covid-19 exposed individuals in the past 2 weeks";
        }
        if(request.getHadPositiveTest().equals("T")){
            symptoms += "\n The patient has had a positive Covid-19 test in the past 2 weeks";
        }

        symptomTxt.setText(symptoms);


        for(User.CovidStatus stat : User.CovidStatus.values()){
            reviewStatus.getItems().add(new Label(stat.toString()));
        }
        int i = 0;
        for (Label label : reviewStatus.getItems()) {
            if (label.getText().equals(request.getStatus().toString())) {
                reviewStatus.getSelectionModel().select(i);
            }
            i++;
        }

        validateButton();

    }

    public void handleButtonAction(ActionEvent a) {
        super.handleButtonAction(a);
        JFXButton btn = (JFXButton) a.getSource();
        if (btn.getId().equals("btnSubmit")) {
            CovidSurveyRequest request = null;
            try {
                request = (CovidSurveyRequest) DatabaseHandler.getHandler().getSpecificRequestById(id, Request.RequestType.COVID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            User.CovidStatus status = User.CovidStatus.valueOf(reviewStatus.getValue().getText());

            assert request != null;
            request.setStatus(status);
            if(status != User.CovidStatus.PENDING){
                request.setProgress("T");
            }

            try {
                DatabaseHandler.getHandler().updateRequest(request);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @FXML
    private void validateButton() {
        btnSubmit.setDisable(
                reviewStatus.getValue() == null
        );
    }
}
