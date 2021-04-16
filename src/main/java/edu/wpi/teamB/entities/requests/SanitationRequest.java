package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SanitationRequest extends Request {

    private String sanitationType;
    private String sanitationSize;
    private boolean hazardous;
    private boolean biologicalSubstance;
    private boolean occupied;

    public SanitationRequest(String sanitationType, String sanitationSize, boolean hazardous, boolean biologicalSubstance, boolean occupied, String requestID, String employeeName, String location, String description) {
        super(requestID, "Sanitation", employeeName, location, description);
        this.sanitationType = sanitationType;
        this.sanitationSize = sanitationSize;
        this.hazardous = hazardous;
        this.biologicalSubstance = biologicalSubstance;
        this.occupied = occupied;
    }
}
