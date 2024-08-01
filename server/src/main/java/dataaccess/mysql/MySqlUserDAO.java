package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.ResultSet;

public class MySqlUserDAO extends MySqlDataAccess implements UserDAO {

    public MySqlUserDAO() throws DataAccessException {
        super();
    }

    public void createUser(UserData theUserData) throws DataAccessException {

        try (var connection = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";

            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, theUserData.username());
                preparedStatement.setString(2, theUserData.password());
                preparedStatement.setString(3, theUserData.email());

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData getUser(String theUsername, String thePassword) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username = ?";
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.setString(1, theUsername);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        String storedUsername = resultSet.getString("username");
                        String storedPassword = resultSet.getString("password");
                        String storedEmail = resultSet.getString("email");

                        if (storedUsername.equals(theUsername)) {
                            return new UserData(storedUsername, storedPassword, storedEmail);
                        }
                    }

                    throw new DataAccessException("User not found");
                }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public int size() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var preparedStatement = connection.prepareStatement("SELECT username FROM user");
            ResultSet resultSet = preparedStatement.executeQuery();

            int size = 0;
            while (resultSet.next()) {
                size++;
            }

            return size;
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteAll() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var preparedStatement = connection.prepareStatement("DROP TABLE user");
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

        new MySqlAuthDAO();
    }
}
