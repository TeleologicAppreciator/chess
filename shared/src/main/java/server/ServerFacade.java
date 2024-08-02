package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.JoinData;
import model.UserData;
import serialization.Serializer;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String theUrl) {
        serverUrl = theUrl;
    }

    public AuthData registerUser (UserData theRegisteringUser) throws Exception {
        var path = "/user";
        return this.makeRequest("POST", path, theRegisteringUser, AuthData.class, null);
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
        record listGameResponse(GameData[] game) {
        }
        var response = makeRequest("GET", path, null, listGameResponse.class, theGetAllGamesAuth);
        return response.game();
    }

    public Integer createGame(GameData theGameName, AuthData theCreateGameAuth) throws Exception {
        var path = "/game";
        return this.makeRequest("POST", path, theGameName, Integer.class, theCreateGameAuth);
    }

    public void joinGame(JoinData theJoiningPlayer, AuthData theJoiningGameAuth) throws Exception {
        var path = "/game";
        this.makeRequest("PUT", path, theJoiningPlayer, null, theJoiningGameAuth);
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
            throw new Exception(e.getMessage());
        }


    }

    private static  void writeAuthorization(AuthData theAuthData, HttpURLConnection http) throws IOException {
        if(theAuthData != null) {
            http.addRequestProperty("Authorization", theAuthData.authToken());
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(theAuthData.authToken().getBytes());
            }
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Serializer(request).serialize();
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new DataAccessException("failure: " + status);
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
