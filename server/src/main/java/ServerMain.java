import server.Server;

public class ServerMain {
    public static void main(String[] args) {
        Server testServer = new Server();
        testServer.run(8080);
    }
}