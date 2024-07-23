package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import request.RetrieveAllGamesRequest;
import result.RetrieveAllGamesResult;

public class RetrieveAllGamesService {
    private final AuthDAO myAuthData;
    private final GameDAO myGameData;

    public RetrieveAllGamesService(AuthDAO theAuthData, GameDAO theGameData) {
        myAuthData = theAuthData;
        myGameData = theGameData;
    }

    public RetrieveAllGamesResult retrieveAllGames(RetrieveAllGamesRequest theRetrieveAllGamesRequest) {
        //AuthData currentAuth = myAuthData.getAuth(theRetrieveAllGamesRequest.myAuthToken());

        //currentAuth isn't null

        var retrieveAllGamesResult =  new RetrieveAllGamesResult(myGameData.getAllGames());

        return retrieveAllGamesResult;
    }
}
