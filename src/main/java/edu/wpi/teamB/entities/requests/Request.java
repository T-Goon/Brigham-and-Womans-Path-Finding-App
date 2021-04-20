package edu.wpi.teamB.entities.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Request {

    private String requestID;

    private String requestType;

    private String time;

    private String date;

    private String complete;

    private String employeeName;

    private String location;

    private String description;
}
