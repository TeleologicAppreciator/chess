package server;

import org.eclipse.jetty.client.HttpResponseException;
import service.DeleteService;
import spark.*;

public class Server {
    private final DeleteService myService;

    public Server(DeleteService theService) {
        myService = theService;
    }

    public Server run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::deleteAll);

        Spark.awaitInitialization();
        return this;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object deleteAll(Request req, Response res) {
        myService.deleteAllData();

        res.status(200);
        return "";
    }
}
