package handler;

import request.JoinGameRequest;
import result.Result;
import serialization.Deserializer;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {
    private final JoinGameService myJoinGameService;

    public JoinGameHandler(JoinGameService theJoinGameService) {
        myJoinGameService = theJoinGameService;
    }

    public Object joinGame(Request theRequest, Response theResponse) {
        var deserializeJoinGameRequest = new Deserializer(theRequest.body(), new JoinGameRequest("", 1, ""));

        JoinGameRequest joinGameRequest = (JoinGameRequest) deserializeJoinGameRequest.deserialize();
        joinGameRequest.setAuthToken(theRequest.headers("Authorization"));

        Result joinGameResult = myJoinGameService.joinGame(joinGameRequest);

        theResponse.status(getStatusCode(joinGameResult));
        return getSerializedResult(joinGameResult);
    }
}
