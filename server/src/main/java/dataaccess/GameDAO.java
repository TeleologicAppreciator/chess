package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public interface GameDAO extends DataAccess {
    public GameData createGame(String theGameName);

    public GameData getGame(int theGameID);

    public GameData[] getAllGames();

    public void updateGame(ChessGame.TeamColor thePlayerColor, AuthData theUserAuthData, GameData theGame);
}
