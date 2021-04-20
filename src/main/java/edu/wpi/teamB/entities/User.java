package edu.wpi.teamB.entities;

import edu.wpi.teamB.entities.requests.Request;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    public enum AuthenticationLevel {
        ADMIN,
        STAFF,
        PATIENT,
        GUEST
    }

    private String username;

    private String firstName;

    private String lastName;

    private AuthenticationLevel authenticationLevel;

    private List<Request.RequestType> jobs;

    /**
     * @param auth the level of authorization required
     * @return whether the user is at least that level of authorization
     */
    public boolean isAtLeast(AuthenticationLevel auth) {
        switch (auth) {
            case GUEST:
                return true;
            case PATIENT:
                return authenticationLevel != AuthenticationLevel.GUEST;
            case STAFF:
                return authenticationLevel == AuthenticationLevel.STAFF || authenticationLevel == AuthenticationLevel.ADMIN;
            case ADMIN:
                return authenticationLevel == AuthenticationLevel.ADMIN;
            default:
                throw new IllegalStateException("Unknown authentication level!");
        }
    }
}
