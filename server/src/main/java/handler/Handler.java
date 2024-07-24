package handler;

import result.Result;
import serialization.Serializer;

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
        } else {
            //errorMessage.equals("Error: already taken")
            return 403;
        }
    }

    public String getSerializedResult (Result theResult) {
        Serializer serializer = new Serializer(theResult);
        return serializer.serialize();
    }
}
