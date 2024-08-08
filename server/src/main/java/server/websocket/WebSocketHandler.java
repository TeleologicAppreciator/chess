package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
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
                sendMessage(theSession, new ErrorMessage("Error: unauthorized"));
                return;
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
            sendMessage(theSession, new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    private void connect(Session theSession, String theUsername, UserGameCommand theConnectCommand)
            throws IOException, DataAccessException {
        GameData gameData = gameService.getGameData().getGame(theConnectCommand.getGameID());

        if(gameData == null) {
            sendMessage(theSession, new ErrorMessage("Error: invalid game"));
            return;
        }

        //generic UserGameCommands of type connect are observe commands
        if(theConnectCommand instanceof JoinCommand) {
            ChessGame.TeamColor playerColor;

            if(((JoinCommand) theConnectCommand).getPlayerColor().equalsIgnoreCase("white")) {
                playerColor = ChessGame.TeamColor.WHITE;
            } else {
                playerColor = ChessGame.TeamColor.BLACK;
            }

            ChessGame.TeamColor validTeamColor = getTeamColor(theUsername, gameData);
            boolean joinedTheCorrectColor = validTeamColor != null && validTeamColor == playerColor;

            if(joinedTheCorrectColor) {
                connections.broadcast(theUsername, new NotificationMessage(
                        "%s has joined the game %s as %s".formatted(theUsername,
                                theConnectCommand.getGameID(), ((JoinCommand) theConnectCommand).getPlayerColor())));

                sendMessage(theSession, new LoadGameMessage(gameData.game()));
            } else {
                sendMessage(theSession, new ErrorMessage("Error: Did not join a color that is registered to you"));
            }
            //observer joined
        } else {
            connections.broadcast(theUsername, new NotificationMessage(
                    "%s has joined game %s as an observer".formatted(theUsername,
                            theConnectCommand.getGameID())));

            sendMessage(theSession, new LoadGameMessage(gameData.game()));
        }
    }

    private void makeMove(Session theSession, String theUsername, MakeMoveCommand theMoveCommand)
            throws IOException, DataAccessException {
        GameData gameData = gameService.getGameData().getGame(theMoveCommand.getGameID());
        ChessGame game = gameData.game();

        ChessGame.TeamColor userMoveColor = getTeamColor(theUsername, gameData);

        if(userMoveColor == null) {
            sendMessage(theSession, new ErrorMessage("Error: You are observing"));
            return;
        }

        if(!game.getTeamTurn().equals(userMoveColor)) {
            sendMessage(theSession, new ErrorMessage("Error: It is not your turn to move"));
            return;
        }

        if(game.isInCheckmate(userMoveColor) || game.isInStalemate(userMoveColor)) {
            sendMessage(theSession, new ErrorMessage("Error: The game is over."));
            return;
        }

        try {
            game.makeMove(theMoveCommand.getMove());
        } catch (InvalidMoveException e) {
            sendMessage(theSession, new ErrorMessage("Error: invalid chess move"));
            return;
        }

        ChessGame.TeamColor opponentColor;
        if(getTeamColor(theUsername, gameData) == ChessGame.TeamColor.BLACK) {
            opponentColor = ChessGame.TeamColor.WHITE;
        } else {
            opponentColor = ChessGame.TeamColor.BLACK;
        }

        connections.broadcast(theUsername, new NotificationMessage("%s playing as %s moved %s from %s to %s".formatted(
                theUsername, userMoveColor,
                game.getBoard().getPiece(theMoveCommand.getMove().getStartPosition()),
                theMoveCommand.getMove().getStartPosition().toString(),
                theMoveCommand.getMove().getEndPosition().toString())));

        NotificationMessage notificationMessage = null;

        if(game.isInCheckmate(opponentColor)) {
            notificationMessage = new NotificationMessage(
                    "Checkmate, %s is the winner!".formatted(userMoveColor.toString()));
        }

        if(game.isInStalemate(opponentColor)) {
            notificationMessage = new NotificationMessage("Stalemate! The game ends in a draw!");
        }

        if(game.isInCheck(opponentColor)) {
            notificationMessage = new NotificationMessage("%s is in check!".formatted(opponentColor.toString()));
        }

        gameService.getGameData().updateLiveGame(gameData);

        LoadGameMessage loadGameMessage = new LoadGameMessage(gameData.game());
        connections.broadcast(theUsername, loadGameMessage);
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
        if(theMessage instanceof ErrorMessage) {
            System.out.println(((ErrorMessage) theMessage).getErrorMessage());
        }
        theSession.getRemote().sendString(new Gson().toJson(theMessage));
    }

    private ChessGame.TeamColor getTeamColor(String theUsername, GameData theGameData) {
        if (theUsername.equals(theGameData.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        }

        if (theUsername.equals(theGameData.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        }

        return null;
    }
}
