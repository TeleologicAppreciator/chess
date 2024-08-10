package websocket.messages;

import com.google.gson.Gson;

public class ErrorMessage extends ServerMessage {
    private String errorMessage;

    public ErrorMessage(String theError) {
        super(ServerMessageType.ERROR);
        errorMessage = theError;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
