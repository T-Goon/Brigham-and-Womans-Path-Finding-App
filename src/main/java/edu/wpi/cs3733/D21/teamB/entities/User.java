package edu.wpi.cs3733.D21.teamB.entities;

import edu.wpi.cs3733.D21.teamB.App;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class User implements IStoredEntity {
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

    public boolean addJob(Request.RequestType job){
       if(jobs.contains(job)){
           return false;
       }
       jobs.add(job);
       return true;
    }

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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", authenticationLevel=" + authenticationLevel +
                ", jobs=" + jobs +
                '}';
    }
}
