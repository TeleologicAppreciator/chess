package dataaccess;

import java.sql.SQLException;

public class MySqlDataAccess {

    public MySqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createGameDatabase = {
        """
        CREATE TABLE IF NOT EXISTS  game (
            `id` INT NOT NULL AUTO_INCREMENT,
            `whiteUsername` varchar(256) DEFAULT NULL,
            `blackUsername` varchar(256) DEFAULT NULL,
            `name` varchar(256) NOT NULL,
            `json` TEXT NOT NULL,
             PRIMARY KEY (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """
    };

    private final String[] createUserDatabase = {
        """
        CREATE TABLE IF NOT EXISTS  user (
          `id` INT NOT NULL AUTO_INCREMENT,
           `username` varchar(256) NOT NULL,
           `password` varchar(256) NOT NULL,
           `email` varchar(256) NOT NULL,
           PRIMARY KEY (`id`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """
    };

    private final String[] createAuthDatabase = {
        """
        CREATE TABLE IF NOT EXISTS  auth (
          `authToken` varchar(256) NOT NULL,
          `username` varchar(256) NOT NULL,
          PRIMARY KEY (`authToken`)
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
        """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createGameDatabase) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }

            for (var statement : createUserDatabase) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }

            for (var statement : createAuthDatabase) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    protected boolean isUsernameValid(String username) {
        return username.matches("[a-zA-Z0-9!?]+");
    }

    protected boolean isPasswordValid(String password) {
        return isUsernameValid(password);
    }

}
