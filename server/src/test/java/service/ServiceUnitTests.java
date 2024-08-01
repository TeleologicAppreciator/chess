package service;

import chess.ChessGame;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceUnitTests {
    private MemoryUserDAO userTestDatabase;
    private MemoryAuthDAO authTestDatabase;
    private MemoryGameDAO gameTestDatabase;

    static private AuthData goodAuth;
    static private AuthData badAuth;

    static private UserData testUser;

    @BeforeEach
    void resetData() {
        userTestDatabase = new MemoryUserDAO();
        authTestDatabase = new MemoryAuthDAO();
        gameTestDatabase = new MemoryGameDAO();

        goodAuth = new AuthData("goodAuthToken", "goodAuthUsername");
        badAuth = new AuthData("badAuthToken", "badAuthUsername");
        authTestDatabase.createAuth(goodAuth);

        testUser = new UserData("test", "test", "test@test.com");
    }

    @Test
    void deleteAllPositive() {
        assertDoesNotThrow(() -> userTestDatabase.createUser(testUser));

        gameTestDatabase.createGame("name");

        assertEquals(userTestDatabase.size(), 1);
        assertEquals(authTestDatabase.size(), 1);
        assertEquals(gameTestDatabase.size(), 1);

        var deleteAllService = new DeleteAllService(userTestDatabase, authTestDatabase, gameTestDatabase);
        try {
            deleteAllService.deleteAllData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(userTestDatabase.size(), 0);
        assertEquals(authTestDatabase.size(), 0);
        assertEquals(gameTestDatabase.size(), 0);
    }

    @Test
    void createGamePositive() {
        var createGameService = new CreateGameService(authTestDatabase, gameTestDatabase);

        CreateGameRequest createGameRequest = new CreateGameRequest(goodAuth.authToken(), goodAuth.username());
        var result = assertDoesNotThrow(() -> createGameService.createGame(createGameRequest));

        ChessGame equalsForTheTest = assertDoesNotThrow(() -> gameTestDatabase.getGame(1).game());

        var newGame = new GameData(1, null, null, goodAuth.username(), equalsForTheTest);
        var gameFromDatabase = assertDoesNotThrow(() -> gameTestDatabase.getGame(1));

        assertEquals(newGame, gameFromDatabase);
    }

    @Test
    void createGameNegative() {
        CreateGameRequest createGameRequest = new CreateGameRequest(badAuth.authToken(), badAuth.username());

        var result = new CreateGameService(authTestDatabase, gameTestDatabase).createGame(createGameRequest);

        assertNotNull(result.getErrorMessage());
    }

    @Test
    void getAllGamesPositive() {
        var getAllGamesService = new GetAllGamesService(authTestDatabase, gameTestDatabase);

        var getAllGamesRequest = new GetAllGamesRequest(goodAuth.authToken());
        var result = getAllGamesService.getAllGames(getAllGamesRequest);

        assertNull(result.getErrorMessage());
    }

    @Test
    void getAllGamesNegative() {
        var getAllGamesRequest = new GetAllGamesRequest(badAuth.authToken());
        var result = new GetAllGamesService(authTestDatabase, gameTestDatabase).getAllGames(getAllGamesRequest);

        assertNotNull(result.getErrorMessage());
    }

    @Test
    void joinGamePositive() {
        gameTestDatabase.createGame(goodAuth.username());

        try {
            assertNull(gameTestDatabase.getGame(1).blackUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }

        var joinGameService = new JoinGameService(authTestDatabase, gameTestDatabase);
        var joinGameRequest = new JoinGameRequest("black", 1, goodAuth.authToken());

        var result = joinGameService.joinGame(joinGameRequest);

        try {
            assertNotNull(gameTestDatabase.getGame(1).blackUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void joinGameNegative() {
        gameTestDatabase.createGame("name");
        try {
            assertNull(gameTestDatabase.getGame(1).blackUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }

        var joinGameRequest = new JoinGameRequest("black", 1, badAuth.authToken());
        var result = new JoinGameService(authTestDatabase, gameTestDatabase);

        try {
            assertNull(gameTestDatabase.getGame(1).blackUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void loginPositive() {
        var loginService = new LoginService(userTestDatabase, authTestDatabase);

        assertDoesNotThrow(() -> userTestDatabase.createUser(testUser));

        var loginRequest = new LoginRequest(testUser.username(), testUser.password());
        var result = loginService.login(loginRequest);

        assertNull(result.getErrorMessage());
    }

    @Test
    void loginNegative() {
        var loginRequest = new LoginRequest(badAuth.username(), "");
        var result = new LoginService(userTestDatabase, authTestDatabase).login(loginRequest);
        assertNotNull(result.getErrorMessage());
    }

    @Test
    void logoutPositive() {
        assertDoesNotThrow(() -> userTestDatabase.createUser(testUser));

        var logoutRequest = new LogoutRequest(goodAuth.authToken());
        var logoutService = new LogoutService(authTestDatabase);

        var result = logoutService.logout(logoutRequest);
        assertNull(result.getErrorMessage());
    }

    @Test
    void logoutNegative() {
        var logoutRequest = new LogoutRequest(badAuth.authToken());
        var logoutService = new LogoutService(authTestDatabase);

        var result = logoutService.logout(logoutRequest);
        assertNotNull(result.getErrorMessage());
    }

    @Test
    void registerPositive() {
        var registerService = new RegisterService(userTestDatabase, authTestDatabase);
        RegisterRequest registerRequest = new RegisterRequest(
                testUser.username(), testUser.password(), testUser.email());

        var result = registerService.registerUser(registerRequest);
        assertNull(result.getErrorMessage());
    }

    @Test
    void registerNegative() {
        var registerService = new RegisterService(userTestDatabase, authTestDatabase);
        RegisterRequest registerRequest = new RegisterRequest(
                testUser.username(), testUser.password(), testUser.email());

        assertDoesNotThrow(() -> userTestDatabase.createUser(testUser));

        var result = registerService.registerUser(registerRequest);
        assertNotNull(result.getErrorMessage());
    }

    @Test
    void authServiceUserAuthorizedVerificationPositive() {
        AuthService authService = new AuthService(authTestDatabase);
        assertNotNull(authService.userAuthorizedVerification(goodAuth.authToken()));
    }

    @Test
    void authServiceUserAuthorizedVerificationNegative() {
        AuthService authService = new AuthService(authTestDatabase);
        assertNull(authService.userAuthorizedVerification(badAuth.authToken()));
    }

    @Test
    void authServiceIsNotAuthorizedPositive() {
        AuthService authService = new AuthService(authTestDatabase);
        var result = authService.isNotAuthorized(goodAuth.authToken());

        assertFalse(result);
    }

    @Test
    void authServiceIsNotAuthorizedNegative() {
        AuthService authService = new AuthService(authTestDatabase);
        var result = authService.isNotAuthorized("badAuth");

        assertTrue(result);
    }

    @Test
    void authServiceGetAuthDataPositive() {
        AuthService authService = new AuthService(authTestDatabase);
        assertEquals(authService.getAuthData(), authTestDatabase);
    }

    @Test
    void authServiceGetAuthDataNegative() {
        AuthService authService = new AuthService(authTestDatabase);
        assertNotNull(authService.getAuthData());
    }
}