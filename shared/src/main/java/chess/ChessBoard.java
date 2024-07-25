package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessPiece.PieceType;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] myBoard;

    public ChessBoard() {
        myBoard = new ChessPiece[8][8];
    }

    private ChessPiece[][] getMyBoard() {
        return myBoard;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        getMyBoard()[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (getMyBoard()[position.getRow() - 1][position.getColumn() - 1] == null) {
            return null;
        }

        return getMyBoard()[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Tells whether a given position is on the chess board or not
     *
     * @param thePosition The position you want to check is in bounds
     * @return If the position is in bounds return true
     */
    public boolean isInBounds(ChessPosition thePosition) {
        return thePosition.getRow() >= 1 && thePosition.getColumn() >= 1
                && thePosition.getRow() <= 8 && thePosition.getColumn() <= 8;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(getMyBoard(), that.getMyBoard());
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(getMyBoard());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");

        for(int i = 1; i <= 8; i++) {
            for(int j = 1; j <= 8; j++) {
                ChessPosition positionToCheck = new ChessPosition(i, j);

                sb.append("|");
                if (this.getPiece(positionToCheck) != null) {
                    sb.append(getPiece(positionToCheck).toString());
                } else {
                    sb.append(" ");
                }

            }
            sb.append("|");
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        clearBoard();

        spawnBlackTeam();
        spawnWhiteTeam();
    }

    private void clearBoard() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                getMyBoard()[i][j] = null;
            }
        }
    }

    private void spawnBlackTeam() {
        this.addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.ROOK));
        this.addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.BISHOP));
        this.addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.QUEEN));
        this.addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.KING));
        this.addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.BISHOP));
        this.addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.KNIGHT));
        this.addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.ROOK));
        this.addPiece(new ChessPosition(7, 1), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.PAWN));
        this.addPiece(new ChessPosition(7, 2), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.PAWN));
        this.addPiece(new ChessPosition(7, 3), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.PAWN));
        this.addPiece(new ChessPosition(7, 4), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.PAWN));
        this.addPiece(new ChessPosition(7, 5), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.PAWN));
        this.addPiece(new ChessPosition(7, 6), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.PAWN));
        this.addPiece(new ChessPosition(7, 7), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.PAWN));
        this.addPiece(new ChessPosition(7, 8), new ChessPiece(ChessGame.TeamColor.BLACK, PieceType.PAWN));
    }

    private void spawnWhiteTeam() {
        this.addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.ROOK));
        this.addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.BISHOP));
        this.addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.QUEEN));
        this.addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.KING));
        this.addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.BISHOP));
        this.addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.KNIGHT));
        this.addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.ROOK));
        this.addPiece(new ChessPosition(2, 1), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.PAWN));
        this.addPiece(new ChessPosition(2, 2), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.PAWN));
        this.addPiece(new ChessPosition(2, 3), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.PAWN));
        this.addPiece(new ChessPosition(2, 4), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.PAWN));
        this.addPiece(new ChessPosition(2, 5), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.PAWN));
        this.addPiece(new ChessPosition(2, 6), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.PAWN));
        this.addPiece(new ChessPosition(2, 7), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.PAWN));
        this.addPiece(new ChessPosition(2, 8), new ChessPiece(ChessGame.TeamColor.WHITE, PieceType.PAWN));
    }
}