package dataaccess;

import java.util.Collection;

import chess.ChessGame;
import model.*;

public interface DataAccess {
    void clear();
    AuthData createUser(UserData theUser);
    UserData getUser(int theUsername) throws DataAccessException;
    GameData createGame(int theGameName);
    GameData getGame(int theGameID) throws DataAccessException;
    Collection<GameData> listGames();
    void updateGame(ChessGame.TeamColor thePlayerColor, AuthData theUserData, GameData theGame) throws DataAccessException;
    void createAuth(AuthData theUserData);
    AuthData getAuth(String theAuthToken) throws DataAccessException;
    void deleteAuth(AuthData theUserData) throws DataAccessException    ;
}
