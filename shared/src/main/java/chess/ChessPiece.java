package chess;

import chess.movescalculator.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor myTeamColor;
    private final ChessPiece.PieceType myPieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        myTeamColor = pieceColor;
        myPieceType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return myTeamColor == that.myTeamColor && myPieceType == that.myPieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myTeamColor, myPieceType);
    }

    @Override
    public String toString() {
        if (myPieceType == ChessPiece.PieceType.KING) {
            return whatCaseForPieceColor("k");
        } else if (myPieceType == ChessPiece.PieceType.QUEEN) {
            return whatCaseForPieceColor("q");
        } else if (myPieceType == ChessPiece.PieceType.BISHOP) {
            return whatCaseForPieceColor("b");
        } else if (myPieceType == ChessPiece.PieceType.KNIGHT) {
            return whatCaseForPieceColor("n");
        } else if (myPieceType == ChessPiece.PieceType.ROOK) {
            return whatCaseForPieceColor("r");
        } else if (myPieceType == ChessPiece.PieceType.PAWN) {
            return whatCaseForPieceColor("p");
        }

        return " ";
    }

    private String whatCaseForPieceColor(String pieceTypeString) {
        if(isWhitePiece()){
            return pieceTypeString.toUpperCase();
        }

        return pieceTypeString;
    }

    private boolean isWhitePiece() {
        return this.getTeamColor().equals(ChessGame.TeamColor.WHITE);
    }


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return myTeamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return myPieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();

        if(myPieceType.equals(ChessPiece.PieceType.KING)) {
            return new KingMovesCalculator().pieceMoves(this, board, myPosition);
        } else if(myPieceType.equals(ChessPiece.PieceType.QUEEN)) {
            return new QueenMovesCalculator().pieceMoves(this, board, myPosition);
        } else if(myPieceType.equals(ChessPiece.PieceType.BISHOP)) {
            return new BishopMovesCalculator().pieceMoves(this, board, myPosition);
        } else if(myPieceType.equals(ChessPiece.PieceType.KNIGHT)) {
            return new KnightMovesCalculator().pieceMoves(this, board, myPosition);
        } else if(myPieceType.equals(ChessPiece.PieceType.ROOK)) {
            return new RookMovesCalculator().pieceMoves(this, board, myPosition);
        } else {
            return new PawnMovesCalculator().pieceMoves(this, board, myPosition);
        }
    }
}
