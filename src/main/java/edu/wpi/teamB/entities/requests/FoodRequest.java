package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodRequest extends Request {

    private String patientName;
    private String arrivalTime;
    private String mealChoice;

    public FoodRequest(String patientName, String arrivalTime, String mealChoice, String requestID, String requestType, String employeeName, String description) {
        super(requestID, requestType, employeeName, description);
        this.patientName = patientName;
        this.arrivalTime = arrivalTime;
        this.mealChoice = mealChoice;
    }
}
