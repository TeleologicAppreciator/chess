package result;

import chess.ChessGame;

public class JoinGameResult extends Result {
    private Integer ifThereIsAnErrorThisNeedsToBeNull;

    public JoinGameResult(Integer theIntegerThatIsThereIfNoErrors) {
        ifThereIsAnErrorThisNeedsToBeNull = theIntegerThatIsThereIfNoErrors;
    }

    public JoinGameResult(String theErrorMessage) {
        message = theErrorMessage;
    }

    public Integer getIfThereIsAnErrorThisNeedsToBeNull() {
        return ifThereIsAnErrorThisNeedsToBeNull;
    }

    public String getMessage() {
        return message;
    }
}
