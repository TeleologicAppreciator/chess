package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import request.JoinGameRequest;
import result.JoinGameResult;

public class JoinGameService {
    private AuthDAO myAuthData;
    private GameDAO myGameData;

    public JoinGameService(AuthDAO theAuthData, GameDAO theGameData) {
        myAuthData = theAuthData;
        myGameData = theGameData;
    }

    public JoinGameResult joinGame(JoinGameRequest theJoinGameRequest) {
        //AuthData theUserAuthData = myAuthData.getAuth(theJoinGameRequest.authToken());

        GameData gameToJoin = myGameData.getGame(theJoinGameRequest.gameID());

        //myGameData.updateGame(theJoinGameRequest.teamColorOfJoiningPlayer(), theUserAuthData, gameToJoin);

        return new JoinGameResult(Integer.valueOf(1));
    }
}
