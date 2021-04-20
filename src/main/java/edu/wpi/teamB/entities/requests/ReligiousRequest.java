package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReligiousRequest extends Request {

    private String patientName;
    private String religiousDate;
    private String startTime;
    private String endTime;
    private String faith;
    private String infectious;

    public ReligiousRequest(String patientName, String religiousDate, String startTime, String endTime, String faith, String infectious, String requestID, String time, String date, String complete, String employeeName, String location, String description) {
        super(requestID, RequestType.RELIGIOUS, time, date, complete, employeeName, location, description);
        this.patientName = patientName;
        this.religiousDate = religiousDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.faith = faith;
        this.infectious = infectious;
    }
}
