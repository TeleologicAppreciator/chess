package websocket.messages;

import chess.ChessBoard;
import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    ChessGame game;

    public LoadGameMessage(ChessGame theGame) {
        super(ServerMessageType.LOAD_GAME);
        game = theGame;
    }

    ChessGame getGame() {
        return game;
    }
}
