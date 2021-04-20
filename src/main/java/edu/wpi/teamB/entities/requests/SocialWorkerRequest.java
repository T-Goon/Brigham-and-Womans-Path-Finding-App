package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialWorkerRequest extends Request {

    private String patientName;
    private String timeForArrival;

    public SocialWorkerRequest(String patientName, String timeForArrival, String requestID, String time, String date, String complete, String employeeName, String location, String description) {
        super(requestID, RequestType.SOCIAL_WORKER, time, date, complete, employeeName, location, description);
        this.patientName = patientName;
        this.timeForArrival = timeForArrival;
    }
}
