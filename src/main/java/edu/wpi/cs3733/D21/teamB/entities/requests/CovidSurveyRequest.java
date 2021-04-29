package edu.wpi.cs3733.D21.teamB.entities.requests;

import edu.wpi.cs3733.D21.teamB.entities.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CovidSurveyRequest extends Request {

        private String symptomFever;

        private String symptomChills;

        private String symptomCough;

        private String symptomShortBreath;

        private String symptomSoreTht;

        private String symptomHeadache;

        private String symptomAches;

        private String symptomNose;

        private String symptomLostTaste;

        private String symptomNausea;

        private String hadCloseContact;

        private String hadPositiveTest;

        private String username;

        private User.CovidStatus status;

    public CovidSurveyRequest( String requestID,
                               String time,
                               String date,
                               String complete,
                               String employeeName,
                               String location,
                               String description,
                               String username,
                               User.CovidStatus status,
                               String symptomFever,
                               String symptomCough,
                               String symptomShortBreath,
                               String symptomSoreTht,
                               String symptomHeadache,
                               String symptomAches,
                               String symptomNose,
                               String symptomLostTaste,
                               String symptomNausea,
                               String hadCloseContact,
                               String hadPositiveTest) {
        super(requestID, RequestType.COVID, time, date, complete, employeeName, location, description);
        this.username = username;
        this.status = status;
        this.symptomFever = symptomFever;
        this.symptomCough = symptomCough;
        this.symptomSoreTht = symptomSoreTht;
        this.symptomShortBreath = symptomShortBreath;
        this.symptomHeadache = symptomHeadache;
        this.symptomAches = symptomAches;
        this.symptomNose = symptomNose;
        this.symptomLostTaste = symptomLostTaste;
        this.symptomNausea = symptomNausea;
        this.hadCloseContact = hadCloseContact;
        this.hadPositiveTest = hadPositiveTest;

    }
}
