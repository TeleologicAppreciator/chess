package chess.movescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator extends DirectionalScanCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition theStartPosition) {
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();
        final int positive = 1;
        final int negative = -1;
        boolean isRow = true;

        addStraightMoves(thePiece, theBoard, theStartPosition, isRow, positive, validMoves);
        addStraightMoves(thePiece, theBoard, theStartPosition, isRow, negative, validMoves);
        addStraightMoves(thePiece, theBoard, theStartPosition, !isRow, negative, validMoves);
        addStraightMoves(thePiece, theBoard, theStartPosition, !isRow, positive, validMoves);

        return validMoves;
    }
}
