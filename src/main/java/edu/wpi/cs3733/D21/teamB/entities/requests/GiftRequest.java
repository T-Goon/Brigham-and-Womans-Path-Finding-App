package edu.wpi.cs3733.D21.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GiftRequest extends Request{

    private String patientName;
    private String deliveryDate;
    private String startTime;
    private String endTime;
    private String wantsBalloons;
    private String wantsTeddyBear;
    private String wantsChocolate;


    public GiftRequest(String patientName, String deliveryDate, String startTime, String endTime,
                         String wantsBalloons, String wantsTeddyBear, String wantsChocolate, String requestID,
                         String time, String date, String complete, String employeeName, String location, String description) {
        super(requestID, RequestType.GIFT, time, date, complete, employeeName, location, description);
        this.patientName = patientName;
        this.deliveryDate = deliveryDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.wantsBalloons = wantsBalloons;
        this.wantsTeddyBear = wantsTeddyBear;
        this.wantsChocolate = wantsChocolate;
    }
}
