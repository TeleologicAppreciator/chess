package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.DriverManager;
import java.sql.ResultSet;

public class MySqlAuthDAO extends MySqlDataAccess implements AuthDAO {
    public MySqlAuthDAO() throws DataAccessException {
        super();
    }

    public void createAuth(AuthData theUserData) throws DataAccessException {
        try (var connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", "root", "Mypasswordformysqlserver50!")) {
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";

            if (isUsernameValid(theUserData.username())) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.setString(1, theUserData.authToken());
                    preparedStatement.setString(2, theUserData.username());

                    preparedStatement.executeUpdate();
                } catch (Exception e) {
                    throw new DataAccessException("Unable to read data");
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
        }
    }

    public AuthData getAuth(String theAuthToken) throws DataAccessException {
        try (var connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", "root", "Mypasswordformysqlserver50!")) {
            var statement = "SELECT authToken FROM auth WHERE authToken = ?";

            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, theAuthToken);

                ResultSet resultSet = preparedStatement.executeQuery();

                String authTokenIsTheSame = null;
                while (resultSet.next()) {
                    authTokenIsTheSame = resultSet.getString("authToken");
                    if (authTokenIsTheSame.equals(theAuthToken)) {
                        String username = resultSet.getString("username");
                        return new AuthData(authTokenIsTheSame, username);
                    }
                }

                throw new DataAccessException("Auth token not found");
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
        }
    }

    public void deleteAuth(AuthData theUserData) throws DataAccessException {
        try (var connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", "root", "Mypasswordformysqlserver50!")) {
            var statement = "DELETE FROM auth WHERE authToken = ?";

            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, theUserData.authToken());

                ResultSet resultSet = preparedStatement.executeQuery();
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
        }
    }

    public int size() throws DataAccessException {
        try (var connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", "root", "Mypasswordformysqlserver50!")) {
            var preparedStatement = connection.prepareStatement("SELECT authToken FROM auth");
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
        try (var connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", "root", "Mypasswordformysqlserver50!")) {
            var preparedStatement = connection.prepareStatement("DROP TABLE auth");
            preparedStatement.executeQuery();
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
        }
    }
}
