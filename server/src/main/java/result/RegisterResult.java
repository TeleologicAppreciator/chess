package result;

public class RegisterResult extends Result {
    String myUsername;
    String myAuthToken;

    public RegisterResult(String theUsername, String theAuthToken) {
        myUsername = theUsername;
        myAuthToken = theAuthToken;
    }

    public RegisterResult(String theError) {
        myMessage = theError;
    }
}
