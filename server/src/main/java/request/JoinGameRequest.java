package request;

import chess.ChessGame;

public class JoinGameRequest {
    private String playerColor;
    private int gameID;
    private String authToken;

    public JoinGameRequest(String thePlayerColor, int theGameID, String theAuthToken) {
        playerColor = thePlayerColor;
        gameID = theGameID;
        authToken = theAuthToken;
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
