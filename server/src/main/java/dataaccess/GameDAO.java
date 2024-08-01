package dataaccess;

import model.GameData;

public interface GameDAO extends DataAccess {
    public GameData createGame(String theGameName) throws DataAccessException;

    public GameData getGame(int theGameID) throws DataAccessException;

    public void updateGame(String thePlayerColor, String username, GameData theGame)
            throws DataAccessException;

    public int size() throws DataAccessException;
}
