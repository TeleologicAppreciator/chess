package result;

public class LoginResult extends Result {
    String username;
    String authToken;

    public LoginResult(String theUsername, String theAuthToken) {
        username = theUsername;
        authToken = theAuthToken;
    }

    public LoginResult(String theErrorMessage) {
        message = theErrorMessage;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
