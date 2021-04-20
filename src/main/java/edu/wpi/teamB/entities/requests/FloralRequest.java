package edu.wpi.teamB.entities.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FloralRequest extends Request {

    private String patientName;
    private String deliveryDate;
    private String startTime;
    private String endTime;
    private String wantsRoses;
    private String wantsTulips;
    private String wantsDaisies;
    private String wantsLilies;
    private String wantsSunflowers;
    private String wantsCarnations;
    private String wantsOrchids;

    public FloralRequest(String patientName, String deliveryDate, String startTime, String endTime,
                         String wantsRoses, String wantsTulips, String wantsDaisies, String wantsLilies,
                         String wantsSunflowers, String wantsCarnations, String wantsOrchids, String requestID,
                         String time, String date, String complete, String employeeName, String location, String description) {
        super(requestID, RequestType.FLORAL, time, date, complete, employeeName, location, description);
        this.patientName = patientName;
        this.deliveryDate = deliveryDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.wantsRoses = wantsRoses;
        this.wantsTulips = wantsTulips;
        this.wantsDaisies = wantsDaisies;
        this.wantsLilies = wantsLilies;
        this.wantsSunflowers = wantsSunflowers;
        this.wantsCarnations = wantsCarnations;
        this.wantsOrchids = wantsOrchids;
    }
}
