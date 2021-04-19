package edu.wpi.teamB.entities;

import java.util.List;
import java.util.Objects;

public class User {
    public enum AuthenticationLevel {
        ADMIN,
        STAFF,
        PATIENT,
        GUEST
    }

    private AuthenticationLevel authenticationLevel;

    private String username;

    private String firstName;

    private String lastName;

    private List<String> jobs; //Should be List<...> from requests, TODO

    public User(String username, String firstName, String lastName, AuthenticationLevel authenticationLevel, List<String> jobs) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authenticationLevel = authenticationLevel;
        this.jobs = jobs;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AuthenticationLevel getAuthenticationLevel() {
        return authenticationLevel;
    }

    public void setAuthenticationLevel(AuthenticationLevel authenticationLevel) {
        this.authenticationLevel = authenticationLevel;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getJobs() {
        return jobs;
    }

    public void setJobs(List<String> jobs) {
        this.jobs = jobs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return authenticationLevel == user.authenticationLevel && Objects.equals(username, user.username) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(jobs, user.jobs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authenticationLevel, username, firstName, lastName, jobs);
    }
}
