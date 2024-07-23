package handler;

import request.LogoutRequest;
import result.LogoutResult;
import serialization.Serializer;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    private LogoutService myLogoutService;

    public LogoutHandler(LogoutService theLogoutService) {
        myLogoutService = theLogoutService;
    }

    public Object logout(Request theRequest, Response theResponse) {
        LogoutRequest logoutRequest = new LogoutRequest(theRequest.headers("Authorization"));

        LogoutResult logoutResult = myLogoutService.logout(logoutRequest);

        if (logoutResult.hasError()) {
            theResponse.status(401);
            Serializer errorLogoutSerializer = new Serializer(logoutResult);
            return errorLogoutSerializer.serialize();
        }

        theResponse.status(200);
        return "";
    }
}
