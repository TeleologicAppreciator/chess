package result;

public class LogoutResult extends Result {
    boolean isError = true;

    public LogoutResult() {
        isError = false;
    }

    public LogoutResult(String theErrorMessage) {
        super(theErrorMessage);
    }

    public boolean hasError() {
        return isError;
    }
}
