package server;

import dataaccess.*;
import dataaccess.mysql.MySqlAuthDAO;
import dataaccess.mysql.MySqlGameDAO;
import dataaccess.mysql.MySqlUserDAO;
import handler.*;
import service.*;
import spark.*;

public class Server {
    private UserDAO myUserDatabase = null;
    private AuthDAO myAuthDatabase = null;
    private GameDAO myGameDatabase = null;

    private final DeleteAllHandler myDeleteAllHandler;
    private final RegisterHandler myRegisterHandler;
    private final LoginHandler myLoginHandler;
    private final LogoutHandler myLogoutHandler;
    private final GetAllGamesHandler myGetAllGamesHandler;
    private final CreateGameHandler myCreateGameHandler;
    private final JoinGameHandler myJoinGameHandler;


    public Server() {
        try {
            myUserDatabase = new MySqlUserDAO();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            myAuthDatabase = new MySqlAuthDAO();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            myGameDatabase = new MySqlGameDAO();
        } catch (Exception e) {
            e.printStackTrace();
        }

        myDeleteAllHandler = new DeleteAllHandler(
                new DeleteAllService(myUserDatabase, myAuthDatabase, myGameDatabase));

        myRegisterHandler = new RegisterHandler(new RegisterService(myUserDatabase, myAuthDatabase));
        myLoginHandler = new LoginHandler(new LoginService(myUserDatabase, myAuthDatabase));
        myLogoutHandler = new LogoutHandler(new LogoutService(myAuthDatabase));
        myGetAllGamesHandler = new GetAllGamesHandler(
                new GetAllGamesService(myAuthDatabase, myGameDatabase));

        myCreateGameHandler = new CreateGameHandler(new CreateGameService(myAuthDatabase, myGameDatabase));
        myJoinGameHandler = new JoinGameHandler(new JoinGameService(myAuthDatabase, myGameDatabase));
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", myRegisterHandler::register);
        Spark.post("/session", myLoginHandler::login);
        Spark.delete("/session", myLogoutHandler::logout);
        Spark.get("/game", myGetAllGamesHandler::getAllGames);
        Spark.post("/game", myCreateGameHandler::createGame);
        Spark.put("/game", myJoinGameHandler::joinGame);
        Spark.delete("/db", myDeleteAllHandler::deleteAll);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
