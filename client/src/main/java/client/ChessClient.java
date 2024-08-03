package client;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.ServerFacade;

public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private String visitorName = null;
    private State state = State.SIGNEDOUT;
    private AuthData authData = null;

    public ChessClient(String theServerUrl) {
        server = new ServerFacade(theServerUrl);
        serverUrl = theServerUrl;
    }

    public String register(String theUsername, String thePassword, String theEmail) throws DataAccessException {
        state = State.SIGNEDIN;
        UserData userRegistrationData = new UserData(theUsername, thePassword, theEmail);

        authData = server.registerUser(userRegistrationData);
        return String.format("Successfully registered user %s", userRegistrationData);
    }

    public String login(String theUsername, String thePassword) throws DataAccessException {
            state = State.SIGNEDIN;
            UserData userToLogin = new UserData(theUsername, thePassword, null);

            authData = server.loginUser(userToLogin);
            return String.format("Welcome %s", userToLogin.username());
    }

    public String list() throws DataAccessException {
        GameData[] listOfAllGames = server.getAllGames(authData);
        StringBuilder constructedListOFAllGames = new StringBuilder();

        for (int i = 0; i < listOfAllGames.length; i++) {
            constructedListOFAllGames.append((i+1));
            constructedListOFAllGames.append(listOfAllGames[i].);
        }
    }

    public void logout() throws DataAccessException {
        state = State.SIGNEDOUT;
        authData = null;
    }

    public void quit() {
        System.exit(0);
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
}
