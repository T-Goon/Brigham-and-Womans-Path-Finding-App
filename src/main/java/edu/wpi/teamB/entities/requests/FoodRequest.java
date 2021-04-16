package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodRequest extends Request {

    private String patientName;
    private String arrivalTime;
    private String mealChoice;

    public FoodRequest(String patientName, String arrivalTime, String mealChoice, String requestID, String employeeName, String location, String description) {
        super(requestID, "Food", employeeName, location, description);
        this.patientName = patientName;
        this.arrivalTime = arrivalTime;
        this.mealChoice = mealChoice;
    }
}
