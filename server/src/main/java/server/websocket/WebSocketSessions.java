package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WebSocketSessions {
    //map of session to gameID
    private final Map<Session, Integer> sessionMap;

    public WebSocketSessions() {
        this.sessionMap = new HashMap<>();
    }

    public void addSession(Session theSession, Integer theGameID) {
        sessionMap.put(theSession, theGameID);
    }

    public void updateSession(Session theSession, Integer theGameID) {
        sessionMap.replace(theSession, theGameID);
    }

    public void removeSession(Integer theGameID) {
        sessionMap.remove(theGameID);
    }

    public void broadcast(Session excludeUserWhoMadeAction, ServerMessage theNotification, int affectedGameID)
            throws IOException {
        var removeList = new ArrayList<Session>();
        for (var c : sessionMap.keySet()) {
            if (c.isOpen()) {
                sendMessageIfNotSource(c, excludeUserWhoMadeAction, theNotification);
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            sessionMap.remove(c);
        }
    }

    public void sendMessageIfNotSource(Session theSession, Session theSessionToCompare, ServerMessage theNotification)
            throws IOException {

        if (!theSession.equals(theSessionToCompare)) {
            send(theSession, theNotification.toString());
        }
    }

    public void send(Session theSession, String theMessageAlreadyInJson) throws IOException {
        theSession.getRemote().sendString(theMessageAlreadyInJson);
    }
}