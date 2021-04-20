package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalTransportRequest extends Request {

    private String patientName;
    private String transportType;
    private String destination;
    private String patientAllergies;
    private String outNetwork;
    private String infectious;
    private String unconscious;

    public ExternalTransportRequest(String patientName, String transportType, String destination, String patientAllergies, String outNetwork, String infectious, String unconscious, String requestID, String time, String date, String complete, String employeeName, String location, String description) {
        super(requestID, RequestType.EXTERNAL_TRANSPORT, time, date, complete, employeeName, location, description);
        this.patientName = patientName;
        this.transportType = transportType;
        this.destination = destination;
        this.patientAllergies = patientAllergies;
        this.outNetwork = outNetwork;
        this.infectious = infectious;
        this.unconscious = unconscious;
    }
}
