package edu.wpi.teamB.entities;

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

    private List<String> jobs; //Should be List<...> from requests, TODO

    /**
     * @param auth the level of authorization required
     * @return whether the user is at least that level of authorization
     */
    public boolean isAtLeast(AuthenticationLevel auth) {
        return authenticationLevel.compareTo(auth) > 0;
    }
}
