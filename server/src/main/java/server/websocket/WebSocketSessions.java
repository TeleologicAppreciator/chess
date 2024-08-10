package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {
    //map of session to gameID
    private final Map<Session, Set<Integer>> sessionMap;

    public WebSocketSessions() {
        this.sessionMap = new ConcurrentHashMap<>();
    }

    public synchronized void startup(Session session) {
        sessionMap.put(session, new HashSet<>());
    }

    public synchronized void addSession(Session theSession, Integer theGameID) {
        var session = sessionMap.get(theSession);

        sessionMap.get(theSession).add(theGameID);
    }

    public synchronized void removeSessionFromGame(Session theSession, Integer theGameID) {
        sessionMap.get(theSession).remove(theGameID);
    }

    public synchronized void removeSessionFromWebSocket(Session theSession) {
        sessionMap.remove(theSession);
    }

    public synchronized void broadcast(Session excludeUserWhoMadeAction, ServerMessage theNotification, Integer affectedGameID)
            throws IOException {
        var removeList = new ArrayList<Session>();
        for (var c : sessionMap.keySet()) {
            if (c.isOpen() && sessionMap.get(c).contains(affectedGameID)) {
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

    public synchronized void sendMessageIfNotSource(Session theSession, Session theSessionToCompare, ServerMessage theNotification)
            throws IOException {

        if (!theSession.equals(theSessionToCompare)) {
            send(theSession, theNotification.toString());
        }
    }

    public synchronized void send(Session theSession, String theMessageAlreadyInJson) throws IOException {
        theSession.getRemote().sendString(theMessageAlreadyInJson);
    }
}