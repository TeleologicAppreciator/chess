package handler;

import request.GetAllGamesRequest;
import result.Result;
import serialization.Serializer;
import service.GetAllGamesService;
import spark.Request;
import spark.Response;

public class GetAllGamesHandler {
    private final GetAllGamesService myGetAllGamesService;

    public GetAllGamesHandler(GetAllGamesService theGetAllGamesService) {
        myGetAllGamesService = theGetAllGamesService;
    }

    public Object getAllGames(Request theRequest, Response theResponse) {
        var getAllGamesRequest = new GetAllGamesRequest(theRequest.headers("Authorization"));

        Result getAllGamesResult =
                myGetAllGamesService.retrieveAllGames(getAllGamesRequest);

        String errorCode = getAllGamesResult.getErrorMessage();
        if (errorCode != null) {
            if (errorCode.equals("Error: unauthorized")) {
                theResponse.status(401);
            }
        }

        Serializer serializer = new Serializer(getAllGamesResult);
        return serializer.serialize();
    }
}
