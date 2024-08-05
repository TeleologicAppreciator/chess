package server.result;

import model.GameData;

public class GetAllGameResult extends Result {
    private GameData[] games;

    public GetAllGameResult(GameData[] theGames) {
        games = theGames;
    }

    public GetAllGameResult(String theErrorMessage) {
        super(theErrorMessage);
    }

    public GameData[] games() {
        return games;
    }

}
