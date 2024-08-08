package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import websocket.commands.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final AuthService authService;
    private final WebSocketSessions sessions = new WebSocketSessions();

    public WebSocketHandler(AuthService theAuthService) {
        authService = theAuthService;
    }

    @OnWebSocketConnect
    public void onConnect(Session theSession) {
        sessions.addSession(theSession);
    }

    @OnWebSocketClose
    public void onClose(Session theSession, int theStatusCode, String theReason) {
        sessions.removeSession(theSession);
    }

    @OnWebSocketMessage
    public void onMessage(Session theSession, String theMessage) {
        try {
            UserGameCommand command = new Gson().fromJson(theMessage, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.
            AuthData usernameContainer = authService.getAuthData().getAuth(command.getAuthToken());
            if(usernameContainer == null) {
                throw new DataAccessException("Error: unauthorized");
            }
            String username = usernameContainer.username();

            sessions.saveSession(command.getGameID(), theSession);

            switch (command.getCommandType()) {
                case CONNECT -> connect(theSession, username, command);
                case MAKE_MOVE -> makeMove(theSession, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(theSession, username, command);
                case RESIGN -> resign(theSession, username, command);
            }
        } catch (DataAccessException e) {
            // Serializes and sends the error message
            try {
                theSession.getRemote().sendString("Error: unauthorized");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                theSession.getRemote().sendString("Error: " + e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void connect(Session theSession, String theUsername, UserGameCommand theConnectCommand) {

    }

    private void makeMove(Session theSession, String theUsername, MakeMoveCommand theMoveCommand) {

    }

    private void leaveGame(Session theSession, String theUsername, UserGameCommand theLeaveCommand) {

    }

    private void resign(Session theSession, String theUsername, UserGameCommand theResignCommand) {

    }

    /**
     * Stores web socket connections (Session) for each game
     */
    private static class WebSocketSessions {

        //map of gameID to sessions participating in that game
        private final Map<Integer, Set<Session>> gameMap;

        //map of session to gameID
        private final Map<Session, Integer> sessionMap;

        private WebSocketSessions() {
            this.gameMap = new HashMap<>();
            this.sessionMap = new HashMap<>();
        }

        private void addSessionForGame(Integer gameID, Session session) {
            gameMap.get(gameID).add(session);
            sessionMap.put(session, gameID);
        }

        private boolean removeSessionFromGame(Integer theGameID, Session theSession) {
            removeSession(theSession);
            return gameMap.get(theGameID).remove(theSession);
        }

        private void removeSession(Session theSession) {
            sessionMap.remove(theSession);
        }

        private void saveSession(int theGameID, Session theSession) {
            sessionMap.replace(theSession, theGameID);
        }

        private void addSession(Session theSession) {
            sessionMap.put(theSession, 0);
        }
    }
}
