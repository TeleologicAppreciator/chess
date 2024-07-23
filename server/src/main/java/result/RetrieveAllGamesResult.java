package result;

import model.GameData;

public class RetrieveAllGamesResult extends Result {
    private GameData[] myGamesAll;

    public RetrieveAllGamesResult(GameData[] theGamesAll) {
        myGamesAll = theGamesAll;
    }

    public RetrieveAllGamesResult(String theErrorMessage) {
        message = theErrorMessage;
    }
}
