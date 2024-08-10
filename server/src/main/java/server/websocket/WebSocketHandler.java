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

@WebSocket
public class WebSocketHandler {
    private final WebSocketSessions sessions = new WebSocketSessions();
    private final AuthService authService;
    private final JoinGameService gameService;

    public WebSocketHandler(AuthService theAuthService, JoinGameService theGameService) {
        authService = theAuthService;
        gameService = theGameService;
    }

    @OnWebSocketConnect
    public void onConnect(Session theSession) {
        sessions.startup(theSession);
    }

    @OnWebSocketClose
    public void onClose(Session theSession, int theStatusCode, String theReason) {
        sessions.removeSessionFromWebSocket(theSession);
    }

    @OnWebSocketMessage
    public void onMessage(Session theSession, String theMessage) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(theMessage, UserGameCommand.class);

            AuthData usernameContainer = authService.getAuthData().getAuth(command.getAuthToken());
            if(usernameContainer == null) {
                sendOnlyToUser(theSession, new ErrorMessage("Error: unauthorized"));
                return;
            }
            String username = usernameContainer.username();

            switch (command.getCommandType()) {
                case CONNECT -> join(theSession, username, command);
                case MAKE_MOVE -> makeMove(theSession, username, theMessage);
                case LEAVE -> leaveGame(theSession, username, command);
                case RESIGN -> resign(theSession, username, command);
            }
        } catch (DataAccessException e) {
            sendOnlyToUser(theSession, new ErrorMessage("Error: unauthorized"));
        } catch (Exception e) {
            var ex = e.getMessage();
            sendOnlyToUser(theSession, new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    private void join(Session theSession, String theUsername, UserGameCommand theConnectCommand)
            throws DataAccessException, IOException {

        GameData gameData = gameService.getGameData().getGame(theConnectCommand.getGameID());

        ChessGame.TeamColor playerColor = getTeamColorFromUsername(gameData, theUsername);

        if(playerColor == null) {
            sessions.broadcast(theSession, new NotificationMessage(
                    "%s has joined game %s as an observer".formatted(theUsername,
                            theConnectCommand.getGameID())), theConnectCommand.getGameID());

        } else {
            if(playerColor == ChessGame.TeamColor.BLACK) {
                sessions.broadcast(theSession, new NotificationMessage(
                        "%s has joined the game as black".formatted(theUsername)), theConnectCommand.getGameID());
            } else {
                sessions.broadcast(theSession, new NotificationMessage(
                        "%s has joined the white".formatted(theUsername)), theConnectCommand.getGameID());
            }
        }

        LoadGameMessage loadGame = new LoadGameMessage(gameData.game());

        sendOnlyToUser(theSession, loadGame);

        sessions.addSession(theSession, theConnectCommand.getGameID());
    }

    private ChessGame.TeamColor getTeamColorFromUsername(GameData gameData, String theUsername) {
        if(gameData.whiteUsername() != null && gameData.whiteUsername().equals(theUsername)) {
            return ChessGame.TeamColor.WHITE;
        } else if (gameData.blackUsername() != null && gameData.blackUsername().equals(theUsername)) {
            return ChessGame.TeamColor.BLACK;
        } else {
            return null;
        }
    }

    private void makeMove(Session theSession, String theUsername, String stillJson)
            throws IOException, DataAccessException {

        MakeMoveCommand moveCommand = new Gson().fromJson(stillJson, MakeMoveCommand.class);

        GameData gameData = gameService.getGameData().getGame(moveCommand.getGameID());
        ChessGame game = gameData.game();

        ChessGame.TeamColor userMoveColor = getTeamColor(theUsername, gameData);

        if(userMoveColor == null) {
            sendOnlyToUser(theSession, new ErrorMessage("Error: You are observing"));
            return;
        }

        if(game.isGameOver() || game.isInCheckmate(userMoveColor) || game.isInStalemate(userMoveColor)) {
            sendOnlyToUser(theSession, new ErrorMessage("Error: The game is over."));
            return;
        }

        if(!game.getTeamTurn().equals(userMoveColor)) {
            sendOnlyToUser(theSession, new ErrorMessage("Error: It is not your turn to move"));
            return;
        }

        try {
            game.makeMove(moveCommand.getMove());
        } catch (InvalidMoveException e) {
            sendOnlyToUser(theSession, new ErrorMessage("Error: invalid chess move"));
            return;
        }

        ChessGame.TeamColor opponentColor = getOpponentColor(userMoveColor);

        sessions.broadcast(theSession, new NotificationMessage("%s playing as %s moved %s from %s to %s".formatted(
                theUsername, userMoveColor,
                game.getBoard().getPiece(moveCommand.getMove().getEndPosition()).getFullString(),
                moveCommand.getMove().getStartPosition().toString(),
                moveCommand.getMove().getEndPosition().toString())), moveCommand.getGameID());

        NotificationMessage notificationMessage = null;

        if(game.isInCheck(opponentColor)) {
            notificationMessage = new NotificationMessage("%s is in check!".formatted(opponentColor.toString()));
        }

        if(game.isInCheckmate(opponentColor)) {
            game.setGameOver();

            notificationMessage = new NotificationMessage(
                    "Checkmate, %s is the winner!".formatted(userMoveColor.toString()));
        }

        if(game.isInStalemate(opponentColor)) {
            game.setGameOver();

            notificationMessage = new NotificationMessage("Stalemate! The game ends in a draw!");
        }

        if(notificationMessage != null) {
            sendOnlyToUser(theSession, notificationMessage);
            sessions.broadcast(theSession, notificationMessage, moveCommand.getGameID());
        }

        var debug = game.getTeamTurn();
        GameData updatedGame = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), game);

        gameService.getGameData().updateLiveGame(updatedGame);

        LoadGameMessage loadGameMessage = new LoadGameMessage(updatedGame.game());
        sendOnlyToUser(theSession, loadGameMessage);
        sessions.broadcast(theSession, loadGameMessage, moveCommand.getGameID());
    }

    private void leaveGame(Session theSession, String theUsername, UserGameCommand theLeaveCommand)
            throws DataAccessException {

        try {
            GameData gameData = gameService.getGameData().getGame(theLeaveCommand.getGameID());

            var teamColor = getTeamColor(theUsername, gameData);

            if(teamColor == ChessGame.TeamColor.WHITE) {
                gameService.getGameData().updateJoinGame("white", null, gameData);
            }

            if(teamColor == ChessGame.TeamColor.BLACK) {
                gameService.getGameData().updateJoinGame("black", null, gameData);
            }

            sessions.broadcast(theSession, new NotificationMessage("%s has left the game.".formatted(theUsername)),
                    theLeaveCommand.getGameID());

            sessions.removeSessionFromGame(theSession, theLeaveCommand.getGameID());
        } catch(IOException e) {
            try {
                sendOnlyToUser(theSession, new ErrorMessage("Error: invalid connection"));
            } catch(IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void resign(Session theSession, String theUsername, UserGameCommand theResignCommand)
            throws DataAccessException, IOException {
        GameData gameData = gameService.getGameData().getGame(theResignCommand.getGameID());
        ChessGame game = gameData.game();
        ChessGame.TeamColor userColor = getTeamColor(theUsername, gameData);

        if(userColor == null) {
            sendOnlyToUser(theSession, new ErrorMessage("Error: You are observing"));
            return;
        }

        ChessGame.TeamColor opponentColor = getOpponentColor(userColor);

        if(game.isGameOver()) {
            sendOnlyToUser(theSession, new ErrorMessage("Error: The game is over."));
            return;
        }

        game.setGameOver();
        gameService.getGameData().updateLiveGame(gameData);

        String opponentUsername = null;

        if(userColor == ChessGame.TeamColor.WHITE) {
            opponentUsername = gameData.blackUsername();
        } else {
            opponentUsername = gameData.whiteUsername();
        }

        var resignNotification = new NotificationMessage(
                "%s has resigned, %s wins!".formatted(theUsername, opponentUsername));

        sendOnlyToUser(theSession, resignNotification);
        sessions.broadcast(theSession, resignNotification, theResignCommand.getGameID());
    }

    private ChessGame.TeamColor getTeamColor(String theUsername, GameData theGameData) {
        if (theGameData.whiteUsername() != null && theUsername.equals(theGameData.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        }

        if (theGameData.blackUsername() != null && theUsername.equals(theGameData.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        }

        return null;
    }

    private ChessGame.TeamColor getOpponentColor(ChessGame.TeamColor theTeamColor) {
        if(theTeamColor.equals(ChessGame.TeamColor.BLACK)) {
            return ChessGame.TeamColor.WHITE;
        } else {
            return ChessGame.TeamColor.BLACK;
        }
    }

    private void sendOnlyToUser(Session theSession, ServerMessage theMessage) throws IOException {
        String messageConvertedToJson = theMessage.toString();
        theSession.getRemote().sendString(messageConvertedToJson);
    }
}
