package server.result;

public class UserResult extends Result {
    String username;
    String authToken;

    public UserResult(String theUsername, String theAuthToken) {
        username = theUsername;
        authToken = theAuthToken;
    }
}
