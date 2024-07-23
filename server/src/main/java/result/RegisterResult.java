package result;

public class RegisterResult extends Result {
    String username;
    String authToken;

    public RegisterResult(String theUsername, String theAuthToken) {
        username = theUsername;
        authToken = theAuthToken;
    }

    public RegisterResult(String theErrorMessage) {
        super(theErrorMessage);
    }
}
