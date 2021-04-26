package edu.wpi.cs3733.D21.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmergencyRequest extends Request {

    private String medicalEmergency;
    private String securityEmergency;

    public EmergencyRequest(String medicalEmergency, String securityEmergency, String requestID, String time, String date, String complete, String employeeName, String location, String description) {
        super(requestID, RequestType.EMERGENCY, time, date, complete, employeeName, location, description);
        this.medicalEmergency = medicalEmergency;
        this.securityEmergency = securityEmergency;
    }
}
