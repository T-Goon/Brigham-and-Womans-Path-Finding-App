package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaundryRequest extends Request {
    private String serviceType;
    private String serviceSize;
    private boolean dark;
    private boolean light;
    private boolean occupied;

    public LaundryRequest(String serviceType, String serviceSize, boolean dark, boolean light, boolean occupied, String requestID, String employeeName, String location, String description) {
        super(requestID, "Laundry", employeeName, location, description);
        this.serviceType = serviceType;
        this.serviceSize = serviceSize;
        this.dark = dark;
        this.light = light;
        this.occupied = occupied;
    }
}
