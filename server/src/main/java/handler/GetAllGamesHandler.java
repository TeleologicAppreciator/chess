package handler;

import com.google.gson.Gson;
import server.request.GetAllGamesRequest;
import service.GetAllGamesService;
import spark.Request;
import spark.Response;

public class GetAllGamesHandler extends Handler {
    private final GetAllGamesService myGetAllGamesService;

    public GetAllGamesHandler(GetAllGamesService theGetAllGamesService) {
        myGetAllGamesService = theGetAllGamesService;
    }

    public Object getAllGames(Request theRequest, Response theResponse) {
        var getAllGamesRequest = new GetAllGamesRequest(theRequest.headers("Authorization"));

        var getAllGamesResult = myGetAllGamesService.getAllGames(getAllGamesRequest);

        theResponse.status(getStatusCode(getAllGamesResult));
        return new Gson().toJson(getAllGamesResult);
    }
}
