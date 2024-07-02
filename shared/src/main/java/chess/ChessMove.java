package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private final ChessPosition myStartPosition;
    private final ChessPosition myEndPosition;
    private final ChessPiece.PieceType myPromotionType;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        myStartPosition = startPosition;
        myEndPosition = endPosition;
        myPromotionType = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return myStartPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return myEndPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(myStartPosition, chessMove.myStartPosition)
                && Objects.equals(myEndPosition, chessMove.myEndPosition)
                && myPromotionType == chessMove.myPromotionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myStartPosition, myEndPosition, myPromotionType);
    }

    @Override
    public String toString() {
        return myEndPosition.toString();
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return myPromotionType;
    }
}
