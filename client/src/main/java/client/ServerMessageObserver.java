package client;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public interface ServerMessageObserver {
    public void notifyClient(NotificationMessage theMessage);
    public void notifyError(ErrorMessage theMessage);
    public void loadGame(LoadGameMessage theMessage);
}
