package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaseManagerRequest extends Request {

    private String patientName;
    private String timeForArrival;

    public CaseManagerRequest(String patientName, String timeForArrival, String requestID, String time, String date, String complete, String employeeName, String location, String description) {
        super(requestID, "Case Manager", time, date, complete, employeeName, location, description);
        this.patientName = patientName;
        this.timeForArrival = timeForArrival;
    }
}
