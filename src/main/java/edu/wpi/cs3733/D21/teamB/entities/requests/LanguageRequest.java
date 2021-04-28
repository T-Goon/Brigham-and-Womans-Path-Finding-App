package edu.wpi.cs3733.D21.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LanguageRequest extends Request {

    private String patientName;
    private String timeForArrival;
    private String language;

    public LanguageRequest(String language, String patientName, String timeForArrival, String requestID, String time, String date, String complete, String employeeName, String location, String description) {
        super(requestID, RequestType.LANGUAGE, time, date, complete, employeeName, location, description);
        this.patientName = patientName;
        this.timeForArrival = timeForArrival;
        this.language = language;
    }
}
