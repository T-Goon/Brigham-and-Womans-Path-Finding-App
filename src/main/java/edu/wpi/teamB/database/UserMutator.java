package edu.wpi.teamB.database;

import edu.wpi.teamB.entities.User;
import edu.wpi.teamB.entities.requests.Request;

import java.sql.SQLException;
import java.sql.Statement;

public class UserMutator implements IDatabaseEntityMutator<UserPasswordMatch> {

    /**
     * @param user     User and password match to add
     * @return Whether user has been successfully added
     */
    public void addEntity(UserPasswordMatch user) throws SQLException {
        Statement statement = DatabaseHandler.getDatabaseHandler("main.db").getStatement();
        String hash = DatabaseHandler.getDatabaseHandler("main.db").passwordHash(user.password);

        String query = "INSERT INTO Users VALUES " +
                "('" + user.user.getUsername()
                + "', '" + user.user.getFirstName()
                + "', '" + user.user.getLastName()
                + "', '" + user.user.getAuthenticationLevel().toString()
                + "', '" + hash
                + "')";

        try {
            assert statement != null;
            statement.execute(query);
            if (user.user.getJobs() != null) {
                for (Request.RequestType job : user.user.getJobs()) {
                    query = "INSERT INTO Jobs VALUES " +
                            "('" + user.user.getUsername()
                            + "', '" + job.toString()
                            + "')";
                    statement.execute(query);
                }
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given the username of a user, delete them from the database
     *
     * @param username the username of the user to delete
     * @return true if success
     */
    public void removeEntity(String username) throws SQLException {
        Statement statement = DatabaseHandler.getDatabaseHandler("main.db").getStatement();
        String deleteJobs = "DELETE FROM Jobs WHERE (username = '" + username + "')";
        String deleteUser = "DELETE FROM Users WHERE (username = '" + username + "')";
        try {
            assert statement != null;
            statement.executeUpdate(deleteJobs);
            int update = statement.executeUpdate(deleteUser);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the information for a given user
     *
     * @param newUser the updated user
     * @return whether the attempt to update the user was successful
     */
    public void updateEntity(UserPasswordMatch newUser) throws SQLException {
        Statement statement = DatabaseHandler.getDatabaseHandler("main.db").getStatement();
        String updateUser = "UPDATE Users " +
                "SET username = '" + newUser.user.getUsername() + "'," +
                "firstName = '" + newUser.user.getFirstName() + "'," +
                "lastName = '" + newUser.user.getLastName() + "'," +
                "authenticationLevel = '" + newUser.user.getAuthenticationLevel().toString() + "'" +
                "WHERE (username = '" + newUser.user.getUsername() + "')";
        String deleteJobs = "DELETE FROM Jobs WHERE (username = '" + newUser.user.getUsername() + "')";
        try {
            if (DatabaseHandler.getDatabaseHandler("main.db").getUserByUsername(newUser.user.getUsername()) == null) {
                return;
            } else {
                assert statement != null;
                statement.execute(updateUser);
                statement.execute(deleteJobs);
                for (Request.RequestType job : newUser.user.getJobs()) {
                    String query = "INSERT INTO Jobs VALUES " +
                            "('" + newUser.user.getUsername()
                            + "', '" + job.toString()
                            + "')";
                    statement.execute(query);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
