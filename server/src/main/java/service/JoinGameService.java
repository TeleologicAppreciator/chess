package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import request.JoinGameRequest;
import result.Result;

public class JoinGameService extends AuthService {
    private final GameDAO myGameData;

    public JoinGameService(AuthDAO theAuthData, GameDAO theGameData) {
        super(theAuthData);
        myGameData = theGameData;
    }

    public Result joinGame(JoinGameRequest theJoinGameRequest) {
        if (isNotAuthorized(theJoinGameRequest.authToken())) {
            return new Result("Error: unauthorized");
        }

        GameData gameToJoin;

        try {
            gameToJoin = myGameData.getGame(theJoinGameRequest.gameID());
        } catch (DataAccessException e) {
            return new Result("Error: bad request");
        }

        AuthData needUsername = null;

        try {
            needUsername = getAuthData().getAuth(theJoinGameRequest.authToken());
        } catch (DataAccessException e) {
            //there won't be errors the user is already authorized
        }

        try {
            assert needUsername != null;
            myGameData.updateGame(
                    theJoinGameRequest.playerColor(), needUsername.username(), gameToJoin);

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
