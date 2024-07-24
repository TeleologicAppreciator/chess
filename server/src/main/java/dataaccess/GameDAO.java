package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public interface GameDAO extends DataAccess {
    public GameData createGame(String theGameName);

    public GameData getGame(int theGameID) throws DataAccessException;

    public GameData[] getAllGames();

    public void updateGame(String thePlayerColor, String username, GameData theGame)
            throws DataAccessException;

    public int size();

    public GameData getGameTest(int gameID);
}
