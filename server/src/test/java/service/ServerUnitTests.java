package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

class ServerUnitTests {
    static private Server testServer;
    static private AuthData makeSureAuthWorks;

    @BeforeAll
    static void startServer() {
        testServer = new Server();
        testServer.run(8080);
    }

    @AfterAll
    static void stopServer() {
        testServer.stop();
    }

    @BeforeEach
    void resetData() {
        MemoryGameDAO memoryGameDAO = new MemoryGameDAO();
        MemoryAuthDAO memoryAuthDAO = new MemoryAuthDAO();
        MemoryUserDAO memoryUserDAO = new MemoryUserDAO();

        testServer.setMyUserDatabase(memoryUserDAO);
        testServer.setMyGameDatabase(memoryGameDAO);
        testServer.setMyAuthDatabase(memoryAuthDAO);

        makeSureAuthWorks = new AuthData("111", "username");
        testServer.getAuthDatabase().createAuth(makeSureAuthWorks);
    }

    @Test
    void deleteAllPositive() {
        UserData newUser = new UserData("test", "test", "test");

        UserDAO testUserDatabase = testServer.getUserDatabase();
        AuthDAO testAuthDatabase = testServer.getAuthDatabase();
        GameDAO testGameDatabase = testServer.getGameDatabase();

        try {
            testUserDatabase.createUser(newUser);
        } catch (Exception e) {
            //there will be no error
        }
        testGameDatabase.createGame("name");

        assertEquals(testUserDatabase.size(), 1);
        assertEquals(testAuthDatabase.size(), 1);
        assertEquals(testGameDatabase.size(), 1);

        var deleteAllService = new DeleteAllService(testUserDatabase, testAuthDatabase, testGameDatabase);
        deleteAllService.deleteAllData();

        assertEquals(testUserDatabase.size(), 0);
        assertEquals(testAuthDatabase.size(), 0);
        assertEquals(testGameDatabase.size(), 0);
    }

    @Test
    void createGamePositive() {
        AuthDAO authDatabase = testServer.getAuthDatabase();

        var createGameService = new CreateGameService(testServer.getAuthDatabase(), testServer.getGameDatabase());

        CreateGameRequest createGameRequest = new CreateGameRequest(makeSureAuthWorks.authToken(), "name");
        var result = assertDoesNotThrow(() -> createGameService.createGame(createGameRequest));

        ChessGame equalsForTheTest = null;

        try {
            equalsForTheTest = testServer.getGameDatabase().getGame(1).game();
        } catch (DataAccessException e) {
            //there won't be an error
        }

        var newGame = new GameData(1, null, null, "name", equalsForTheTest);

        try {
            assertEquals(newGame, testServer.getGameDatabase().getGame(1));
        } catch (DataAccessException e) {
            //there won't be an error
        }
    }

    @Test
    void createGameNegative() {
        CreateGameRequest createGameRequest = new CreateGameRequest("badAuth", "name");

        var result = new CreateGameService(testServer.getAuthDatabase(), testServer.getGameDatabase())
                .createGame(createGameRequest);

        assertNotNull(result.getErrorMessage());
    }

    @Test
    void getAllGamesPositive() {
        var getAllGamesService = new GetAllGamesService(testServer.getAuthDatabase(), testServer.getGameDatabase());

        var getAllGamesRequest = new GetAllGamesRequest("111");
        var result = getAllGamesService.retrieveAllGames(getAllGamesRequest);

        assertNull(result.getErrorMessage());
    }

    @Test
    void getAllGamesNegative() {
        var getAllGamesRequest = new GetAllGamesRequest("badAuth");
        var result = new GetAllGamesService(
                testServer.getAuthDatabase(), testServer.getGameDatabase()).retrieveAllGames(getAllGamesRequest);

        assertNotNull(result.getErrorMessage());
    }

