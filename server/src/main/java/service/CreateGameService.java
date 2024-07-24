package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import result.CreateGameResult;
import result.Result;

public class CreateGameService extends AuthService {
    private final GameDAO myGameData;

    public CreateGameService(AuthDAO theAuthData, GameDAO theGameData) {
        super(theAuthData);
        myGameData = theGameData;
    }

    public Result createGame(CreateGameRequest theCreateGameRequest) {
        AuthData authDataToVerify = userAuthorizedVerification(theCreateGameRequest.authToken());

        Result authorizationResult = unauthorized(authDataToVerify);

        if (authorizationResult != null) {
            return authorizationResult;
        }

        GameData newGame = myGameData.createGame(theCreateGameRequest.gameName());

        return new CreateGameResult(newGame.gameID());
    }
}
