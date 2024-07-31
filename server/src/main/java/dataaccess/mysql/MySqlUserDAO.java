package dataaccess.mysql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlUserDAO extends MySqlDataAccess implements UserDAO {

    public MySqlUserDAO() throws DataAccessException {
        super();
    }

    public void createUser(UserData theUserData) throws DataAccessException {
        UserData seeIfUserIsAlreadyCreated = null;

        try {
            seeIfUserIsAlreadyCreated = getUser(theUserData.username(), theUserData.password());
        } catch (Exception e) {
            if(!e.getMessage().equals("User not found")) {
                throw new DataAccessException(e.getMessage());
            }
        }

        if(seeIfUserIsAlreadyCreated != null) {
            throw new DataAccessException("User already exists");
        }

        try (var connection = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";

            if (isUsernameValid(theUserData.username()) && isPasswordValid(theUserData.password())
                    && isEmailValid(theUserData.email())) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.setString(1, theUserData.username());
                    preparedStatement.setString(2, theUserData.password());
                    preparedStatement.setString(3, theUserData.email());

                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
        }
    }

    public UserData getUser(String theUsername, String thePassword) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username = ?";

            if (isUsernameValid(theUsername)) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.setString(1, theUsername);

                    ResultSet resultSet = preparedStatement.executeQuery();

                    String theUsernameIsTheSame = null;
                    while (resultSet.next()) {
                        theUsernameIsTheSame = resultSet.getString("username");
                        if (theUsernameIsTheSame.equals(theUsername)) {
                            String password = resultSet.getString("password");

                            if (!password.equals(thePassword)) {
                                throw new DataAccessException("Wrong password");
                            }

                            String email = resultSet.getString("email");
                            return new UserData(theUsernameIsTheSame, password, email);
                        }
                    }

                    throw new DataAccessException("User not found");
                } catch (Exception e) {
                    throw new DataAccessException("Unable to read data");
                }
            }

            throw new DataAccessException("Username and password are required");
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
            connection.close();

            return size;
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
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

    private boolean isEmailValid(String email) {
        return email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
    }
}
