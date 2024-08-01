package dataaccess;

import chess.ChessGame;
import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.mysql.MySqlGameDAO;
import dataaccess.mysql.MySqlUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseUnitTests {
    private MySqlUserDAO userTestDatabase;
    private MySqlAuthDAO authTestDatabase;
    private MySqlGameDAO gameTestDatabase;

    static private AuthData goodAuth;
    static private AuthData badAuth;

    static private UserData testUser;

    @BeforeEach
    void resetData() throws DataAccessException {
        try {
            userTestDatabase = new MySqlUserDAO();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

        try {
            authTestDatabase = new MySqlAuthDAO();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

        try {
            gameTestDatabase = new MySqlGameDAO();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

        authTestDatabase.deleteAll();
        userTestDatabase.deleteAll();
        gameTestDatabase.deleteAll();

        goodAuth = new AuthData("goodAuthToken", "goodAuthUsername");
        badAuth = new AuthData("badAuthToken", "badAuthUsername");

        try {
            authTestDatabase.createAuth(goodAuth);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

        testUser = new UserData("test", "test", "test@test.com");
    }

    @Test
    void createAuthPositive() throws DataAccessException {
        AuthData newAuth = new AuthData("newAuthToken", "newAuthUsername");

        try {
            authTestDatabase.createAuth(newAuth);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

        try {
            assertEquals(authTestDatabase.getAuth(newAuth.authToken()), newAuth);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Test
    void createAuthNegative() throws DataAccessException {
        AuthData newAuth = new AuthData("newAuthToken", "newAuthUsername");

        try {
            authTestDatabase.createAuth(newAuth);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

        //duplicate auth
        assertThrows(DataAccessException.class, () -> authTestDatabase.createAuth(newAuth));
    }

    @Test
    void getAuthPositive() throws DataAccessException {
        assertEquals(authTestDatabase.getAuth("goodAuthToken"), goodAuth);
    }

    @Test
    void getAuthNegative() throws DataAccessException {
        //auth token not in db
        assertThrows(DataAccessException.class, () -> authTestDatabase.getAuth("badAuthToken"));
    }

    @Test
    void deleteAuthPositive() throws DataAccessException {
        assertEquals(authTestDatabase.size(), 1);
        assertDoesNotThrow(() -> authTestDatabase.deleteAuth(goodAuth));
        assertEquals(authTestDatabase.size(), 0);
    }

    @Test
    void deleteAuthNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> authTestDatabase.deleteAuth(badAuth));
    }

    @Test
    void authSizePositive() throws DataAccessException {
        assertEquals(authTestDatabase.size(), 1);
        authTestDatabase.deleteAuth(goodAuth);
        assertEquals(authTestDatabase.size(), 0);
    }

    @Test
    void authSizeNegative() throws DataAccessException {
        assertDoesNotThrow(() -> authTestDatabase.size());
    }

    @Test
    void authDeleteAllPositive() throws DataAccessException {
        AuthData newAuth = new AuthData("newAuthToken", "newAuthUsername");
        assertDoesNotThrow(() -> authTestDatabase.createAuth(newAuth));
        assertEquals(authTestDatabase.size(), 2);

        assertDoesNotThrow(() -> authTestDatabase.deleteAll());
        assertEquals(authTestDatabase.size(), 0);
    }

    @Test
    void authDeleteAllNegative() throws DataAccessException {
        assertDoesNotThrow(() -> authTestDatabase.deleteAll());
    }

    @Test
    void createUserPositive() throws DataAccessException {
        assertDoesNotThrow(() -> userTestDatabase.createUser(testUser));
        assertEquals(userTestDatabase.getUser(testUser.username()), testUser);
    }

    @Test
    void createUserNegative() throws DataAccessException {
        assertDoesNotThrow(() -> userTestDatabase.createUser(testUser));
        assertEquals(userTestDatabase.size(), 1);

        assertThrows(DataAccessException.class, () -> userTestDatabase.createUser(testUser));
    }

    @Test
    void getUserPositive() throws DataAccessException {
        createUserPositive();
    }

    @Test
    void getUserNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> userTestDatabase.getUser(testUser.username()));
    }

    @Test
    void userSizePositive() throws DataAccessException {
        assertEquals(userTestDatabase.size(), 0);
        userTestDatabase.createUser(testUser);
        assertEquals(userTestDatabase.size(), 1);
    }

    @Test
    void userSizeNegative() throws DataAccessException {
        assertEquals(userTestDatabase.size(), 0);
        userTestDatabase.createUser(testUser);
        assertNotEquals(userTestDatabase.size(), 0);
    }

    @Test
    void userDeleteAllPositive() throws DataAccessException {
        assertDoesNotThrow(() -> userTestDatabase.createUser(testUser));
        assertEquals(authTestDatabase.size(), 1);

        assertDoesNotThrow(() -> authTestDatabase.deleteAll());
        assertEquals(authTestDatabase.size(), 0);
    }

    @Test
    void userDeleteAllNegative() throws DataAccessException {
        assertDoesNotThrow(() -> userTestDatabase.deleteAll());
    }

    @Test
    void createGamePositive() throws DataAccessException {
        assertEquals(gameTestDatabase.size(), 0);

        String gameName = "gameName";
        assertDoesNotThrow(() -> gameTestDatabase.createGame(gameName));
        assertEquals(gameTestDatabase.size(), 1);
    }

    @Test
    void createGameNegative() throws DataAccessException {
        String gameName = "gameName";
        assertDoesNotThrow(() -> gameTestDatabase.createGame(gameName));
        assertNotEquals(new GameData(1, null, null, gameName, new ChessGame()), gameTestDatabase.getGame(1));
    }

    @Test
    void getGamePositive() throws DataAccessException {
        assertEquals(gameTestDatabase.size(), 0);
        String gameName = "gameName";
        assertDoesNotThrow(() -> gameTestDatabase.createGame(gameName));
        assertEquals(gameTestDatabase.size(), 1);
        assertEquals(gameTestDatabase.getGame(1).gameName(), "gameName");
        assertEquals(gameTestDatabase.getGame(1).gameID(), 1);
        assertNull(gameTestDatabase.getGame(1).whiteUsername());
        assertNull(gameTestDatabase.getGame(1).blackUsername());
    }

    @Test
    void getGameNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> gameTestDatabase.getGame(2));
    }

    @Test
    void joinGamePositive() throws DataAccessException {
        String gameName = "gameName";
        assertDoesNotThrow(() -> gameTestDatabase.createGame(gameName));
        assertDoesNotThrow(() -> gameTestDatabase.updateGame("white", "test", gameTestDatabase.getGame(1)));
    }

    @Test
    void joinGameNegative() throws DataAccessException {
        String gameName = "gameName";
        assertDoesNotThrow(() -> gameTestDatabase.createGame(gameName));
        assertThrows(Exception.class, () -> gameTestDatabase.updateGame("white", "test", null));
    }

    @Test
    void gameSizePositive() throws DataAccessException {
        assertEquals(gameTestDatabase.size(), 0);
        String gameName = "gameName";
        assertDoesNotThrow(() -> gameTestDatabase.createGame(gameName));
        assertEquals(gameTestDatabase.size(), 1);
    }

    @Test
    void gameSizeNegative() throws DataAccessException {
        assertEquals(gameTestDatabase.size(), 0);
        String gameName = "gameName";
        assertDoesNotThrow(() -> gameTestDatabase.createGame(gameName));
        assertNotEquals(gameTestDatabase.size(), 0);
    }

    @Test
    void gameDeleteAllPositive() throws DataAccessException {
        assertDoesNotThrow(() -> gameTestDatabase.createGame("gameName"));
        assertEquals(authTestDatabase.size(), 1);

        assertDoesNotThrow(() -> authTestDatabase.deleteAll());
        assertEquals(authTestDatabase.size(), 0);
    }

    @Test
    void gameDeleteAllNegative() throws DataAccessException {
        assertDoesNotThrow(() -> gameTestDatabase.deleteAll());
    }
}
