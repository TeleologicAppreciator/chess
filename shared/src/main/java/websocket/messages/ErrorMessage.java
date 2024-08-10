package websocket.messages;

import com.google.gson.Gson;

public class ErrorMessage extends ServerMessage {
    private String error;

    public ErrorMessage(String theError) {
        super(ServerMessageType.ERROR);
        error = theError;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
