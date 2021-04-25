package edu.wpi.teamB.database;


import edu.wpi.cs3733.D21.teamB.database.DatabaseHandler;
import edu.wpi.cs3733.D21.teamB.database.IDatabaseEntityMutator;
import edu.wpi.cs3733.D21.teamB.entities.IStoredEntity;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import lombok.AllArgsConstructor;

import java.sql.SQLException;

public class UserMutator implements IDatabaseEntityMutator<UserMutator.UserPasswordMatch> {

    private final DatabaseHandler db;

    public UserMutator(DatabaseHandler db) {
        this.db = db;
    }

    /**
     * @param user User and password match to add
     */
    public void addEntity(UserPasswordMatch user) throws SQLException {
        String hash = db.passwordHash(user.password);
        String query = "INSERT INTO Users VALUES " +
                "('" + user.user.getUsername()
                + "', '" + user.user.getFirstName()
                + "', '" + user.user.getLastName()
                + "', '" + user.user.getAuthenticationLevel().toString()
                + "', '" + hash
                + "')";
        db.runStatement(query, false);
        if (user.user.getJobs() != null) {
            for (Request.RequestType job : user.user.getJobs()) {
                query = "INSERT INTO Jobs VALUES " +
                        "('" + user.user.getUsername()
                        + "', '" + job.toString()
                        + "')";
                db.runStatement(query, false);
            }
        }
    }

    /**
     * Given the username of a user, delete them from the database
     *
     * @param username the username of the user to delete
     */
    public void removeEntity(String username) throws SQLException {
        String deleteJobs = "DELETE FROM Jobs WHERE (username = '" + username + "')";
        String deleteUser = "DELETE FROM Users WHERE (username = '" + username + "')";
        db.runStatement(deleteJobs, false);
        db.runStatement(deleteUser, false);
    }

    /**
     * Updates the information for a given user
     *
     * @param newUser the updated user
     */
    public void updateEntity(UserPasswordMatch newUser) throws SQLException {
        String updateUser = "UPDATE Users " +
                "SET username = '" + newUser.user.getUsername() + "'," +
                "firstName = '" + newUser.user.getFirstName() + "'," +
                "lastName = '" + newUser.user.getLastName() + "'," +
                "authenticationLevel = '" + newUser.user.getAuthenticationLevel().toString() + "'" +
                "WHERE (username = '" + newUser.user.getUsername() + "')";
        String deleteJobs = "DELETE FROM Jobs WHERE (username = '" + newUser.user.getUsername() + "')";
        if (db.getUserByUsername(newUser.user.getUsername()) != null) {
            db.runStatement(updateUser, false);
            db.runStatement(deleteJobs, false);
            for (Request.RequestType job : newUser.user.getJobs()) {
                String query = "INSERT INTO Jobs VALUES " +
                        "('" + newUser.user.getUsername()
                        + "', '" + job.toString()
                        + "')";
                db.runStatement(query, false);
            }
        }
    }

    public void addFavoriteToUser(String favoriteLocation) throws SQLException {
        User user = db.getAuthenticationUser();
        String username = user.getUsername();

        String query = "INSERT INTO FavoriteLocations VALUES " +
                "('" + username
                + "', '" + favoriteLocation
                + "')";
        db.runStatement(query, false);
    }

    public void removeFavoriteFromUser(String favoriteLocation) throws SQLException {
        User user = db.getAuthenticationUser();
        String username = user.getUsername();

        String query = "DELETE FROM FavoriteLocations WHERE username = '" + username + "' AND favoriteLocation = '" + favoriteLocation + "'";
        db.runStatement(query, false);
    }

    public void getFavoritesForUser() {
        User user = db.getAuthenticationUser();
        String username = user.getUsername();

    }

    @AllArgsConstructor
    public static class UserPasswordMatch implements IStoredEntity {
        User user;
        String password;
    }

}
