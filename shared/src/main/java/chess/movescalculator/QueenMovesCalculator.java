package chess.movescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator extends DirectionalScanCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition theStartPosition) {
         Collection<ChessMove> validMoves = new HashSet<ChessMove>();
         final int positive = 1;
         final int negative = -1;

         addDiagonalMoves(thePiece, theBoard, theStartPosition, positive, positive, validMoves);
         addDiagonalMoves(thePiece, theBoard, theStartPosition, positive, negative, validMoves);
         addDiagonalMoves(thePiece, theBoard, theStartPosition, negative, positive, validMoves);
         addDiagonalMoves(thePiece, theBoard, theStartPosition, negative, negative, validMoves);

         addStraightMoves(thePiece, theBoard, theStartPosition, true, positive, validMoves);
         addStraightMoves(thePiece, theBoard, theStartPosition, true, negative, validMoves);
         addStraightMoves(thePiece, theBoard, theStartPosition, false, negative, validMoves);
         addStraightMoves(thePiece, theBoard, theStartPosition, false, positive, validMoves);

         return validMoves;
    }
}
