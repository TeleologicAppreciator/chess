package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import request.JoinGameRequest;
import result.JoinGameResult;
import result.Result;

public class JoinGameService extends AuthService{
    private GameDAO myGameData;

    public JoinGameService(AuthDAO theAuthData, GameDAO theGameData) {
        super(theAuthData);
        myGameData = theGameData;
    }

    public Result joinGame(JoinGameRequest theJoinGameRequest) {
        AuthData authDataToVerify = userAuthorizedVerification(theJoinGameRequest.authToken());

        Result authorizationResult = unauthorized(authDataToVerify);

        if (authorizationResult != null) {
            return authorizationResult;
        }

        GameData gameToJoin;

        try {
            gameToJoin = myGameData.getGame(theJoinGameRequest.gameID());
        } catch (DataAccessException e) {
            return new Result("Error: bad request");
        }

        try {
            myGameData.updateGame(
                    theJoinGameRequest.playerColor(), authDataToVerify.username(), gameToJoin);

        } catch (DataAccessException e) {
            if(e.getMessage().equals("Invalid player color")) {
                return new Result("Error: bad request");
            } else {
                return new Result("Error: already taken");
            }
        }

        return new Result();
    }
}
