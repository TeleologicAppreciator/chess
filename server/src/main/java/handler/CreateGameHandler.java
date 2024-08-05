package handler;

import com.google.gson.Gson;
import server.request.CreateGameRequest;
import server.result.Result;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler {
    private final CreateGameService myCreateGameService;

    public CreateGameHandler(CreateGameService theCreateGameService) {
        myCreateGameService = theCreateGameService;
    }

    public Object createGame(Request theRequest, Response theResponse) {
        var gameRequest = new Gson().fromJson(theRequest.body(), CreateGameRequest.class);
        gameRequest.setAuthToken(theRequest.headers("Authorization"));

        Result createGameResult = myCreateGameService.createGame(gameRequest);

        theResponse.status(this.getStatusCode(createGameResult));
        return new Gson().toJson(createGameResult);
    }
}
