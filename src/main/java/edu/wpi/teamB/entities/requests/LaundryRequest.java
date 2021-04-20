package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaundryRequest extends Request {

    private String serviceType;
    private String serviceSize;
    private String dark;
    private String light;
    private String occupied;

    public LaundryRequest(String serviceType, String serviceSize, String dark, String light, String occupied, String requestID, String time, String date, String complete, String employeeName, String location, String description) {
        super(requestID, RequestType.LAUNDRY, time, date, complete, employeeName, location, description);
        this.serviceType = serviceType;
        this.serviceSize = serviceSize;
        this.dark = dark;
        this.light = light;
        this.occupied = occupied;
    }
}
