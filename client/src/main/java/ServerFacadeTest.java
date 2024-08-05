import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.JoinData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTest {
    private static ServerFacade serverFacade;
    private static Server server;

    private UserData testUser;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);

        UserData testUser = new UserData("test", "test", "test@test.com");

        try {
            serverFacade.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            serverFacade.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerPositive() {
        assertDoesNotThrow(() -> serverFacade.registerUser(testUser));
    }

    @Test
    public void registerNegative() {
        assertDoesNotThrow(() -> serverFacade.registerUser(testUser));
        assertThrows(DataAccessException.class, () -> serverFacade.registerUser(testUser));
    }

    @Test
    public void loginPositive() {
        AuthData logoutAuth = null;
        try {
            logoutAuth = serverFacade.registerUser(testUser);
            serverFacade.logoutUser(logoutAuth);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        assertDoesNotThrow(() -> serverFacade.loginUser(testUser));
    }

    @Test
    public void loginNegative() {
        assertThrows(DataAccessException.class, () -> serverFacade.loginUser(testUser));
    }

    @Test
    public void logoutPositive() {
        AuthData logoutAuth = null;
        try {
            logoutAuth = serverFacade.registerUser(testUser);
            AuthData finalLogoutAuth = logoutAuth;
            assertDoesNotThrow(() -> serverFacade.logoutUser(finalLogoutAuth));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void logoutNegative() {
        AuthData logoutAuth = null;
        assertThrows(DataAccessException.class, () -> serverFacade.logoutUser(logoutAuth));
    }

    @Test
    public void getAllGamesPositive() {
        AuthData getAllGamesAuth = null;
        try {
            getAllGamesAuth = serverFacade.registerUser(testUser);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        AuthData finalGetAllGamesAuth = getAllGamesAuth;
        assertDoesNotThrow(() -> serverFacade.getAllGames(finalGetAllGamesAuth));
    }

    @Test
    public void getAllGamesNegative() {
        AuthData getAllGamesAuth = null;
        assertThrows(DataAccessException.class, () -> serverFacade.getAllGames(getAllGamesAuth));
    }

    @Test
    public void createGamePositive() {
        AuthData createGameAuth = null;
        try {
            createGameAuth = serverFacade.registerUser(testUser);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        GameData newGame = new GameData(Integer.MAX_VALUE, null, null, "good game", null);
        AuthData finalCreateGameAuth = createGameAuth;

        assertDoesNotThrow(() -> serverFacade.createGame(newGame, finalCreateGameAuth));
    }

    @Test
    public void createGameNegative() {
        AuthData createGameAuth = null;
        try {
            createGameAuth = serverFacade.registerUser(testUser);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        GameData newGame = new GameData(Integer.MAX_VALUE, null, null, null, null);
        AuthData finalCreateGameAuth = createGameAuth;

        assertDoesNotThrow(() -> serverFacade.createGame(newGame, finalCreateGameAuth));
    }

    @Test
    public void joinGamePositive() {
        AuthData createGameAuth = null;
        try {
            createGameAuth = serverFacade.registerUser(testUser);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        GameData newGame = new GameData(Integer.MAX_VALUE, "winner", null, null, null);
        AuthData finalCreateGameAuth = createGameAuth;

        try {
            serverFacade.createGame(newGame, finalCreateGameAuth);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        JoinData dataToJoinWith = new JoinData("black", 1);
        assertDoesNotThrow(() -> serverFacade.joinGame(dataToJoinWith, finalCreateGameAuth));
    }

    @Test
    public void joinGameNegative() {
        AuthData createGameAuth = null;
        try {
            createGameAuth = serverFacade.registerUser(testUser);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        GameData newGame = new GameData(Integer.MAX_VALUE, "winner", null, null, null);
        AuthData finalCreateGameAuth = createGameAuth;

        try {
            serverFacade.createGame(newGame, finalCreateGameAuth);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        JoinData dataToJoinWith = new JoinData("white", 1);
        assertThrows(DataAccessException.class, () -> serverFacade.joinGame(dataToJoinWith, finalCreateGameAuth));
    }

    public void deleteAllPositive() {
        assertDoesNotThrow(() -> serverFacade.deleteAll());
    }
}
