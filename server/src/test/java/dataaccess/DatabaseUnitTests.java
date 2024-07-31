package dataaccess;

import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.mysql.MySqlDataAccess;
import dataaccess.mysql.MySqlGameDAO;
import dataaccess.mysql.MySqlUserDAO;
import model.AuthData;
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

        testUser = new UserData("test", "test", "test");
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
        try {
            assertEquals(authTestDatabase.getAuth("goodAuthToken"), goodAuth);
        } catch (Exception e) {
            throw new DataAccessException("unable to connect to database");
        }
    }

    @Test
    void getAuthNegative() throws DataAccessException {
        //auth token not in db
        assertThrows(DataAccessException.class, () -> authTestDatabase.getAuth("badAuthToken"));
    }

    @Test
    void deleteAuthPositive() throws DataAccessException {
        assertDoesNotThrow(() -> authTestDatabase.deleteAuth(goodAuth));
    }

    @Test
    void deleteAuthNegative() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> authTestDatabase.deleteAuth(badAuth));
    }

    @Test
    void sizePositive() throws DataAccessException {
        assertEquals(authTestDatabase.size(), 1);
        authTestDatabase.deleteAuth(goodAuth);
        assertEquals(authTestDatabase.size(), 0);
    }

    @Test
    void sizeNegative() throws DataAccessException {
        assertDoesNotThrow(() -> authTestDatabase.size());
    }

    @Test
    void deleteAllPositive() throws DataAccessException {
        AuthData newAuth = new AuthData("newAuthToken", "newAuthUsername");
        assertDoesNotThrow(() -> authTestDatabase.createAuth(newAuth));
        assertEquals(authTestDatabase.size(), 2);

        assertDoesNotThrow(() -> authTestDatabase.deleteAll());
        assertEquals(authTestDatabase.size(), 0);
    }
}
