package websocket.commands;

import chess.ChessGame;

public class JoinCommand extends UserGameCommand {
    private final ChessGame.TeamColor playerColor;

    public JoinCommand(int theGameID, String theAuthToken, ChessGame.TeamColor thePlayerColor) {
        super(UserGameCommand.CommandType.CONNECT, theAuthToken, theGameID);
        playerColor = thePlayerColor;
    }
}
