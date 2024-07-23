package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
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
        AuthData authDataToVerify = userAuthorizedVerification(theGetAllGamesRequest.myAuthToken());

        Result authorizationResult = unauthorized(authDataToVerify);

        if (authorizationResult != null) {
            return authorizationResult;
        }

        return new GetAllGameResult(myGameData.getAllGames());
    }
}
