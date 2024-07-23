package handler;

import request.CreateGameRequest;
import result.Result;
import serialization.Deserializer;
import serialization.Serializer;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    private CreateGameService myCreateGameService;

    public CreateGameHandler(CreateGameService theCreateGameService) {
        myCreateGameService = theCreateGameService;
    }

    public Object createGame(Request theRequest, Response theResponse) {
        String authToken = theRequest.headers("Authorization");

        var deserializeCreateGameHandler = new Deserializer(theRequest.body(), new CreateGameRequest("", ""));
        CreateGameRequest createGameRequest = (CreateGameRequest) deserializeCreateGameHandler.deserialize();
        createGameRequest.setAuthToken(authToken);

        Result createGameResult = myCreateGameService.createGame(createGameRequest);

        String errorMessage = createGameResult.getErrorMessage();
        if(errorMessage != null) {
            if(errorMessage.equals("Error: bad request")) {
                theResponse.status(400);
            } else {
                theResponse.status(401);
            }
        }

        Serializer serializer = new Serializer(createGameResult);
        return serializer.serialize();
    }
}
