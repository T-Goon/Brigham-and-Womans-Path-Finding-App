package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicineRequest extends Request {

    private String patientName;
    private String medicine;

    public MedicineRequest(String patientName, String medicine, String requestID, String time, String date, String complete, String employeeName, String location, String description) {
        super(requestID, RequestType.MEDICINE, time, date, complete, employeeName, location, description);
        this.patientName = patientName;
        this.medicine = medicine;
    }
}
