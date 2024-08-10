package websocket.messages;

import com.google.gson.Gson;

public class NotificationMessage extends ServerMessage{

    String message;

    public NotificationMessage(String theNotification) {
        super(ServerMessageType.NOTIFICATION);
        message = theNotification;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
