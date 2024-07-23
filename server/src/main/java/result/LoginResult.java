package result;

public class LoginResult extends Result {
    String username;
    String authToken;

    public LoginResult(String theUsername, String theAuthToken) {
        username = theUsername;
        authToken = theAuthToken;
    }

    public LoginResult(String theErrorMessage) {
        super(theErrorMessage);
    }

    public String getUsername() {
        return username;
    }
}
