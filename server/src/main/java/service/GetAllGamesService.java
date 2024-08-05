package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;
import server.request.GetAllGamesRequest;
import server.result.Result;
import server.result.GetAllGameResult;

public class GetAllGamesService extends AuthService {
    private final GameDAO myGameData;

    public GetAllGamesService(AuthDAO theAuthData, GameDAO theGameData) {
        super(theAuthData);
        myGameData = theGameData;
    }

    public Result getAllGames(GetAllGamesRequest theGetAllGamesRequest) {
        if (isNotAuthorized(theGetAllGamesRequest.authToken())) {
            return new Result("Error: unauthorized");
        }

        GameData[] games;
        try{
            games = new GameData[myGameData.size()];
        } catch (Exception e) {
            return new Result("Error: " + e.getMessage());
        }

        for(int i = 0; i < games.length; i++) {
            try {
                games[i] = myGameData.getGame(i+1);
            } catch (Exception e) {
                return new Result("Error: " + e.getMessage());
            }
        }

        return new GetAllGameResult(games);
    }
}
