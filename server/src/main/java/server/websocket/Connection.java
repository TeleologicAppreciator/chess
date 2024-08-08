package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String visitorName;
    public Session session;

    public Connection(String theVisitorName, Session theSession) {
        visitorName = theVisitorName;
        session = theSession;
    }

    public void send(String theMessage) throws IOException {
        session.getRemote().sendString(theMessage);
    }
}
