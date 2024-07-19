package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public interface GameDAO extends DataAccess {
    public GameData createGame(int theGameName);

    public GameData getGame(int theGameID) throws DataAccessException;

    public Collection<GameData> listGames();

    public void updateGame(ChessGame.TeamColor thePlayerColor, AuthData theUserData, GameData theGame)
            throws DataAccessException;
}
