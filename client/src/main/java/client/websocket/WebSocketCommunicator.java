package client.websocket;

import client.ServerMessageObserver;
import com.google.gson.Gson;
import websocket.messages.*;

import javax.websocket.*;
import java.net.URI;

public class WebSocketCommunicator extends Endpoint {
    private Session session;
    private ServerMessageObserver observer;

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

                        observer.notify(serverMessage);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        //send to error in client
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
