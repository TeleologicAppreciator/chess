package client.websocket;

import client.ServerMessageObserver;
import com.google.gson.Gson;
import websocket.messages.*;

import javax.websocket.*;
import java.net.URI;

public class WebSocketCommunicator extends Endpoint {
    private final Session session;
    private final ServerMessageObserver observer;

    public WebSocketCommunicator(String url, ServerMessageObserver theObserver) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            observer = theObserver;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, socketURI);

            session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String theMessage) {
                    try {
                        ServerMessage serverMessage = new Gson().fromJson(theMessage, ServerMessage.class);
                        switch (serverMessage.getServerMessageType()) {
                            case NOTIFICATION -> observer.notifyClient(
                                    new Gson().fromJson(theMessage, NotificationMessage.class));
                            case ERROR -> observer.notifyError(new Gson().fromJson(theMessage, ErrorMessage.class));
                            case LOAD_GAME -> observer.loadGame(new Gson().fromJson(theMessage, LoadGameMessage.class));
                        }
                    } catch (Exception e) {
                        observer.notifyError(new ErrorMessage(e.getMessage()));
                    }
                }
            });
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public void onOpen(Session session, EndpointConfig config) {}

    public void sendMessage(String theMessage) {
        session.getAsyncRemote().sendText(theMessage);
    }
}
