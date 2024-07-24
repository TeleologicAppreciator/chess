package dataaccess;

import chess.ChessGame;
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

    public GameData getGame(int gameID) throws DataAccessException {
        GameData gameFromGameID = myGameData.get(gameID);

        if (gameFromGameID == null) {
            throw new DataAccessException("Game not found");
        }

        return myGameData.get(gameID);
    }

    public GameData[] getAllGames() {
        HashSet<GameData> games = new HashSet<>(myGameData.values());
        
        GameData[] allGames = new GameData[games.size()];

        return games.toArray(allGames);
    }

    @Override
    public void updateGame(String thePlayerColor, String theUsername, GameData theGame)
            throws DataAccessException {

        int gameIDOfUpdatingGame = theGame.gameID();

        GameData gameToUpdate = myGameData.get(gameIDOfUpdatingGame);

        ChessGame.TeamColor playerColor = whatPlayerColor(thePlayerColor);

        if(playerColor == null) {
            throw new DataAccessException("Invalid player color");
        }
        else if(playerColor.equals(ChessGame.TeamColor.BLACK)) {
            if(gameToUpdate.blackUsername() != null) {
                throw new DataAccessException("Black username already exists");
            }

            gameToUpdate = new GameData(theGame.gameID(),
                    theGame.whiteUsername(), theUsername,
                    theGame.gameName(), theGame.game());
        } else {
            if(gameToUpdate.whiteUsername() != null) {
                throw new DataAccessException("White username already exists");
            }

            gameToUpdate = new GameData(theGame.gameID(),
                    theUsername, theGame.blackUsername(),
                    theGame.gameName(), theGame.game());
        }

        myGameData.remove(gameIDOfUpdatingGame);
        myGameData.put(gameIDOfUpdatingGame, gameToUpdate);
    }

    public void deleteAll() {
        myGameData.clear();
    }

    private void incrementGameID() {
        gameID++;
    }

    private ChessGame.TeamColor whatPlayerColor(String thePlayerColor) {
        if(thePlayerColor == null) {
            return null;
        }

        ChessGame.TeamColor resultTeamColor = null;

        if(thePlayerColor.equalsIgnoreCase("white")) {
            resultTeamColor = ChessGame.TeamColor.WHITE;
        } else if(thePlayerColor.equalsIgnoreCase("black")) {
            resultTeamColor = ChessGame.TeamColor.BLACK;
        }

        return resultTeamColor;
    }

    public int size() {
        return myGameData.size();
    }

    public GameData getGameTest(int gameID) {
        return myGameData.get(gameID);
    }
}
