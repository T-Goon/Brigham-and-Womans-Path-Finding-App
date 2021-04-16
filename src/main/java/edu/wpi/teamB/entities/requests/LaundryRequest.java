package edu.wpi.teamB.entities.requests;

public class LaundryRequest extends Request {
    private String serviceType;
    private String serviceSize;
    private boolean dark;
    private boolean light;
    private boolean occupied;

    public ExternalTransportRequest(String serviceType, String serviceSize, boolean dark, boolean light, boolean occupied, String requestID, String requestType, String employeeName, String location, String description) {
        super(requestID, requestType, employeeName, location, description);
        this.serviceType = serviceType;
        this.serviceSize = serviceSize;
        this.dark = dark;
        this.light = light;
        this.occupied = occupied;
    }
}
