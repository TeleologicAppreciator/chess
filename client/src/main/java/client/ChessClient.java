package client;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.JoinData;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private String visitorName = null;
    private State state = State.SIGNEDOUT;
    private AuthData authData = null;
    private GameData[] clientListedGameData = null;

    public ChessClient(String theServerUrl) {
        server = new ServerFacade(theServerUrl);
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
                default -> help();
            };
        } catch (DataAccessException e) {
            return clientErrorMessage(e.getMessage(), cmd);
        }
    }

    public String register(String... params) throws DataAccessException {
        String username = null;
        String password = null;
        String email = null;
        if (params.length > 2) {
            username = params[0];
            password = params[1];
            email = params[2];
        } else {
            throw new DataAccessException("You need a username, password, and email to register");
        }
        UserData userRegistrationData = new UserData(username, password, email);

        authData = server.registerUser(userRegistrationData);
        state = State.SIGNEDIN;
        return String.format("Successfully registered user: %s", username);
    }

    public String login(String... params) throws DataAccessException {
        String username = null;
        String password = null;
        if (params.length > 1) {
            username = params[0];
            password = params[1];
        } else {
            throw new DataAccessException("You must specify a username and password to login");
        }
        UserData userToLogin = new UserData(username, password, null);

        authData = server.loginUser(userToLogin);
        state = State.SIGNEDIN;
        return String.format("Welcome %s", userToLogin.username());
    }

    public String createGame(String... params) throws DataAccessException {
        String gameName = null;
        if (params.length > 0) {
            gameName = params[0];
        } else {
            throw new DataAccessException("You must specify the game name to make a game");
        }        //the server does not grab the gameID int value
        server.createGame(new GameData(Integer.MIN_VALUE, null, null, gameName, null), authData);

        return "Successfully created game: " + gameName;
    }

    public String joinGame(String... params) throws DataAccessException {
        Integer gameID = null;
        String playerColor = null;
        if (params.length > 1) {
            gameID = Integer.parseInt(params[0]);
            playerColor = params[1];
        } else {
            throw new DataAccessException("Input invalid please make sure you enter valid game ID from the list");
        }

        JoinData dataOfGameToJoin = new JoinData(playerColor, clientListedGameData[gameID - 1].gameID());
        try {
            server.joinGame(dataOfGameToJoin, authData);
        } catch (DataAccessException e) {
            return "Error joining game: " + dataOfGameToJoin;
        }

        return drawChessBoard();
    }

    public String observe(String... params) throws DataAccessException {
        Integer gameID = null;
        if (params.length > 0) {
            gameID = Integer.parseInt(params[0]);
        } else {
            throw new DataAccessException("Input invalid please make sure you enter valid game ID from the list");
        }
        JoinData dataOfGameToObserve = new JoinData(null, clientListedGameData[gameID - 1].gameID());

        return drawChessBoard();
    }

    public String listGames() throws DataAccessException {
        GameData[] listOfAllGames = server.getAllGames(authData);
        clientListedGameData = listOfAllGames;
        StringBuilder constructedListOFAllGames = new StringBuilder();

        for (int i = 0; i < listOfAllGames.length; i++) {
            constructedListOFAllGames.append((i+1));
            constructedListOFAllGames.append(") ");
            constructedListOFAllGames.append(listOfAllGames[i].gameName());
            constructedListOFAllGames.append(". White: ");
            constructedListOFAllGames.append(listOfAllGames[i].whiteUsername());
            constructedListOFAllGames.append(", Black: ");
            constructedListOFAllGames.append(listOfAllGames[i].blackUsername());
            constructedListOFAllGames.append("\n");
        }

        return constructedListOFAllGames.toString();


    }

    public String logout() throws DataAccessException {
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
        return """
               create <NAME> - a game
               list - games
               join <ID> [WHITE|BLACK] - a game
               observe <ID> - a game
               logout - when you are done
               quit - playing chess
               help - with possible commands
               """;
    }

    private String drawChessBoard() {
        return "drawing chess board be implemented";
    }

    public String getLoginState() {
        return state.toString();
    }

    private String clientErrorMessage(String errorMessage, String inputType) {
        //String error500 = "internal server error";

        return switch (inputType) {
            case "login" -> {
                if (errorMessage.equals("failure 401")) {
                    yield "username or password are incorrect";
                }
                yield errorMessage;
            }
            case "list", "logout" -> {
                if (errorMessage.equals("failure 401")) {
                    yield "unauthorized";
                }
                yield errorMessage;
            }
            case "create" -> switch (errorMessage) {
                case "failure 400" -> "invalid game name";
                case "failure 401" -> "unauthorized";
                default -> errorMessage;
            };
            case "join" -> switch (errorMessage) {
                case "failure 400" -> "not a valid game ID";
                case "failure 401" -> "unauthorized";
                case "failure: 403" -> "player color already taken";
                default -> errorMessage;
            };
            case "observe" -> {
                if (errorMessage.equals("failure 400")) {
                    yield "not a valid game ID";
                }
                yield errorMessage;
            }
            case "register" -> switch (errorMessage) {
                case "failure 400" -> "not a valid username or password";
                case "failure: 403" -> "username already taken";
                default -> errorMessage;
            };
            default -> errorMessage;
        };
    }
}

