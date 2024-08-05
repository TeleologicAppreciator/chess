package handler;

import com.google.gson.Gson;
import server.request.LogoutRequest;
import server.result.Result;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler {
    private final LogoutService myLogoutService;

    public LogoutHandler(LogoutService theLogoutService) {
        myLogoutService = theLogoutService;
    }

    public Object logout(Request theRequest, Response theResponse) {
        LogoutRequest logoutRequest = new LogoutRequest(theRequest.headers("Authorization"));

        Result logoutResult = myLogoutService.logout(logoutRequest);

        theResponse.status(getStatusCode(logoutResult));
        return new Gson().toJson(logoutResult);
    }
}
