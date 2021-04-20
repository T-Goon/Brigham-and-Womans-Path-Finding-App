package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalTransportRequest extends Request {

    private String patientName;
    private String transportType;
    private String unconscious;
    private String infectious;

    public InternalTransportRequest(String patientName, String transportType, String unconscious, String infectious, String requestID, String time, String date, String complete, String employeeName, String location, String description) {
        super(requestID, RequestType.INTERNAL_TRANSPORT, time, date, complete, employeeName, location, description);
        this.patientName = patientName;
        this.transportType = transportType;
        this.unconscious = unconscious;
        this.infectious = infectious;
    }
}
