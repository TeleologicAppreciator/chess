package handler;

import server.result.Result;

public class Handler {

    public Handler () {}

    public int getStatusCode(Result theResult) {
        String errorMessage = theResult.getErrorMessage();

        if (errorMessage == null) {
            return 200;
        } else if (errorMessage.equals("Error: bad request")) {
            return 400;
        } else if (errorMessage.equals("Error: unauthorized")) {
            return 401;
        } else if (errorMessage.equals("Error: already taken")) {
            return 403;
        } else {
            //errorMessage.equals("Error: Unable to read data")
            return 500;
        }
    }
}
