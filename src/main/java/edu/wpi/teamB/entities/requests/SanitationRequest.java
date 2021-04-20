package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SanitationRequest extends Request {

    private String sanitationType;
    private String sanitationSize;
    private String hazardous;
    private String biologicalSubstance;
    private String occupied;

    public SanitationRequest(String sanitationType, String sanitationSize, String hazardous, String biologicalSubstance, String occupied, String requestID, String time, String date, String complete, String employeeName, String location, String description) {
        super(requestID, RequestType.SANITATION, time, date, complete, employeeName, location, description);
        this.sanitationType = sanitationType;
        this.sanitationSize = sanitationSize;
        this.hazardous = hazardous;
        this.biologicalSubstance = biologicalSubstance;
        this.occupied = occupied;
    }
}
