package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import request.RetrieveAllGamesRequest;
import result.RetrieveAllGamesResult;

public class RetrieveAllGamesService extends AuthService {
    private final GameDAO myGameData;

    public RetrieveAllGamesService(AuthDAO theAuthData, GameDAO theGameData) {
        super(theAuthData);
        myGameData = theGameData;
    }

    public RetrieveAllGamesResult retrieveAllGames(RetrieveAllGamesRequest theRetrieveAllGamesRequest) {
        //AuthData currentAuth = myAuthData.getAuth(theRetrieveAllGamesRequest.myAuthToken());

        //currentAuth isn't null

        var retrieveAllGamesResult =  new RetrieveAllGamesResult(myGameData.getAllGames());

        return retrieveAllGamesResult;
    }
}
