package service;

import dataaccess.*;

public class DeleteService {
    private final UserDAO myUserData;
    private final AuthDAO myAuthData;
    private final GameDAO myGameData;

    public DeleteService(UserDAO theUserData, AuthDAO theAuthData, GameDAO theGameData) {
        myUserData = theUserData;
        myAuthData = theAuthData;
        myGameData = theGameData;
    }

    public void deleteAllData() {
        deleteAllUsers();
        deleteAllAuth();
        deleteAllGames();
    }

    private void deleteAllUsers() {
        myUserData.deleteAll();
    }

    private void deleteAllAuth() {
        myAuthData.deleteAll();
    }

    private void deleteAllGames() {
        myGameData.deleteAll();
    }
}
