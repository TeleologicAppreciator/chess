package request;

public class CreateGameRequest {
    private String authToken;
    private final String gameName;

    public CreateGameRequest(String theAuthToken, String theGameName) {
        authToken = theAuthToken;
        gameName = theGameName;
    }

    public String authToken() {
        return authToken;
    }

    public String gameName() {
        return gameName;
    }

    public void setAuthToken(String theAuthToken) {
        authToken = theAuthToken;
    }
}
