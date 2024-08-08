package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    ChessMove move;

    public MakeMoveCommand(int theGameID, String authToken, ChessMove theMove) {
        super(CommandType.MAKE_MOVE, authToken, theGameID);
        move = theMove;
    }

    public ChessMove getMove() {
        return move;
    }
}
