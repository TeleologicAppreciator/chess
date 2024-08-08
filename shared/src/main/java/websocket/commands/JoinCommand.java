package websocket.commands;

import chess.ChessGame;

public class JoinCommand extends UserGameCommand {
    private final String playerColor;

    public JoinCommand(int theGameID, String theAuthToken, String thePlayerColor) {
        super(UserGameCommand.CommandType.CONNECT, theAuthToken, theGameID);
        playerColor = thePlayerColor;
    }

    public String getPlayerColor() {
        return playerColor;
    }
}
