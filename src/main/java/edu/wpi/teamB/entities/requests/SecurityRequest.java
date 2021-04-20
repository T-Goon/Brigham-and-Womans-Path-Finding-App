package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityRequest extends Request {

    private int urgency;

    public SecurityRequest(int urgency, String requestID, String time, String date, String complete, String employeeName, String location, String description) {
        super(requestID, RequestType.SECURITY, time, date, complete, employeeName, location, description);
        this.urgency = urgency;
    }
}
