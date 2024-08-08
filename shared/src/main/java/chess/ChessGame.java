package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private final ChessBoard myChessBoard;
    private TeamColor myCurrentTeamTurn;

    public ChessGame() {
        myChessBoard = new ChessBoard();
        myCurrentTeamTurn = TeamColor.WHITE;

        myChessBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return myCurrentTeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        myCurrentTeamTurn = team;
    }

    private void setNextTeamTurn() {
        if (getTeamTurn().equals(TeamColor.BLACK)) {
            setTeamTurn(TeamColor.WHITE);
        } else {
            setTeamTurn(TeamColor.BLACK);
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(this.getBoard().getPiece(startPosition) == null) {
            return null;
        }

        HashSet<ChessMove> validMoves = new HashSet<>();

        ChessPiece pieceToMove = this.getBoard().getPiece(startPosition);
        HashSet<ChessMove> possibleMoves = (HashSet<ChessMove>) pieceToMove.pieceMoves(this.getBoard(), startPosition);

        for (ChessMove currentMove : possibleMoves) {
            ChessGame ifThisMoveGoesThrough = new ChessGame();
            ifThisMoveGoesThrough.setBoard(this.getBoard());
            ifThisMoveGoesThrough.getBoard().addPiece(currentMove.getEndPosition(), pieceToMove);
            ifThisMoveGoesThrough.getBoard().addPiece(currentMove.getStartPosition(), null);

            if(!ifThisMoveGoesThrough.isInCheck(pieceToMove.getTeamColor())) {
                validMoves.add(currentMove);
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToCheck = this.getBoard().getPiece(move.getStartPosition());

        if(pieceToCheck == null) {
            throw new InvalidMoveException("There is no piece at position " + move.getStartPosition());
        }

        if(this.getBoard().getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("It is not your turn");
        }

        if(!this.validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }

        if(move.getPromotionPiece() != null) {
            this.getBoard().addPiece(move.getEndPosition(), new ChessPiece(pieceToCheck.getTeamColor(), move.getPromotionPiece()));
            this.getBoard().addPiece(move.getStartPosition(), null);
        } else {
            this.getBoard().addPiece(move.getEndPosition(), pieceToCheck);
            this.getBoard().addPiece(move.getStartPosition(), null);
        }

        this.setNextTeamTurn();
    }

    private Collection<ChessMove> allMovesPossibleOnBoard() {
        HashSet<ChessMove> movesPossible = new HashSet<ChessMove>();

        for(int i = 1; i <= 8; i++) {
            for(int j = 1; j <= 8; j++) {
                ChessPosition positionToCheck = new ChessPosition(i, j);
                ChessPiece pieceToCheck = this.getBoard().getPiece(positionToCheck);

                if(pieceToCheck != null) {
                    movesPossible.addAll(pieceToCheck.pieceMoves(this.getBoard(), positionToCheck));
                }
            }
        }

        return movesPossible;
    }

    private ChessPosition currentPlayerKingPosition(ChessBoard theBoard, TeamColor theTeamColor) {
        boolean isWhiteTeam = theTeamColor.equals(TeamColor.WHITE);

        for(int i = 1; i <= 8; i++) {
            for(int j = 1; j <= 8; j++) {
                ChessPosition positionToCheck = new ChessPosition(i, j);
                ChessPiece pieceToCheck = theBoard.getPiece(positionToCheck);

                if(pieceToCheck != null && pieceToCheck.getPieceType().equals(ChessPiece.PieceType.KING)) {
                    if(isWhiteTeam && pieceToCheck.getTeamColor().equals(TeamColor.WHITE)) {
                        return positionToCheck;
                    } else if (!isWhiteTeam && pieceToCheck.getTeamColor().equals(TeamColor.BLACK)) {
                        return positionToCheck;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = currentPlayerKingPosition(this.getBoard(), teamColor);
        HashSet<ChessMove> isKingInTheseMoves = (HashSet<ChessMove>) allMovesPossibleOnBoard();

        for(ChessMove move : isKingInTheseMoves) {
            if(move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && noMoreValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && noMoreValidMoves(teamColor);
    }

    private boolean noMoreValidMoves(TeamColor teamColor) {
        Collection<ChessMove> isThereAnyValidMovesForMyTeam = new HashSet<>();

        for(int i = 1 ; i <= 8; i++) {
            for(int j = 1 ; j <= 8; j++) {
                ChessPosition positionToCheck = new ChessPosition(i, j);
                ChessPiece pieceToCheck = this.getBoard().getPiece(positionToCheck);

                if(pieceToCheck != null && pieceToCheck.getTeamColor().equals(teamColor)) {
                    isThereAnyValidMovesForMyTeam.addAll(this.validMoves(positionToCheck));
                }
            }
        }

        return isThereAnyValidMovesForMyTeam.isEmpty();
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        for(int i = 1; i <= 8; i++) {
            for(int j = 1; j <= 8; j++) {
                ChessPosition curPosition = new ChessPosition(i, j);

                myChessBoard.addPiece(curPosition, board.getPiece(curPosition));
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return myChessBoard;
    }
}
