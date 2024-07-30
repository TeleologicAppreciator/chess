package service;

import dataaccess.*;

import java.sql.DriverManager;

public class DeleteAllService {
    private final UserDAO myUserData;
    private final AuthDAO myAuthData;
    private final GameDAO myGameData;

    public DeleteAllService(UserDAO theUserData, AuthDAO theAuthData, GameDAO theGameData) {
        myUserData = theUserData;
        myAuthData = theAuthData;
        myGameData = theGameData;
    }

    public void deleteAllData() throws DataAccessException {
        try (var connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", "root", "Mypasswordformysqlserver50!")) {
            deleteAllUsers();
            deleteAllAuth();
            deleteAllGames();
        }
        catch (Exception e) {
            throw new DataAccessException("Unable to read data");
        }

    }

    private void deleteAllUsers() throws DataAccessException {
        myUserData.deleteAll();
    }

    private void deleteAllAuth() throws DataAccessException {
        myAuthData.deleteAll();
    }

    private void deleteAllGames() throws DataAccessException {
        myGameData.deleteAll();
    }
}
