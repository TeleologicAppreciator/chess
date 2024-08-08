package websocket.messages;

import chess.ChessBoard;

public class LoadGameMessage extends ServerMessage {
    ChessBoard board;

    public LoadGameMessage(ChessBoard theBoard) {
        super(ServerMessageType.LOAD_GAME);
        board = theBoard;
    }

    ChessBoard getBoard() {
        return board;
    }
}
