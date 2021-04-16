package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternalTransportRequest extends Request {

    private String patientName;
    private String transportType;
    private boolean unconscious;
    private boolean infectious;

    public InternalTransportRequest(String patientName, String transportType, boolean unconscious, boolean infectious, String requestID, String requestType, String employeeName, String description) {
        super(requestID, requestType, employeeName, description);
        this.patientName = patientName;
        this.transportType = transportType;
        this.unconscious = unconscious;
        this.infectious = infectious;
    }
}
