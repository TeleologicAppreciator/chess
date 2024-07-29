package dataaccess;

import java.sql.SQLException;

public class MySqlDataAccess {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `name` varchar(256) NOT NULL,
              `json` TEXT NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(name)
            ) CREATE TABLE IF NOT EXISTS  user (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(password)
            ) CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) DEFAULT NULL,
              `username` varchar(256) DEFAULT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    private void addUser(String theUsername, String thePassword, String theEmail) throws DataAccessException, SQLException {
        String addStatment = "INSERT INTO user (username, password, email) VALUES (theUsername, thePassword, theEmail)";
        try (var connnection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connnection.prepareStatement(addStatment)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to add user: %s", e.getMessage()));
        }
    }
}
