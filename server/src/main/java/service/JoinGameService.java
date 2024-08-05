package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import server.request.JoinGameRequest;
import server.result.Result;

public class JoinGameService extends AuthService {
    private final GameDAO myGameData;

    public JoinGameService(AuthDAO theAuthData, GameDAO theGameData) {
        super(theAuthData);
        myGameData = theGameData;
    }

    public Result joinGame(JoinGameRequest theJoinGameRequest) {
        if(isNotAuthorized(theJoinGameRequest.authToken())) {
            return new Result("Error: unauthorized");
        }

        if(theJoinGameRequest.playerColor() == null) {
            return new Result("Error: bad request");
        }

        GameData gameToJoin;

        try {
            gameToJoin = myGameData.getGame(theJoinGameRequest.gameID());
        } catch(DataAccessException e) {
            return new Result("Error: bad request");
        }

        if(theJoinGameRequest.playerColor().equalsIgnoreCase("white") && gameToJoin.whiteUsername() != null ||
                theJoinGameRequest.playerColor().equalsIgnoreCase("black") && gameToJoin.blackUsername() != null) {
            return new Result("Error: already taken");
        }

        AuthData needUsername = null;

        try {
            needUsername = getAuthData().getAuth(theJoinGameRequest.authToken());
        } catch (DataAccessException e) {
            //the user is already authorized, only database errors possible
            return new Result(e.getMessage());
        }

        try {
            myGameData.updateGame(
                    theJoinGameRequest.playerColor(), needUsername.username(), gameToJoin);
        } catch(DataAccessException e) {
            if(e.getMessage().equals("Invalid player color")) {
                return new Result("Error: bad request");
            } else {
                return new Result(e.getMessage());
            }
        }

        return new Result();
    }
}
