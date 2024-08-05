package handler;

import com.google.gson.Gson;
import server.request.JoinGameRequest;
import server.result.Result;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {
    private final JoinGameService myJoinGameService;

    public JoinGameHandler(JoinGameService theJoinGameService) {
        myJoinGameService = theJoinGameService;
    }

    public Object joinGame(Request theRequest, Response theResponse) {
        JoinGameRequest joinGameRequest = new Gson().fromJson(theRequest.body(), JoinGameRequest.class);
        joinGameRequest.setAuthToken(theRequest.headers("Authorization"));

        Result joinGameResult = myJoinGameService.joinGame(joinGameRequest);

        theResponse.status(getStatusCode(joinGameResult));
        return new Gson().toJson(joinGameResult);
    }
}
