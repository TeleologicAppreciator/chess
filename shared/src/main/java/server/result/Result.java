package server.result;

public class Result {
    String message;

    public Result(String theErrorMessage) {
        message = theErrorMessage;
    }

    public Result() {}

    public String getErrorMessage() {
        return message;
    }
}
