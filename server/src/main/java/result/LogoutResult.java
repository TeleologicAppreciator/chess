package result;

public class LogoutResult extends Result {
    Integer noError;

    public LogoutResult(Integer isThereAnErrorIfSoThisNeedsToBeNull) {
        noError = isThereAnErrorIfSoThisNeedsToBeNull;
    }

    public LogoutResult(String theErrorMessage) {
        super(theErrorMessage);
    }
}
