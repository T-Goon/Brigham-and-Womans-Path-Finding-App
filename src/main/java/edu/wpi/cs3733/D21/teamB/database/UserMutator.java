package edu.wpi.cs3733.D21.teamB.database;

import edu.wpi.cs3733.D21.teamB.entities.IStoredEntity;
import edu.wpi.cs3733.D21.teamB.entities.User;
import edu.wpi.cs3733.D21.teamB.entities.requests.Request;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
                + "', '" + user.user.getEmail()
                + "', '" + user.user.getFirstName()
                + "', '" + user.user.getLastName()
                + "', '" + user.user.getAuthenticationLevel().toString()
                + "', '" + user.user.getCovidStatus().toString()
                + "', '" + user.user.getTtsEnabled()
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
     * Updates the information for a given user
     *
     * @param newUser the updated user
     */
    public void updateEntity(UserPasswordMatch newUser) throws SQLException {
        String updateUser = "UPDATE Users " +
                "SET username = '" + newUser.user.getUsername() + "'," +
                "email = '" + newUser.user.getEmail() + "'," +
                "firstName = '" + newUser.user.getFirstName() + "'," +
                "lastName = '" + newUser.user.getLastName() + "'," +
                "authenticationLevel = '" + newUser.user.getAuthenticationLevel().toString() + "'," +
                "covidStatus = '" + newUser.user.getCovidStatus().toString() + "'," +
                "ttsEnabled = '" + newUser.user.getTtsEnabled() + "' " +
                "WHERE (username = '" + newUser.user.getUsername() + "')";
        String deleteJobs = "DELETE FROM Jobs WHERE (username = '" + newUser.user.getUsername() + "')";
        if (db.getUserByUsername(newUser.user.getUsername()) != null) {
            db.runStatement(deleteJobs, false);
            db.runStatement(updateUser, false);
            for (Request.RequestType job : newUser.user.getJobs()) {
                String query = "INSERT INTO Jobs VALUES " +
                        "('" + newUser.user.getUsername()
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
        String deleteFace = "DELETE FROM Embeddings WHERE (username = '" + username + "')";
        db.runStatement(deleteJobs, false);
        db.runStatement(deleteFace, false);
        db.runStatement(deleteUser, false);
    }

    /**
     * Get all users in database
     * @return List of User objects in table
     */

    public List<User> getUsers() {
        try {
            String query = "SELECT username FROM Users";
            ResultSet rs = db.runStatement(query, true);
            List<User> outUsers = new ArrayList<>();
            if (rs == null) return outUsers;
            do {
                String username = rs.getString("username");
                outUsers.add(this.getUserByUsername(username));
            } while (rs.next());
            rs.close();
            return outUsers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param username username to query by
     * @return User object with that username, or null if that user doesn't exist
     */
    public User getUserByUsername(String username) {
        try {
            String query = "SELECT job FROM Jobs WHERE (username = '" + username + "')";
            List<Request.RequestType> jobs = new ArrayList<>();
            ResultSet rs = db.runStatement(query, true);
            if (rs != null) {
                do {
                    jobs.add(Request.RequestType.valueOf(rs.getString("job")));
                } while (rs.next());
                rs.close();
            }

            query = "SELECT * FROM Users WHERE (username = '" + username + "')";
            rs = db.runStatement(query, true);
            if (rs == null) return null;
            User outUser = new User(
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    User.AuthenticationLevel.valueOf(rs.getString("authenticationLevel")),
                    User.CovidStatus.valueOf(rs.getString("covidStatus")),
                    rs.getString("ttsEnabled"),
                    jobs
            );
            rs.close();
            return outUser;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * @param email email to query by
     * @return User object with that email, or null if that user doesn't exist
     */
    public User getUserByEmail(String email) {
        try {
            String query = "SELECT * FROM Users WHERE (email = '" + email + "')";
            ResultSet rs = db.runStatement(query, true);
            if (rs == null) return null;
            User outUser = new User(
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    User.AuthenticationLevel.valueOf(rs.getString("authenticationLevel")),
                    rs.getString("ttsEnabled"),
                    null
            );
            rs.close();
            return outUser;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Gets a list of users who are assigned to handle jobs of certain type
     *
     * @param job RequestType enum of the type of job you want the users for
     * @return a list of users who are assigned to jobs of the given type, or null if none of them do
     */
    public List<User> getUsersByJob(Request.RequestType job) {
        try {
            String query = "SELECT username FROM " +
                    "Users NATURAL JOIN Jobs " +
                    "WHERE (job = '" + job.toString() + "')";
            ResultSet rs = db.runStatement(query, true);
            List<User> outUsers = new ArrayList<>();
            if (rs == null) return outUsers;
            do {
                String username = rs.getString("username");
                outUsers.add(this.getUserByUsername(username));
            } while (rs.next());
            rs.close();
            return outUsers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a list of users with the given authentication level
     *
     * @param authenticationLevel the EXACT authentication level you want the users for
     * @return list of users, or null if no users have that level
     */
    public List<User> getUsersByAuthenticationLevel(User.AuthenticationLevel authenticationLevel) {
        try {
            String query = "SELECT username, authenticationLevel FROM " +
                    "Users WHERE authenticationLevel='" + authenticationLevel.toString() + "'";
            ResultSet rs = db.runStatement(query, true);
            List<User> outUsers = new ArrayList<>();
            if (rs == null) return outUsers;
            do {
                String username = rs.getString("username");
                outUsers.add(this.getUserByUsername(username));
            } while (rs.next());
            rs.close();
            return outUsers;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param username Claimed username of authenticator
     * @param password Claimed plaintext password of authenticator
     * @return If authentication is successful, return the User object representing the authenticated user, or null if not found
     */
    public User authenticate(String username, String password) {
        try {
            String query = "SELECT passwordHash FROM Users WHERE (username = '" + username + "')";
            ResultSet rs = db.runStatement(query, true);
            if (rs == null) return null;
            String storedHash = (rs.getString("passwordHash"));
            rs.close();

            // Make sure the hashed password matches
            if (!BCrypt.checkpw(password, storedHash)) return null;

            return this.getUserByUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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

    public void updateParkingForUser(String favoriteLocation) throws SQLException {
        User user = db.getAuthenticationUser();
        String username = user.getUsername();

        String query = "UPDATE FavoriteLocations SET favoriteLocation = '" + favoriteLocation
                + "' WHERE username = '" + username + "' AND (favoriteLocation LIKE '%Parking%' OR favoriteLocation LIKE '%parking%')";
        db.runStatement(query, false);
    }

    public List<String> getFavoritesForUser() throws SQLException {
        User user = db.getAuthenticationUser();
        String username = user.getUsername();

        String query = "SELECT * FROM FavoriteLocations WHERE username = '" + username + "'";
        ResultSet rs = db.runStatement(query, true);
        ArrayList<String> favorites = new ArrayList<>();
        if (rs == null) return favorites;
        do {
            favorites.add(rs.getString("favoriteLocation"));
        } while (rs.next());

        return favorites;
    }

    @AllArgsConstructor
    public static class UserPasswordMatch implements IStoredEntity {
        User user;
        String password;
    }

    /**
     * Updates a password for the current user
     *
     * @param passwordHash the hashed password
     * @throws SQLException if the query is malformed
     */
    public void updatePasswordForUser(String passwordHash) throws SQLException {
        String username = db.getAuthenticationUser().getUsername();
        String query = "UPDATE Users SET passwordHash = '" + passwordHash + "' WHERE username = '" + username + "'";
        db.runStatement(query, false);
    }
}
