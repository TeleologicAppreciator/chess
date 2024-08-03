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
    private GameData[] gameData = null;

    public ChessClient(String theServerUrl) {
        server = new ServerFacade(theServerUrl);
        serverUrl = theServerUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
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
            return e.getMessage();
        }
    }

    public String register(String... params) throws DataAccessException {
        state = State.SIGNEDIN;

        String username = null;
        String password = null;
        String email = null;
        if (params.length > 2) {
            username = params[0];
            password = params[1];
            email = params[2];
        } else {
            throw new DataAccessException("You must specify at least 3 parameters to register");
        }
        UserData userRegistrationData = new UserData(username, password, email);

        authData = server.registerUser(userRegistrationData);
        return String.format("Successfully registered user %s", userRegistrationData);
    }

    public String login(String... params) throws DataAccessException {
            state = State.SIGNEDIN;

            String username = null;
            String password = null;
            if (params.length > 1) {
                username = params[0];
                password = params[1];
            } else {
                throw new DataAccessException("You must specify at least 2 parameters to login");
            }
            UserData userToLogin = new UserData(username, password, null);

            authData = server.loginUser(userToLogin);
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
            throw new DataAccessException("Input invalid please make sure you enter valid gameID from the list");
        }

        JoinData dataOfGameToJoin = new JoinData(playerColor, gameData[gameID - 1].gameID());
        try {
            server.joinGame(dataOfGameToJoin, authData);
        } catch (DataAccessException e) {
            return "Error joining game: " + dataOfGameToJoin;
        }

        return "Successfully joined game: " + dataOfGameToJoin;
    }

    public String observe(String... params) throws DataAccessException {
        Integer gameID = null;
        if (params.length > 0) {
            gameID = Integer.parseInt(params[0]);
        } else {
            throw new DataAccessException("Input invalid please make sure you enter valid gameID from the list");
        }
        JoinData dataOfGameToObserve = new JoinData(null, gameData[gameID - 1].gameID());
        //observe functionality to be implemented in the future.
        return "functionality to be added in future update";
    }

    public String listGames() throws DataAccessException {
        GameData[] listOfAllGames = server.getAllGames(authData);
        gameData = listOfAllGames;
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
        if (state == State.SIGNEDIN) {
            return """
                    register <username> <password> <email> - to create an account
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
}

