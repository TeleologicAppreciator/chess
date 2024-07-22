package server;

import dataaccess.*;
import handler.DeleteAllHandler;
import handler.RegisterHandler;
import model.AuthData;
import model.UserData;
import service.DeleteAllService;
import service.RegisterService;
import spark.*;

import java.util.UUID;

public class Server {
    private final DeleteAllHandler myDeleteAllHandler;
    private final RegisterHandler myRegisterHandler;
    private final UserDAO myUserDatabase;
    private final AuthDAO myAuthDatabase;
    private final GameDAO myGameDatabase;

    public Server() {
        myUserDatabase = new MemoryUserDAO();
        myAuthDatabase = new MemoryAuthDAO();
        myGameDatabase = new MemoryGameDAO();

        myDeleteAllHandler = new DeleteAllHandler(new DeleteAllService(myUserDatabase, myAuthDatabase, myGameDatabase));
        myRegisterHandler = new RegisterHandler(new RegisterService(myUserDatabase));
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", myRegisterHandler::register);
        Spark.post("/session", )
        Spark.delete("/db", myDeleteAllHandler::deleteAll);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
