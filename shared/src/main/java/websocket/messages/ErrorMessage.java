package websocket.messages;

public class ErrorMessage extends ServerMessage {
    private String errorMessage;

    public ErrorMessage(final String theErrorMessage) {
        super(ServerMessageType.ERROR);
        errorMessage = theErrorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
