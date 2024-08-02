package server;

import model.AuthData;
import model.GameData;
import model.UserData;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String theUrl) {
        serverUrl = theUrl;
    }

    public AuthData registerUser (UserData theUserData) {
        var path = "/user";
        return this.makeRequest("POST", path, theUserData, AuthData.class);
    }

    public AuthData loginUser (UserData theUserData) {
        var path = "/session";
        return this.makeRequest("POST", path, theUserData, AuthData.class);
    }

    public void lougoutUser (AuthData theAuthData) {
        var path = "/session";
        return this.makeRequest("DELETE", path, theAuthData, null);
    }

    public GameData[] getAllGames(AuthData theAuthData) {
        var path = "/game";
        return this.makeRequest("GET", path, theAuthData, GameData[].class);
    }
}
