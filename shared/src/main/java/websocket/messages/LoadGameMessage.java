package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;

public class LoadGameMessage extends ServerMessage {
    ChessGame game;

    public LoadGameMessage(ChessGame theGame) {
        super(ServerMessageType.LOAD_GAME);
        game = theGame;
    }

    public ChessGame getGame() {
        return game;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
