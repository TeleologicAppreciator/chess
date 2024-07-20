package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {
    private int gameID = 1;
    private final HashMap<Integer, GameData> myGameData = new HashMap<>();

    public GameData createGame(String theGameName) {
        ChessGame game = new ChessGame();
        GameData newGame = new GameData(gameID, null, null, theGameName, game);

        myGameData.put(gameID, newGame);

        incrementGameID();
        return newGame;
    }

    public GameData getGame(int gameID) {
        return myGameData.get(gameID);
    }

    public Collection<GameData> getAllGames() {
        HashSet<GameData> games = new HashSet<>();
        games.addAll(myGameData.values());

        return games;
    }

    @Override
    public void updateGame(ChessGame.TeamColor thePlayerColor, AuthData theUserData, GameData theGame) throws DataAccessException {
        String newGamePlayer = theUserData.username();
        GameData updatedGame;

        if(thePlayerColor.equals(ChessGame.TeamColor.BLACK)){
            updatedGame = new GameData(theGame.myGameID(),
                    theGame.myWhiteUsername(), newGamePlayer,
                    theGame.myGameName(), theGame.myGame());
        } else {
            updatedGame = new GameData(theGame.myGameID(),
                    newGamePlayer, theGame.myBlackUsername(),
                    theGame.myGameName(), theGame.myGame());
        }

        myGameData.put(gameID, updatedGame);
    }

    public void deleteAll() {
        myGameData.clear();
    }

    private void incrementGameID(){
        gameID++;
    }
}