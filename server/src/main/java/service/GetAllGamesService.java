package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import request.GetAllGamesRequest;
import result.Result;
import result.GetAllGameResult;

public class GetAllGamesService extends AuthService {
    private final GameDAO myGameData;

    public GetAllGamesService(AuthDAO theAuthData, GameDAO theGameData) {
        super(theAuthData);
        myGameData = theGameData;
    }

    public Result retrieveAllGames(GetAllGamesRequest theGetAllGamesRequest) {
        if (isNotAuthorized(theGetAllGamesRequest.authToken())) {
            return new Result("Error: unauthorized");
        }

        GetAllGameResult result = null;

        try {
            result = new GetAllGameResult(myGameData.getAllGames());
        } catch (Exception e) {
            return new Result("Unable to get data");
        }

        return result;
    }
}
