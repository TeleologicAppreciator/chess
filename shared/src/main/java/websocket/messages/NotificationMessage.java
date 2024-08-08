package websocket.messages;

public class NotificationMessage extends ServerMessage {
    private String notificationMessage;

    public NotificationMessage(String theNotificationMessage) {
        super(ServerMessageType.NOTIFICATION);
        notificationMessage = theNotificationMessage;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }
}
