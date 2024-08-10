package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import model.JoinData;
import model.UserData;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.Arrays;

public class ChessClient implements ServerMessageObserver {
    private final ServerFacade server;
    private final String serverUrl;
    private String visitorName = null;
    private State state = State.SIGNEDOUT;
    private AuthData authData = null;
    private GameData[] clientListedGameData = null;

    private static ChessBoardDrawer chessBoardDrawer = null;
    int gameIDSaved;

    public ChessClient(String theServerUrl) {
        server = new ServerFacade(theServerUrl, this);
        serverUrl = theServerUrl;
    }

    public String eval(String input) {
        String cmd = null;
        try {
            var tokens = input.toLowerCase().split(" ");
            cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "list" -> listGames();
                case "create" -> createGame(params);
                case "join" -> joinGame(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "register" -> register(params);
                case "quit" -> "quit";
                case "redraw" -> redrawChessBoard();
                case "leave" -> leaveGame();
                case "move" -> makeMove(params);
                case "resign" -> resign();
                case "legal" -> drawLegalMoves(params);
                default -> help();
            };
        } catch (Exception e) {
            return clientErrorMessage(e.getMessage(), cmd);
        }
    }

    public String register(String... params) throws Exception {
        if(state == State.GAMEPLAY) {
            return "Please leave your current game before registering a new user";
        }
        String username = null;
        String password = null;
        String email = null;
        if (params.length > 2) {
            username = params[0];
            password = params[1];
            email = params[2];
        } else {
            throw new Exception("You need a username, password, and email to register");
        }
        UserData userRegistrationData = new UserData(username, password, email);

        authData = server.registerUser(userRegistrationData);
        state = State.SIGNEDIN;
        return String.format("Successfully registered user: %s", username);
    }

    public String login(String... params) throws Exception {
        if(state == State.GAMEPLAY) {
            return "You are already logged in and playing a game";
        }
        String username = null;
        String password = null;
        if (params.length > 1) {
            username = params[0];
            password = params[1];
        } else {
            throw new Exception("You must specify a username and password to login");
        }
        UserData userToLogin = new UserData(username, password, null);

        authData = server.loginUser(userToLogin);
        state = State.SIGNEDIN;
        return String.format("Welcome %s", userToLogin.username());
    }

    public String createGame(String... params) throws Exception {
        if(state == State.GAMEPLAY) {
            return "Please leave your current game before creating a new game";
        }
        String gameName = null;
        if (params.length > 0) {
            gameName = params[0];
        } else {
            throw new Exception("You must specify the game name to make a game");
        }        //the server does not grab the gameID int value
        server.createGame(new GameData(0, null, null, gameName, null), authData);

        return ("Successfully created game: " + gameName);
    }

    public String joinGame(String... params) throws Exception {
        if(state == State.GAMEPLAY) {
            return "Please leave your current game before joining a new one";
        }
        if (clientListedGameData == null) {
            throw new Exception("You must list games before joining");
        }
        Integer clientGameID = null;
        String playerColor = null;
        if (params.length > 1) {
            clientGameID = Integer.parseInt(params[0]);
            playerColor = params[1];
        } else {
            throw new Exception("Input invalid please make sure you enter valid game ID from the list");
        }
        if(playerColor == null || (!playerColor.equalsIgnoreCase("white") && !playerColor.equalsIgnoreCase("black"))) {
            throw new Exception("You must specify a valid color to join a game");
        }
        if(clientGameID > clientListedGameData.length) {
            throw new Exception("not a valid game ID");
        }
        var game = clientListedGameData[clientGameID - 1];

        var serverGameID = game.gameID();

        if(playerColor.equalsIgnoreCase("white")) {
            if(game.whiteUsername() != null && !game.whiteUsername().equals(authData.username())) {
                return "Error: color already taken";
            }
        } else if(playerColor.equalsIgnoreCase("black")) {
            if(game.blackUsername() != null && !game.blackUsername().equals(authData.username())) {
                return "Error: color already taken";
            }
        }

        //add ourselves to the game if we are not already registered
        if((playerColor.equalsIgnoreCase("white") && game.whiteUsername() == null)
                || (playerColor.equalsIgnoreCase("black") && game.blackUsername() == null)) {

            JoinData dataOfGameToJoin = new JoinData(playerColor, serverGameID);

            executeHTTPJoin(dataOfGameToJoin);
        }

        server.watchWebSocket();
        server.joinPlayer(serverGameID, authData, playerColor);

        ChessGame currentSessionGame = clientListedGameData[clientGameID - 1].game();

        boolean isBlackPerspective = playerColor.equalsIgnoreCase("black");
        chessBoardDrawer = new ChessBoardDrawer(currentSessionGame, isBlackPerspective);
        gameIDSaved = serverGameID;

        state = State.GAMEPLAY;
        return "";
    }

