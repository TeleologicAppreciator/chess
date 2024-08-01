package service;

import dataaccess.*;
import dataaccess.mysql.DatabaseManager;

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
        deleteAllUsers();
        deleteAllAuth();
        deleteAllGames();
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
