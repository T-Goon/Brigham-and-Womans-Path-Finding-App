package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FloralRequest extends Request {

    private String patientName;
    private String deliveryDate;
    private String startTime;
    private String endTime;
    private List<String> flowerNames;

    public FloralRequest(String patientName, String deliveryDate, String startTime, String endTime, List<String> flowerNames, String requestID, String employeeName, String location, String description) {
        super(requestID, "Floral", employeeName, location, description);
        this.patientName = patientName;
        this.deliveryDate = deliveryDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.flowerNames = flowerNames;
    }
}