    private void executeHTTPJoin(JoinData theDataOfGameToJoin) {
        try {
            server.joinGame(theDataOfGameToJoin, authData);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String observe(String... params) throws Exception {
        if(state == State.GAMEPLAY) {
            return "Please leave your current game before observing a new one";
        }

        if (clientListedGameData == null) {
            throw new Exception("You must list games before joining");
        }

        Integer gameID = null;
        if (params.length > 0) {
            gameID = Integer.parseInt(params[0]);
        } else {
            throw new Exception("Input invalid please make sure you enter valid game ID from the list");
        }

        if(gameID > clientListedGameData.length) {
            throw new Exception("not a valid game ID");
        }

        int serverGameID = clientListedGameData[gameID - 1].gameID();

        server.watchWebSocket();
        server.joinObserver(serverGameID, authData);

        chessBoardDrawer = new ChessBoardDrawer(clientListedGameData[gameID - 1].game(), true);
        gameIDSaved = serverGameID;

        state = State.GAMEPLAY;
        return "successfully observing game";
    }

    private String redrawChessBoard() {
        if(state != State.GAMEPLAY) {
            return "Can only draw a board of a game you are currently viewing";
        }
        if(chessBoardDrawer == null) {
            return "Can only draw a board of a game in session";
        }
        chessBoardDrawer.drawChessBoard();
        return "here is the chess board";
    }

    public String makeMove(String... params) throws Exception {
        if(state != State.GAMEPLAY) {
            return "You cannot make a move in a game you aren't playing";
        }
        if(params.length < 2) {
            throw new Exception("You must specify a start and end position to move");
        }
        ChessPosition startPosition = getChessPositionFromInput(params[0]);
        ChessPosition endPosition = getChessPositionFromInput(params[1]);

        if(startPosition == null || endPosition == null) {
            return "Must specify a valid start and end position to move";
        }

        ChessPiece.PieceType promotionType = getPieceType(params);

        ChessMove moveToPushToServer = new ChessMove(startPosition, endPosition, promotionType);

        server.makeMove(gameIDSaved, authData, moveToPushToServer);
        return "";
    }

    private static ChessPiece.PieceType getPieceType(String[] params) {
        ChessPiece.PieceType promotionType = null;

        if(params.length > 2) {
            String promotionString = params[2];
            if(promotionString.equalsIgnoreCase("queen")) {
                promotionType = ChessPiece.PieceType.QUEEN;
            } else if(promotionString.equalsIgnoreCase("rook")) {
                promotionType = ChessPiece.PieceType.ROOK;
            } else if(promotionString.equalsIgnoreCase("knight")) {
                promotionType = ChessPiece.PieceType.KNIGHT;
            } else if(promotionString.equalsIgnoreCase("bishop")) {
                promotionType = ChessPiece.PieceType.BISHOP;
            }
        }
        return promotionType;
    }

    private ChessPosition getChessPositionFromInput(String thePosition) {
        if(!isValidChessPositionInput(thePosition)) {
            return null;
        }

        String firstChar = thePosition.substring(0, 1);
        String secondChar = thePosition.substring(1, 2);

        int row = Integer.parseInt(secondChar);
        int col = convertAlphabetPositionToNumber(firstChar);
        return new ChessPosition(row, col);
    }

    private boolean isValidChessPositionInput(String theInput) {
        if(theInput.length() < 2) {
            return false;
        }

        boolean valid = false;
        for (char c = 'a'; c <= 'h'; c++) {
            if(theInput.charAt(0) == c) {
                valid = true;
            }
        }

        if(!valid) {
            return false;
        }
        for(int i = 0; i <= 8; i++) {
            if(theInput.substring(1, 2).equals(Integer.toString(i))) {
                valid = true;
            }
        }
        return valid;
    }

    private int convertAlphabetPositionToNumber(String theAlphaBetPosition) {
        if(theAlphaBetPosition.equals("a")){
            return 1;
        }
        if(theAlphaBetPosition.equals("b")){
            return 2;
        }
        if(theAlphaBetPosition.equals("c")){
            return 3;
        }
        if(theAlphaBetPosition.equals("d")){
            return 4;
        }
        if(theAlphaBetPosition.equals("e")){
            return 5;
        }
        if(theAlphaBetPosition.equals("f")){
            return 6;
        }
        if(theAlphaBetPosition.equals("g")){
            return 7;
        }
        return 8;
    }

    public String listGames() throws Exception {
        GameData[] listOfAllGames = server.getAllGames(authData);
        clientListedGameData = listOfAllGames;
        StringBuilder constructedListOFAllGames = new StringBuilder();

        for (int i = 0; i < listOfAllGames.length; i++) {
            constructedListOFAllGames.append((i+1));
            constructedListOFAllGames.append(") ");
            constructedListOFAllGames.append("Game name: ");
            constructedListOFAllGames.append(listOfAllGames[i].gameName());
            constructedListOFAllGames.append(". White player: ");
            if(listOfAllGames[i].whiteUsername() == null) {
                constructedListOFAllGames.append("-empty-");
            } else {
                constructedListOFAllGames.append(listOfAllGames[i].whiteUsername());
            }
            constructedListOFAllGames.append("  Black player: ");
            if(listOfAllGames[i].blackUsername() == null) {
                constructedListOFAllGames.append("-empty-");
            } else {
                constructedListOFAllGames.append(listOfAllGames[i].blackUsername());
            }
            constructedListOFAllGames.append("\n");
        }

        String result = constructedListOFAllGames.toString();
        if(result.isEmpty()) {
            return "no games currently created";
        }
        return result;
    }

    public String drawLegalMoves(String... params) {
        if(state != State.GAMEPLAY) {
            return "You must be viewing a game to display legal moves";
        }

        if(params.length < 1) {
            return "You must input the position of the piece you want to view valid moves of";
        }
        ChessPosition positionOfPieceToDrawValidMoves = getChessPositionFromInput(params[0]);

        if(positionOfPieceToDrawValidMoves == null) {
            return "You must input the position of the piece you want to view valid moves of";
        }
        chessBoardDrawer.drawValidMoves(positionOfPieceToDrawValidMoves);
        return "here are the valid moves";
    }

    public String leaveGame() {
        if(state != State.GAMEPLAY) {
            return "Cannot leave a game you aren't currently viewing";
        }

        state = State.SIGNEDIN;
        server.leave(gameIDSaved, authData);

        gameIDSaved = -1;
        return "Successfully left the game";
    }

    public String resign() {
        if(state != State.GAMEPLAY) {
            return "Cannot resign from a game you aren't currently playing";
        }

        server.resign(gameIDSaved, authData);
        return "";
    }

    public String logout() throws Exception {
        state = State.SIGNEDOUT;
        authData = null;

        return "Successfully logged out";
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        }
        else if (state == State.SIGNEDIN) {
            return """
               create <NAME> - a game
               list - games
               join <ID> [WHITE|BLACK] - a game
               observe <ID> - a game
               logout - when you are done
               quit - playing chess
               help - with possible commands
               """;
        } else {
            return """
                    redraw - the chess board
                    legal <position of chess piece> - highlights all of the moves the chess piece can do
                    move <start position of chess piece> <end position of chess piece> {pawn promotion piece type} 
                    - move the chess piece [***example***] d7 d8 queen.
                    - pawn promotion is optional, only for moves that would promote a pawn
                    resign - concede victory to opponent
                    leave - drop yourself from the game so someone else can play
                    help - with possible commands
                    """;
        }
    }

    public String getLoginState() {
        return state.toString();
    }

    private String clientErrorMessage(String errorMessage, String inputType) {
        return switch (inputType) {
            case "login" -> {
                if (errorMessage.contains("401")) {
                    yield "username or password are incorrect";
                }
                yield errorMessage;
            }
            case "list", "logout" -> {
                if (errorMessage.contains("401")) {
                    yield "unauthorized";
                }
                yield errorMessage;
            }
            case "create" -> switch (errorMessage) {
                case "failure: 400" -> "invalid game name";
                case "failure: 401" -> "unauthorized";
                default -> errorMessage;
            };
            case "join" -> switch (errorMessage) {
                case "failure: 400" -> "not a valid game ID";
                case "failure: 401" -> "unauthorized";
                case "failure: 403" -> "player color already taken";
                default -> errorMessage;
            };
            case "observe" -> {
                if (errorMessage.contains("400")) {
                    yield "not a valid game ID";
                }
                yield errorMessage;
            }
            case "register" -> switch (errorMessage) {
                case "failure: 400" -> "not a valid username or password";
                case "failure: 403" -> "username already taken";
                default -> errorMessage;
            };
            default -> errorMessage;
        };
    }

    public static ChessBoardDrawer getChessBoardDrawer() {
        return chessBoardDrawer;
    }

    public void notifyClient(NotificationMessage theNotification) {
        System.out.println("[NOTIFICATION] >>> "
                + theNotification.getMessage());
        System.out.print("[GAMEPLAY] >>> ");
    }

    public void notifyError(ErrorMessage theError) {
        System.out.println(theError.getErrorMessage());
        System.out.print("[GAMEPLAY] >>> ");
    }

    public void loadGame(LoadGameMessage theGameToLoad) {
        System.out.println();
        ChessGame game = theGameToLoad.getGame();
        ChessBoardDrawer.updateGame(game);
        ChessBoardDrawer.drawChessBoard();
        System.out.print("[GAMEPLAY] >>> ");
    }
}