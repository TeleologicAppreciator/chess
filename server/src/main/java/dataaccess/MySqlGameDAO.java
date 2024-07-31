package dataaccess;

import chess.ChessGame;
import model.GameData;
import serialization.Serializer;

import java.sql.DriverManager;
import java.sql.ResultSet;

public class MySqlGameDAO extends MySqlDataAccess implements GameDAO {
    public MySqlGameDAO() throws DataAccessException {
        super();
    }

    public GameData createGame(String theGameName) throws DataAccessException {
        try (var connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", "root", "Mypasswordformysqlserver50!")) {
            var statement = "INSERT INTO game (whiteUsername, blackUsername, name, json) VALUES (?, ?, ?, ?)";

            try (var preparedStatement = connection.prepareStatement(statement)) {
                String whiteUsername = null;
                String blackUsername = null;
                ChessGame game = new ChessGame();

                preparedStatement.setString(1, whiteUsername);
                preparedStatement.setString(2, blackUsername);
                preparedStatement.setString(3, theGameName);
                preparedStatement.setString(4, new Serializer(game).serialize());

                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                int gameID = generatedKeys.getInt(1);

                return new GameData(gameID, null, null, theGameName, game);
            }


        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
        }
    }

    public GameData getGame(int theGameID) throws DataAccessException {
        return null;
    }

    public GameData[] getAllGames() {
        return null;
    }

    public void updateGame(String thePlayerColor, String username, GameData theGame) throws DataAccessException {

    }

    public int size() {
        return 0;
    }

    public GameData getGameTest(int gameID) {
        return null;
    }

    public void deleteAll() {

    }
}
