package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

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

    public GameData[] getAllGames() {
        HashSet<GameData> games = new HashSet<>();
        games.addAll(myGameData.values());
        
        GameData[] allGames = new GameData[games.size()];

        return games.toArray(allGames);
    }

    @Override
    public void updateGame(ChessGame.TeamColor thePlayerColor, AuthData theUserAuthData, GameData theGame){
        String newGamePlayer = theUserAuthData.username();
        GameData updatedGame;

        if(thePlayerColor.equals(ChessGame.TeamColor.BLACK)){
            updatedGame = new GameData(theGame.gameID(),
                    theGame.whiteUsername(), newGamePlayer,
                    theGame.gameName(), theGame.game());
        } else {
            updatedGame = new GameData(theGame.gameID(),
                    newGamePlayer, theGame.blackUsername(),
                    theGame.gameName(), theGame.game());
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
