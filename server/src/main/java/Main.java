import chess.*;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.Server;
import service.DeleteService;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
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

        Server testServer = new Server(service);
        testServer.run(8080);
    }
}