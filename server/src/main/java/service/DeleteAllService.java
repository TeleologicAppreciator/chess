package service;

import dataaccess.*;

public class DeleteAllService {
    private final UserDAO myUserData;
    private final AuthDAO myAuthData;
    private final GameDAO myGameData;

    public DeleteAllService(UserDAO theUserData, AuthDAO theAuthData, GameDAO theGameData) {
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
