package handler;

import result.Result;
import service.DeleteAllService;
import spark.Request;
import spark.Response;

public class DeleteAllHandler extends Handler {
    private final DeleteAllService myDeleteAllService;

    public DeleteAllHandler(DeleteAllService theService) {
        myDeleteAllService = theService;
    }

    public Object deleteAll(Request theRequest, Response theResponse) {
        try {
            myDeleteAllService.deleteAllData();
        } catch (Exception e) {
            theResponse.status(500);
            getSerializedResult(new Result("\"message\": \"Error: " + e.getMessage() + "\""));
        }

        theResponse.status(200);
        return "";
    }
}
