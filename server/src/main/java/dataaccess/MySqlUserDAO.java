package dataaccess;

import model.UserData;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlUserDAO extends MySqlDataAccess implements UserDAO {

    public MySqlUserDAO() throws DataAccessException {
        super();
    }

    public void createUser(UserData theUserData) throws DataAccessException {
        try (var connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", "root", "Mypasswordformysqlserver50!")) {
            var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";

            if (isUsernameValid(theUserData.username()) && isPasswordValid(theUserData.password())
                    && isEmailValid(theUserData.email())) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.setString(1, theUserData.username());
                    preparedStatement.setString(2, theUserData.password());
                    preparedStatement.setString(3, theUserData.email());

                    preparedStatement.executeUpdate();

                    var resultSet = preparedStatement.getGeneratedKeys();
                } catch (Exception e) {
                    throw new DataAccessException("Unable to read data");
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
        }
    }

    public UserData getUser(String theUsername, String thePassword) throws DataAccessException {
        try (var connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", "root", "Mypasswordformysqlserver50!")) {
            var statement = "SELECT username FROM user WHERE username = ?";

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
            throw new DataAccessException("Unable to read data");
        }
    }

    public void deleteAll() throws DataAccessException {
        try (var connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", "root", "Mypasswordformysqlserver50!")) {
            var preparedStatement = connection.prepareStatement("DROP TABLE user");
            preparedStatement.executeQuery();
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
        }
    }

    public int size() throws DataAccessException {
        try (var connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", "root", "Mypasswordformysqlserver50!")) {
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

    private boolean isUsernameValid(String username) {
        return username.matches("[a-zA-Z0-9!?]+");
    }

    private boolean isPasswordValid(String password) {
        return isUsernameValid(password);
    }

    private boolean isEmailValid(String email) {
        return email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$");
    }
}
