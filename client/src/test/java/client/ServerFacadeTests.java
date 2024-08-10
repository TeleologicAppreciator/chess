package client;

import model.AuthData;
import model.GameData;
import model.JoinData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {
    private static ServerFacade serverFacade;
    private static Server server;

    private static UserData testUser;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);

        testUser = new UserData("test", "test", "test@test.com");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearTests() throws Exception {
        serverFacade.deleteAll();
    }

    @Test
    public void registerPositive() {
        assertDoesNotThrow(() -> serverFacade.registerUser(testUser));
    }

    @Test
    public void registerNegative() {
        assertDoesNotThrow(() -> serverFacade.registerUser(testUser));
        assertThrows(Exception.class, () -> serverFacade.registerUser(testUser));
    }

    @Test
    public void loginPositive() throws Exception{
        AuthData logoutAuth = null;
        logoutAuth = serverFacade.registerUser(testUser);
        serverFacade.logoutUser(logoutAuth);

        assertDoesNotThrow(() -> serverFacade.loginUser(testUser));
    }

    @Test
    public void loginNegative() {
        assertThrows(Exception.class, () -> serverFacade.loginUser(testUser));
    }

    @Test
    public void logoutPositive() throws Exception{
        AuthData logoutAuth = null;

        logoutAuth = serverFacade.registerUser(testUser);
        AuthData finalLogoutAuth = logoutAuth;
        assertDoesNotThrow(() -> serverFacade.logoutUser(finalLogoutAuth));
    }

    @Test
    public void logoutNegative() {
        AuthData logoutAuth = null;
        assertThrows(Exception.class, () -> serverFacade.logoutUser(logoutAuth));
    }

    @Test
    public void getAllGamesPositive() throws Exception{
        AuthData getAllGamesAuth = null;
        getAllGamesAuth = serverFacade.registerUser(testUser);

        AuthData finalGetAllGamesAuth = getAllGamesAuth;
        assertDoesNotThrow(() -> serverFacade.getAllGames(finalGetAllGamesAuth));
    }

    @Test
    public void getAllGamesNegative() {
        AuthData getAllGamesAuth = null;
        assertThrows(Exception.class, () -> serverFacade.getAllGames(getAllGamesAuth));
    }

    @Test
    public void createGamePositive() throws Exception {
        AuthData createGameAuth = null;

        createGameAuth = serverFacade.registerUser(testUser);

        GameData newGame = new GameData(Integer.MAX_VALUE, null, null, "good game", null);
        AuthData finalCreateGameAuth = createGameAuth;

        assertDoesNotThrow(() -> serverFacade.createGame(newGame, finalCreateGameAuth));
    }

    @Test
    public void createGameNegative() throws Exception {
        AuthData createGameAuth = null;

        createGameAuth = serverFacade.registerUser(testUser);

        GameData newGame = new GameData(Integer.MAX_VALUE, null, null, "bad game", null);
        AuthData finalCreateGameAuth = createGameAuth;

        assertDoesNotThrow(() -> serverFacade.createGame(newGame, finalCreateGameAuth));
    }

    @Test
    public void joinGamePositive() throws Exception{
        AuthData createGameAuth = null;

        createGameAuth = serverFacade.registerUser(testUser);

        GameData newGame = new GameData(Integer.MAX_VALUE, null, null, "youcanjointhisgame", null);
        AuthData finalCreateGameAuth = createGameAuth;

        serverFacade.createGame(newGame, finalCreateGameAuth);

        JoinData dataToJoinWith = new JoinData("black", 1);
        assertDoesNotThrow(() -> serverFacade.joinGame(dataToJoinWith, finalCreateGameAuth));
    }

    @Test
    public void joinGameNegative() throws Exception {
        AuthData createGameAuth = null;
        createGameAuth = serverFacade.registerUser(testUser);

        GameData newGame = new GameData(Integer.MAX_VALUE, null, null, "unabletojointhisgame", null);

        serverFacade.createGame(newGame, createGameAuth);

        JoinData dataToJoinWith = new JoinData("white", 1);
        AuthData finalCreateGameAuth = createGameAuth;
        serverFacade.joinGame(dataToJoinWith, finalCreateGameAuth);
        assertThrows(Exception.class, () -> serverFacade.joinGame(dataToJoinWith, finalCreateGameAuth));
    }

    @Test
    public void deleteAllPositive() {
        assertDoesNotThrow(() -> serverFacade.deleteAll());
    }
}