package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;
import server.request.CreateGameRequest;
import server.result.CreateGameResult;
import server.result.Result;

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

        GameData newGame = null;

        try {
            newGame = myGameData.createGame(theCreateGameRequest.gameName());
        } catch (Exception e) {
            return new Result("Error: unable to read data");
        }

        return new CreateGameResult(newGame.gameID());
    }
}
