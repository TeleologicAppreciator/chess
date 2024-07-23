package handler;

import request.LogoutRequest;
import result.LogoutResult;
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

        theResponse.status(200);
        return "";
    }
}
