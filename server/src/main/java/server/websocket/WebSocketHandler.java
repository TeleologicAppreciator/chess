package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.AuthService;
import service.JoinGameService;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final AuthService authService;
    private final JoinGameService gameService;
    private final WebSocketSessions sessions = new WebSocketSessions();

    public WebSocketHandler(AuthService theAuthService, JoinGameService theGameService) {
        authService = theAuthService;
        gameService = theGameService;
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
    public void onMessage(Session theSession, String theMessage) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(theMessage, UserGameCommand.class);

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
            sendMessage(theSession, new ErrorMessage("Error: unauthorized"));
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(theSession, new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    private void connect(Session theSession, String theUsername, UserGameCommand theConnectCommand)
            throws IOException, DataAccessException {
        GameData gameData = gameService.getGameData().getGame(theConnectCommand.getGameID());

        if(gameData == null) {
            sendMessage(theSession, new ErrorMessage("Error: invalid game"));
        }

        //generic UserGameCommands of type connect are observe commands
        if(theConnectCommand instanceof JoinCommand) {
            ChessGame.TeamColor playerColor;

            if(((JoinCommand) theConnectCommand).getPlayerColor().equalsIgnoreCase("white")) {
                playerColor = ChessGame.TeamColor.WHITE;
            } else {
                playerColor = ChessGame.TeamColor.BLACK;
            }

            boolean joinedTheCorrectColor;
            if(playerColor == ChessGame.TeamColor.WHITE) {
                joinedTheCorrectColor = gameData.whiteUsername().equals(theUsername);
            } else {
                joinedTheCorrectColor = gameData.blackUsername().equals(theUsername);
            }

            if(!joinedTheCorrectColor) {
                sendMessage(theSession, new ErrorMessage("Error: Did not join a color that is registered to you"));
            } else {
                connections.broadcast(theUsername, new NotificationMessage(
                        "%s has joined the game %s as %s".formatted(theUsername,
                                theConnectCommand.getGameID(), ((JoinCommand) theConnectCommand).getPlayerColor())));

                sendMessage(theSession, new LoadGameMessage(gameData.game().getBoard()));
            }
            //observer joined
        } else {
            connections.broadcast(theUsername, new NotificationMessage(
                    "%s has joined game %s as an observer".formatted(theUsername,
                            theConnectCommand.getGameID())));

            sendMessage(theSession, new LoadGameMessage(gameData.game().getBoard()));
        }
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

    public void sendMessage(Session theSession, ServerMessage theMessage) throws IOException {
        theSession.getRemote().sendString(new Gson().toJson(theMessage));
    }
}
