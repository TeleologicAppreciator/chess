package dataaccess.mysql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
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
        try (var connection = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO game (whiteUsername, blackUsername, name, json) VALUES (?, ?, ?, ?)";

            try (var preparedStatement = connection.prepareStatement(statement)) {
                ChessGame game = new ChessGame();

                preparedStatement.setString(1, null); //white username
                preparedStatement.setString(2, null); //black username
                preparedStatement.setString(3, theGameName);
                preparedStatement.setString(4, new Serializer(game).serialize());

                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                int gameID = generatedKeys.getInt(1);

                return new GameData(gameID, null, null, theGameName, game);
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
                                new Deserializer(resultSet.getString("json"), ChessGame.class).deserialize();

                        return new GameData(theGameID, white, black, name, game);
                    }
                }

                throw new DataAccessException("Game not found");
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData[] getAllGames() throws DataAccessException {
        GameData[] allGamesResult = null;

        try {
            allGamesResult = new GameData[size()];
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

        try (var connection = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, name, json FROM game";

            try (var preparedStatement = connection.prepareStatement(statement)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                int i = 0;
                while (resultSet.next()) {
                    int gameID = Integer.parseInt(resultSet.getString("id"));
                    String white = resultSet.getString("whiteUsername");
                    String black = resultSet.getString("blackUsername");
                    String name = resultSet.getString("name");
                    ChessGame game = (ChessGame)
                            new Deserializer(resultSet.getString("json"), ChessGame.class).deserialize();

                    allGamesResult[i] = new GameData(gameID, white, black, name, game);
                    i++;
                    }
                }

                return allGamesResult;
            } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateGame(String thePlayerColor, String username, GameData theGame) throws DataAccessException {
        if(thePlayerColor == null) {
            throw new DataAccessException("Invalid player color");
        }

        if(!(thePlayerColor.equalsIgnoreCase("white") || thePlayerColor.equalsIgnoreCase("black"))) {
            throw new DataAccessException("Invalid player color");
        }

        //getGame throws exception if there is no game found
        GameData theGameToCompare = getGame(theGame.gameID());

        try (var connection = DatabaseManager.getConnection()) {

            var statement = "SELECT id, whiteUsername, blackUsername FROM game WHERE id = ?";
            try (var preparedStatement = connection.prepareStatement(statement)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                if(thePlayerColor.equalsIgnoreCase("white")) {
                    String white = resultSet.getString("whiteUsername");
                    if (white != null) {
                        throw new DataAccessException("White username already exists");
                    } else {
                        var newStatement = "INSERT INTO game (whiteUsername) VALUES (?)";
                        try (var newPreparedStatement = connection.prepareStatement(newStatement)) {
                            newPreparedStatement.setString(1, username);
                            newPreparedStatement.executeUpdate();
                        }
                    }
                } else {
                    String black = resultSet.getString("blackUsername");
                    if (black != null) {
                        throw new DataAccessException("Black username already exists");
                    } else {
                        var newStatement = "INSERT INTO game (blackUsername) VALUES (?)";
                        try (var newPreparedStatement = connection.prepareStatement(newStatement)) {
                            newPreparedStatement.setString(1, username);
                            newPreparedStatement.executeUpdate();
                        }

                    }
                }
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
