package server.result;

public class CreateGameResult extends Result {
    private int gameID;

    public CreateGameResult(int theGameID) {
        gameID = theGameID;
    }

    public CreateGameResult(String theErrorMessage) {
        super(theErrorMessage);
    }

    public int getGameID() {
        return gameID;
    }
}
