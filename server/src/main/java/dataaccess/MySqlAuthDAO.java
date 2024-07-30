package dataaccess;

import model.AuthData;

import java.sql.DriverManager;

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

    public AuthData getAuth(String theUserName) throws DataAccessException {
        return null;
    }

    public void deleteAuth(AuthData theUserData) throws DataAccessException {

    }

    public int size() throws DataAccessException {
        return 0;
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
