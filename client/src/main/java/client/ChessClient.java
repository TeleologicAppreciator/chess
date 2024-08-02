package client;

import dataaccess.DataAccessException;

public class ChessClient {
    private String visitorName = null;
    private State state = State.SIGNEDOUT;

    public String signIn(String... params) throws DataAccessException {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            visitorName = String.join("-", params);
            return String.format("You signed in as %s", visitorName);
        }
        throw new DataAccessException("Expected: <yourname>");
    }
}
