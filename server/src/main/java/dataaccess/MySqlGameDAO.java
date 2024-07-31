package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import serialization.Deserializer;
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
        try (var connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/mydb", "root", "Mypasswordformysqlserver50!")) {
            var statement = "SELECT id FROM game WHERE id = ?";

            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, Integer.toString(theGameID));

                ResultSet resultSet = preparedStatement.executeQuery();

                String gameIDIsTheSame = null;
                while (resultSet.next()) {
                    gameIDIsTheSame = resultSet.getString("authToken");
                    if (gameIDIsTheSame.equals(Integer.toString(theGameID))) {
                        String white = resultSet.getString("whiteUsername");
                        String black = resultSet.getString("blackUsername");
                        String name = resultSet.getString("name");
                        ChessGame game = (ChessGame)
                                new Deserializer(resultSet.getString("json"), ChessGame.class).deserialize();

                        return new GameData(theGameID, white, black, name, game);
                    }
                }

                throw new DataAccessException("Game not found");
            }
        } catch (Exception e) {
            throw new DataAccessException("Unable to read data");
        }
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
