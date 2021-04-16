package edu.wpi.teamB.entities.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class Request {

    private String requestID;

    private String requestType;

    private String employeeName;

    private String description;
}
