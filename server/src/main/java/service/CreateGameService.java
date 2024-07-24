package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
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
        if (isNotAuthorized(theCreateGameRequest.authToken())) {
            return new Result("Error: unauthorized");
        }

        GameData newGame = myGameData.createGame(theCreateGameRequest.gameName());

        return new CreateGameResult(newGame.gameID());
    }
}
