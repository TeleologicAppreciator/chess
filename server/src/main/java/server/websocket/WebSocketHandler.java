package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.
            String username = getUsername(command.getAuthString());

            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (UnauthorizedException e) {
            // Serializes and sends the error message
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    private void connect(Session theSession, String theUsername, ConnectCommand theConnectCommand) {

    }

    private void makeMove(Session theSession, String theUsername, MakeMoveCommand theMoveCommand) {

    }

    private void leaveGame(Session theSession, String theUsername, LeaveGameCommad theLeaveCommand) {

    }

    private void resign(Session theSession, String theUsername, ResignCommand theResignCommand) {

    }
}
