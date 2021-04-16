package edu.wpi.teamB.entities.requests;

public class MedicineRequest extends Request {

    private String patientName;
    private String medicine;

    public ExternalTransportRequest(String patientName, String medicine, String requestID, String requestType, String employeeName, String location, String description) {
        super(requestID, requestType, employeeName, location, description);
        this.patientName = patientName;
        this.medicine = medicine;
    }
}