    @Test
    void joinGamePositive() {
        testServer.getGameDatabase().createGame("name");


        assertNull(testServer.getGameDatabase().getGameTest(1).blackUsername());

        var joinGameService = new JoinGameService(testServer.getAuthDatabase(), testServer.getGameDatabase());
        var joinGameRequest = new JoinGameRequest("black", 1, "111");


        var result = joinGameService.joinGame(joinGameRequest);

        assertNotNull(testServer.getGameDatabase().getGameTest(1).blackUsername());
    }

    @Test
    void joinGameNegative() {
        testServer.getGameDatabase().createGame("name");
        assertNull(testServer.getGameDatabase().getGameTest(1).blackUsername());
        var joinGameRequest = new JoinGameRequest("black", 1, "bathAuth");
        var result = new JoinGameService(testServer.getAuthDatabase(), testServer.getGameDatabase());
        assertNull(testServer.getGameDatabase().getGameTest(1).blackUsername());
    }

    @Test
    void loginPositive() {
        var loginService = new LoginService(testServer.getUserDatabase(), testServer.getAuthDatabase());
        UserData oldUser = new UserData("test", "test", "test");

        try{
            testServer.getUserDatabase().createUser(oldUser);
        } catch (DataAccessException e) {
            //no exceptions ever
        }

        var loginRequest = new LoginRequest("test", "test");
        var result = loginService.login(loginRequest);

        assertNull(result.getErrorMessage());
    }

    @Test
    void loginNegative() {
        var loginRequest = new LoginRequest("badAuth", "badAuth");
        var result = new LoginService(testServer.getUserDatabase(), testServer.getAuthDatabase()).login(loginRequest);
        assertNotNull(result.getErrorMessage());
    }

    @Test
    void logoutPositive() {
        UserData oldUser = new UserData("test", "test", "test");

        try{
            testServer.getUserDatabase().createUser(oldUser);
        } catch (DataAccessException e) {
            //no exceptions ever
        }

        var logoutService = new LogoutService(testServer.getAuthDatabase());
        //authToken from beforeEach method
        var logoutRequest = new LogoutRequest("111");
        var result = logoutService.logout(logoutRequest);
        assertNull(result.getErrorMessage());
    }

    @Test
    void logoutNegative() {
        var logoutService = new LogoutService(testServer.getAuthDatabase());
        //authToken from beforeEach method
        var logoutRequest = new LogoutRequest("badAuth");
        var result = logoutService.logout(logoutRequest);
        assertNotNull(result.getErrorMessage());
    }

    @Test
    void registerPositive() {
        var registerService = new RegisterService(testServer.getUserDatabase(), testServer.getAuthDatabase());
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test");

        var result = registerService.registerUser(registerRequest);

        assertNull(result.getErrorMessage());
    }

    @Test
    void registerNegative() {
        var registerService = new RegisterService(testServer.getUserDatabase(), testServer.getAuthDatabase());
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test");

        UserData oldUser = new UserData("test", "test", "test");

        try {
            testServer.getUserDatabase().createUser(oldUser);
        } catch (DataAccessException e) {
            //there are no errors
        }

        var result = registerService.registerUser(registerRequest);
        assertNotNull(result.getErrorMessage());
    }

    @Test
    void authServiceUserAuthorizedVerificationPositive() {
        AuthService authService = new AuthService(testServer.getAuthDatabase());
        assertNotNull(authService.userAuthorizedVerification("111"));
    }

    @Test
    void authServiceUserAuthorizedVerificationNegative() {
        AuthService authService = new AuthService(testServer.getAuthDatabase());
        assertNull(authService.userAuthorizedVerification("badAuth"));
    }

    @Test
    void authServiceUnauthorizedPositive() {
        AuthService authService = new AuthService(testServer.getAuthDatabase());
        var result = authService.unauthorized(makeSureAuthWorks);

        assertNull(result);
    }

    @Test
    void authServiceUnauthorizedNegative() {
        AuthService authService = new AuthService(testServer.getAuthDatabase());

        var result = authService.unauthorized(null);

        assertNotNull(result.getErrorMessage());
    }
}