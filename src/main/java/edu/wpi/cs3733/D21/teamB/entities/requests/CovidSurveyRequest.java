package edu.wpi.cs3733.D21.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CovidSurveyRequest extends Request {

        private boolean symptomFever;

        private boolean symptomChills;

        private boolean symptomCough;

        private boolean symptomShortBreath;

        private boolean symptomSoreTht;

        private boolean symptomHeadache;

        private boolean symptomAches;

        private boolean symptomNose;

        private boolean symptomLostTaste;

        private boolean symptomNausea;

        private boolean hadCloseContact;

        private boolean hadPositiveTest;

    public CovidSurveyRequest( String requestID,
                               String time,
                               String date,
                               String complete,
                               String employeeName,
                               String location,
                               String description,
                               boolean symptomFever,
                               boolean symptomCough,
                               boolean symptomShortBreath,
                               boolean symptomSoreTht,
                               boolean symptomHeadache,
                               boolean symptomAches,
                               boolean symptomNose,
                               boolean symptomLostTaste,
                               boolean symptomNausea,
                               boolean hadCloseContact,
                               boolean hadPositiveTest) {
        super(requestID, RequestType.COVID, time, date, complete, employeeName, location, description);
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
