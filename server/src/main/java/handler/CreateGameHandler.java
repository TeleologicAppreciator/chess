package handler;

import request.CreateGameRequest;
import result.Result;
import serialization.Deserializer;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler {
    private final CreateGameService myCreateGameService;

    public CreateGameHandler(CreateGameService theCreateGameService) {
        myCreateGameService = theCreateGameService;
    }

    public Object createGame(Request theRequest, Response theResponse) {
        var deserializeCreateGameRequest = new Deserializer(theRequest.body(), new CreateGameRequest("", ""));
        CreateGameRequest createGameRequest = (CreateGameRequest) deserializeCreateGameRequest.deserialize();
        createGameRequest.setAuthToken(theRequest.headers("Authorization"));

        Result createGameResult = myCreateGameService.createGame(createGameRequest);

        theResponse.status(this.getStatusCode(createGameResult));
        return getSerializedResult(createGameResult);
    }
}
