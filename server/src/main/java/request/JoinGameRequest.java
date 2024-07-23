package request;

import chess.ChessGame;

public class JoinGameRequest {
    private String playerColor;
    private int gameID;
    private String authToken;
    private ChessGame.TeamColor teamColorOfJoiningPlayer = null;

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

    public ChessGame.TeamColor teamColorOfJoiningPlayer() {
        if (teamColorOfJoiningPlayer == null) {
            setPlayerColor(playerColor);
        }

        return teamColorOfJoiningPlayer;
    }

    public void setAuthToken(String theAuthToken) {
        authToken = theAuthToken;
    }

    public void setPlayerColor(String thePlayerColor) {
        if(thePlayerColor.equalsIgnoreCase("white")) {
            teamColorOfJoiningPlayer = ChessGame.TeamColor.WHITE;
        } else {
            teamColorOfJoiningPlayer = ChessGame.TeamColor.BLACK;
        }
    }
}
