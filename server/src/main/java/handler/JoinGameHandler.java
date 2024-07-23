package handler;

import request.JoinGameRequest;
import result.JoinGameResult;
import serialization.Deserializer;
import serialization.Serializer;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    private JoinGameService myJoinGameService;

    public JoinGameHandler(JoinGameService theJoinGameService) {
        myJoinGameService = theJoinGameService;
    }

    public Object joinGame(Request theRequest, Response theResponse) {
        String authToken = theRequest.headers("Authorization");

        var deserializeJoinGameRequest = new Deserializer(theRequest.body(), new JoinGameRequest("", 1, ""));

        JoinGameRequest joinGameRequest = (JoinGameRequest) deserializeJoinGameRequest.deserialize();
        joinGameRequest.setAuthToken(authToken);

        JoinGameResult joinGameStatus = myJoinGameService.joinGame(joinGameRequest);

        if(joinGameStatus.getIfThereIsAnErrorThisNeedsToBeNull() != null) {
            theResponse.status(200);
            return "";
        } else {
            theResponse.status(400);
            return joinGameStatus.getMessage();
        }
    }
}
