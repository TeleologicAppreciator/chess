package chess.movescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition theStartPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();

        for(int row = theStartPosition.getRow() - 1; row <= theStartPosition.getRow() + 1; row++) {
            for(int col = theStartPosition.getColumn() - 1; col <= theStartPosition.getColumn() + 1; col++) {
                ChessPosition positionToCheck = new ChessPosition(row, col);

                if(theBoard.isInBounds(positionToCheck) && (theBoard.getPiece(positionToCheck) == null
                        || theBoard.getPiece(positionToCheck).getTeamColor() != thePiece.getTeamColor())) {

                    validMoves.add(new ChessMove(theStartPosition, positionToCheck, null));
                }
            }
        }

        return validMoves;
    }
}
