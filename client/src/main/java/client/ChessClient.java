package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import model.JoinData;
import model.UserData;
import facade.ServerFacade;

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
        } catch (Exception e) {
            return clientErrorMessage(e.getMessage(), cmd);
        }
    }

    public String register(String... params) throws Exception {
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
        String gameName = null;
        if (params.length > 0) {
            gameName = params[0];
        } else {
            throw new Exception("You must specify the game name to make a game");
        }        //the server does not grab the gameID int value
        server.createGame(new GameData(0, null, null, gameName, null), authData);

        return "Successfully created game: " + gameName;
    }

    public String joinGame(String... params) throws Exception {
        if (clientListedGameData == null) {
            throw new Exception("You must list games before joining");
        }

        Integer gameID = null;
        String playerColor = null;
        if (params.length > 1) {
            gameID = Integer.parseInt(params[0]);
            playerColor = params[1];
        } else {
            throw new Exception("Input invalid please make sure you enter valid game ID from the list");
        }

        JoinData dataOfGameToJoin = new JoinData(playerColor, clientListedGameData[gameID - 1].gameID());
        server.joinGame(dataOfGameToJoin, authData);

        drawChessBoard(clientListedGameData[gameID - 1].game().getBoard(), false);
        drawChessBoard(clientListedGameData[gameID - 1].game().getBoard(), true);

        return "successfully joined game";
    }

    public String observe(String... params) throws Exception {
        if (clientListedGameData == null) {
            throw new Exception("You must list games before joining");
        }

        Integer gameID = null;
        if (params.length > 0) {
            gameID = Integer.parseInt(params[0]);
        } else {
            throw new Exception("Input invalid please make sure you enter valid game ID from the list");
        }
        JoinData dataOfGameToObserve = new JoinData(null, clientListedGameData[gameID - 1].gameID());

        drawChessBoard(clientListedGameData[gameID - 1].game().getBoard(), false);
        drawChessBoard(clientListedGameData[gameID - 1].game().getBoard(), true);

        return "successfully observing game";
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

    private void drawChessBoard(ChessBoard theBoard, boolean isBlackPerspective) {
        System.out.print(EscapeSequences.SET_TEXT_BOLD);

        //line 1
        drawChessTop(isBlackPerspective);
        drawChessBoardBody(theBoard, isBlackPerspective);
        drawChessTop(isBlackPerspective);

        System.out.println();
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private void drawChessBoardBody(ChessBoard theBoard, boolean isBlackPerspective) {
        if (isBlackPerspective) {
            for (int row = 1; row <= 8; ++row) {
                System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                drawChessSide(row);

                for (int column = 0; column < 8; column++) {
                    if (row % 2 == 1) {
                        if (column % 2 == 0) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                        } else {
                            System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                        }
                    } else {
                        if (column % 2 == 0) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                        } else {
                            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                        }
                    }

                    ChessPiece currentPiece = theBoard.getPiece(new ChessPosition(row, column + 1));
                    if (currentPiece != null) {
                        if (currentPiece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                        } else {
                            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        }

                        System.out.print(" ");
                        System.out.print(currentPiece.toString());
                        System.out.print(" ");
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }
                }

                System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
                System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                drawChessSide(row);

                System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                System.out.println();
            }
        } else {
            for (int row = 8; row > 0; --row) {
                System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                drawChessSide(row);

                for (int column = 0; column < 8; column++) {
                    if (row % 2 == 0) {
                        if (column % 2 == 0) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                        } else {
                            System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                        }
                    } else {
                        if (column % 2 == 0) {
                            System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                        } else {
                            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                        }
                    }

                    ChessPiece currentPiece = theBoard.getPiece(new ChessPosition(row, column + 1));
                    if (currentPiece != null) {
                        if (currentPiece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                        } else {
                            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        }

                        System.out.print(" ");
                        System.out.print(currentPiece.toString());
                        System.out.print(" ");
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }
                }

                System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
                System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                drawChessSide(row);

                System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                System.out.println();
            }
        }
    }

    private void drawChessTop(boolean isBlackPerspective) {
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
        System.out.print(EscapeSequences.EMPTY);

        if (isBlackPerspective) {
            for (char c = 'a'; c <= 'h'; ++c) {
                System.out.print(" ");
                System.out.print(c);
                System.out.print(" ");
            }
        } else {
            for (char c = 'h'; c >= 'a'; --c) {
                System.out.print(" ");
                System.out.print(c);
                System.out.print(" ");
            }
        }

        System.out.print(EscapeSequences.EMPTY);
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.println();
    }

    private void drawChessSide(int theLineNumber) {
        System.out.print(" ");
        System.out.print(theLineNumber);
        System.out.print(" ");
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
}

