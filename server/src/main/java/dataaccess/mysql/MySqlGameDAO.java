package dataaccess.mysql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import serialization.Deserializer;
import serialization.Serializer;

import java.sql.ResultSet;
import java.sql.Statement;

public class MySqlGameDAO extends MySqlDataAccess implements GameDAO {
    public MySqlGameDAO() throws DataAccessException {
        super();
    }

    public GameData createGame(String theGameName) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO game (whiteUsername, blackUsername, name, json) VALUES (?, ?, ?, ?)";

            try (var preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ChessGame game = new ChessGame();

                preparedStatement.setString(1, null); //white username
                preparedStatement.setString(2, null); //black username
                preparedStatement.setString(3, theGameName);
                preparedStatement.setString(4, new Serializer(game).serialize());

                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {  // Move to the first row
                        int gameID = generatedKeys.getInt(1);
                        return new GameData(gameID, null, null, theGameName, game);
                    } else {
                        throw new DataAccessException("No ID obtained.");
                    }
                }
            }

        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int theGameID) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, name, json FROM game WHERE id = ?";

            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, Integer.toString(theGameID));

                ResultSet resultSet = preparedStatement.executeQuery();

                String gameIDIsTheSame = null;
                while (resultSet.next()) {
                    gameIDIsTheSame = resultSet.getString("id");
                    if (gameIDIsTheSame.equals(Integer.toString(theGameID))) {
                        String white = resultSet.getString("whiteUsername");
                        String black = resultSet.getString("blackUsername");
                        String name = resultSet.getString("name");
                        ChessGame game = (ChessGame)
                                new Deserializer(resultSet.getString("json"), new ChessGame()).deserialize();

                        return new GameData(theGameID, white, black, name, game);
                    }
                }

                throw new DataAccessException("Game not found");
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateGame(String thePlayerColor, String username, GameData theGame) throws DataAccessException {
        GameData theGameToCompare = getGame(theGame.gameID());

        try (var connection = DatabaseManager.getConnection()) {
            String statement;
            if (thePlayerColor.equalsIgnoreCase("white")) {
                statement = "UPDATE game SET whiteUsername = ? WHERE id = ?";
            } else {
                statement = "UPDATE game SET blackUsername = ? WHERE id = ?";
            }

            try (var preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, theGame.gameID());

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public int size() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT id FROM game";

            int i = 0;

            try (var preparedStatement = connection.prepareStatement(statement)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    i++;
                }
            }

            return i;
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void deleteAll() throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            var preparedStatement = connection.prepareStatement("DROP TABLE game");
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

        new MySqlAuthDAO();
    }
}