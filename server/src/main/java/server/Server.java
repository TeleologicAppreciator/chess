package server;

import dataaccess.*;
import handler.DeleteAllHandler;
import model.AuthData;
import model.UserData;
import service.DeleteAllService;
import spark.*;

import java.util.UUID;

public class Server {
    private final DeleteAllHandler myDeleteAllHandler;
    private final UserDAO myUserDatabase;
    private final AuthDAO myAuthDatabase;
    private final GameDAO myGameDatabase;

    public Server() {
        myUserDatabase = new MemoryUserDAO();
        myAuthDatabase = new MemoryAuthDAO();
        myGameDatabase = new MemoryGameDAO();

        myDeleteAllHandler = new DeleteAllHandler(new DeleteAllService(myUserDatabase, myAuthDatabase, myGameDatabase));
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::createUser);
        Spark.delete("/db", myDeleteAllHandler::deleteAll);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object createUser(Request theRequest, Response theResponse) {
        myService.createUser();
    }
}
