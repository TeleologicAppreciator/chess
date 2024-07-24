package handler;

import service.DeleteAllService;
import spark.Request;
import spark.Response;

public class DeleteAllHandler {
    private final DeleteAllService myDeleteAllService;

    public DeleteAllHandler(DeleteAllService theService) {
        myDeleteAllService = theService;
    }

    public Object deleteAll(Request theRequest, Response theResponse) {
        myDeleteAllService.deleteAllData();

        theResponse.status(200);
        return "";
    }
}
