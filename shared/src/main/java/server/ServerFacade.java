package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.JoinData;
import model.UserData;
import server.request.CreateGameRequest;
import server.request.JoinGameRequest;
import server.request.RegisterRequest;
import server.result.CreateGameResult;
import server.result.GetAllGameResult;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String theUrl) {
        serverUrl = theUrl;
    }

    public AuthData registerUser (UserData theRegisteringUser) throws Exception {
        var path = "/user";
        RegisterRequest registeringUser = new RegisterRequest(
                theRegisteringUser.username(), theRegisteringUser.password(), theRegisteringUser.email());

        return this.makeRequest("POST", path, registeringUser, AuthData.class, null);
    }

    public AuthData loginUser (UserData theLoggingInUser) throws Exception {
        var path = "/session";

        return this.makeRequest("POST", path, theLoggingInUser, AuthData.class, null);
    }

    public void logoutUser (AuthData theLogoutAuth) throws Exception {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, theLogoutAuth);
    }

    public GameData[] getAllGames(AuthData theGetAllGamesAuth) throws Exception {
        var path = "/game";
        var response = makeRequest("GET", path, null, GetAllGameResult.class, theGetAllGamesAuth);
        return response.games();
    }

    public Integer createGame(GameData theGameName, AuthData theCreateGameAuth) throws Exception {
        var path = "/game";
        CreateGameRequest newRequest = new CreateGameRequest(theGameName.gameName());
        var result = this.makeRequest("POST", path, newRequest, CreateGameResult.class, theCreateGameAuth);
        return result.getGameID();
    }

    public void joinGame(JoinData theJoiningPlayer, AuthData theJoiningGameAuth) throws Exception {
        var path = "/game";
        JoinGameRequest joinRequest = new JoinGameRequest(theJoiningPlayer.playerColor(), theJoiningPlayer.gameID());
        this.makeRequest("PUT", path, joinRequest, null, theJoiningGameAuth);
    }

    public void deleteAll() throws Exception {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T, U> U makeRequest(String theMethod, String thePath, T theRequest, Class<U> theResponseClass,
                              AuthData theAuthData) throws Exception {
        try {
            URL url = (new URI(serverUrl + thePath)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(theMethod);
            http.setDoOutput(true);

            writeAuthorization(theAuthData, http);
            writeBody(theRequest, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, theResponseClass);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private static  void writeAuthorization(AuthData theAuthData, HttpURLConnection http) throws IOException {
        if(theAuthData != null) {
            http.addRequestProperty("Authorization", theAuthData.authToken());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new Exception("failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
