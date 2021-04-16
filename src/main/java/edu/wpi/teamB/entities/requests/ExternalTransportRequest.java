package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExternalTransportRequest extends Request {

    private String patientName;
    private String transportType;
    private String destination;
    private String patientAllergies;
    private boolean outNetwork;
    private boolean infectious;
    private boolean unconscious;

    public ExternalTransportRequest(String patientName, String transportType, String destination, String patientAllergies, boolean outNetwork, boolean infectious, boolean unconscious, String requestID, String employeeName, String location, String description) {
        super(requestID, "External Transport", employeeName, location, description);
        this.patientName = patientName;
        this.transportType = transportType;
        this.destination = destination;
        this.patientAllergies = patientAllergies;
        this.outNetwork = outNetwork;
        this.infectious = infectious;
        this.unconscious = unconscious;
    }
}
