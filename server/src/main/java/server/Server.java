package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import service.DeleteService;
import spark.*;

import java.util.UUID;

public class Server {
    private final DeleteService myDeleteService;
    private final UserService myUserService;

    public Server() {
        var userData = new UserData("king", "theonly", "kingkingcastle@gmail.com");
        var userDataStorage = new MemoryUserDAO();
        userDataStorage.createUser(userData);

        var uniqueID = new UUID(100, 150);
        String kingAuthToken = UUID.randomUUID().toString();
        var authData = new AuthData(kingAuthToken, userData.myUsername());
        var authDataStorage = new MemoryAuthDAO();
        authDataStorage.createAuth(authData);

        var gameDataStorage = new MemoryGameDAO();
        gameDataStorage.createGame("kings game");

        var service = new DeleteService(userDataStorage, authDataStorage, gameDataStorage);

        myDeleteService = service;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::createUser);
        Spark.delete("/db", this::deleteAll);

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

    private Object deleteAll(Request theRequest, Response theResponse) {
        myDeleteService.deleteAllData();

        theResponse.status(200);
        return "";
    }
}
