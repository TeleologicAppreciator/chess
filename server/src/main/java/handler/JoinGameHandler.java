package handler;

import request.JoinGameRequest;
import result.Result;
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

        Result joinGameStatus = myJoinGameService.joinGame(joinGameRequest);
        String errorMessage = joinGameStatus.getErrorMessage();

        if(errorMessage == null) {
            theResponse.status(200);
            return "";
        } else if(errorMessage.equals("Error: unauthorized")) {
            theResponse.status(401);;
        } else if(errorMessage.equals("Error: bad request")) {
            theResponse.status(400);
        } else {
            //already taken error
            theResponse.status(403);
        }

        Serializer errorSerializer = new Serializer(joinGameStatus);
        return errorSerializer.serialize();
    }
}
