package edu.wpi.teamB.entities.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public abstract class Request {

    private String requestID;

    private String requestType;

    private Date dateRequested;

    private String employeeName;

    private String description;
}
