package handler;

import request.RetrieveAllGamesRequest;
import result.RetrieveAllGamesResult;
import serialization.Deserializer;
import service.RetrieveAllGamesService;
import spark.Request;
import spark.Response;

public class RetrieveAllGamesHandler {
    private RetrieveAllGamesService myRetrieveAllGamesService;

    public RetrieveAllGamesHandler(RetrieveAllGamesService theRetrieveAllGamesService) {
        myRetrieveAllGamesService = theRetrieveAllGamesService;
    }

    public Object retrieveAllGames(Request theRequest, Response theResponse) {
        var retrieveAllGamesRequest = new RetrieveAllGamesRequest(theRequest.headers("Authorization"));

        return myRetrieveAllGamesService.retrieveAllGames(retrieveAllGamesRequest);
    }
}
