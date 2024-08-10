package client.websocket;

import com.google.gson.Gson;
import websocket.messages.*;

import javax.websocket.*;
import java.net.URI;

public class WebSocketCommunicator extends Endpoint {
    private Session session;

    public WebSocketCommunicator(String url) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, socketURI);

            session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String theMessage) {
                    ServerMessage serverMessage = new Gson().fromJson(theMessage, ServerMessage.class);

                    switch(serverMessage.getServerMessageType()) {
                        case NOTIFICATION -> notifyClient((NotificationMessage) serverMessage);
                        case ERROR -> notifyClient((ErrorMessage) serverMessage);
                        case LOAD_GAME -> load((LoadGameMessage) serverMessage);
                    }
                }
            });
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public void onOpen(Session session, EndpointConfig config) {}

    private void notifyClient(ServerMessageWithString theNotificationOrError) {
        System.out.println(theNotificationOrError.getMessage());
    }

    private void load(LoadGameMessage theGameToLoad) {}

    public void sendMessage(String theMessage) {
        session.getAsyncRemote().sendText(theMessage);
    }
}
