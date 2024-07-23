package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;
import request.CreateGameRequest;
import result.CreateGameResult;

public class CreateGameService {
    private AuthDAO myAuthData;
    private GameDAO myGameData;

    public CreateGameService(AuthDAO theAuthData, GameDAO theGameData) {
        myAuthData = theAuthData;
        myGameData = theGameData;
    }

    public CreateGameResult createGame(CreateGameRequest theCreateGameRequest) {
        //myAuthData.getAuth(theCreateGameRequest.authToken());

        //auth token is valid

        GameData newGame = myGameData.createGame(theCreateGameRequest.gameName());

        return new CreateGameResult(newGame.gameID());
    }
}
