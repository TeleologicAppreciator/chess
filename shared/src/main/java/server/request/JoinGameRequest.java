package server.request;

public class JoinGameRequest {
    private String playerColor;
    private int gameID;
    private String authToken = null;

    public JoinGameRequest(String thePlayerColor, int theGameID) {
        playerColor = thePlayerColor;
        gameID = theGameID;
    }

    public String playerColor() {
        return playerColor;
    }

    public int gameID() {
        return gameID;
    }

    public String authToken() {
        return authToken;
    }

    public void setAuthToken(String theAuthToken) {
        authToken = theAuthToken;
    }
}
