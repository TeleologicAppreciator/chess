package chess.movescalculator;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator extends DirectionalScanCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition theStartPosition) {
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();
        final int positive = 1;
        final int negative = -1;

        addDiagonalMoves(thePiece, theBoard, theStartPosition, positive, positive, validMoves);
        addDiagonalMoves(thePiece, theBoard, theStartPosition, positive, negative, validMoves);
        addDiagonalMoves(thePiece, theBoard, theStartPosition, negative, positive, validMoves);
        addDiagonalMoves(thePiece, theBoard, theStartPosition, negative, negative, validMoves);

        return validMoves;
    }
}
