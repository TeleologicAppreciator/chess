package chess.movescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition theStartPosition) {
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();

        ChessPosition upLeft = new ChessPosition(theStartPosition.getRow() + 2, theStartPosition.getColumn() - 1);
        addValidKnightMoves(thePiece, theBoard, theStartPosition, upLeft, validMoves);

        ChessPosition upRight = new ChessPosition(theStartPosition.getRow() + 2, theStartPosition.getColumn() + 1);
        addValidKnightMoves(thePiece, theBoard, theStartPosition, upRight, validMoves);

        ChessPosition rightUp = new ChessPosition(theStartPosition.getRow() + 1, theStartPosition.getColumn() + 2);
        addValidKnightMoves(thePiece, theBoard, theStartPosition, rightUp, validMoves);

        ChessPosition rightDown = new ChessPosition(theStartPosition.getRow() - 1, theStartPosition.getColumn() + 2);
        addValidKnightMoves(thePiece, theBoard, theStartPosition, rightDown, validMoves);

        ChessPosition downRight = new ChessPosition(theStartPosition.getRow() - 2, theStartPosition.getColumn() + 1);
        addValidKnightMoves(thePiece, theBoard, theStartPosition, downRight, validMoves);

        ChessPosition downLeft = new ChessPosition(theStartPosition.getRow() - 2, theStartPosition.getColumn() - 1);
        addValidKnightMoves(thePiece, theBoard, theStartPosition, downLeft, validMoves);

        ChessPosition leftDown = new ChessPosition(theStartPosition.getRow() - 1, theStartPosition.getColumn() - 2);
        addValidKnightMoves(thePiece, theBoard, theStartPosition, leftDown, validMoves);

        ChessPosition leftUp = new ChessPosition(theStartPosition.getRow() + 1, theStartPosition.getColumn() - 2);
        addValidKnightMoves(thePiece, theBoard, theStartPosition, leftUp, validMoves);

        return validMoves;
    }

    private void addValidKnightMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition theStartPosition,
                              ChessPosition thePositionToCheck, Collection<ChessMove> theValidMoves) {

        if(theBoard.isInBounds(thePositionToCheck) && (theBoard.getPiece(thePositionToCheck) == null
                || theBoard.getPiece(thePositionToCheck).getTeamColor() != thePiece.getTeamColor())) {

            theValidMoves.add(new ChessMove(theStartPosition, thePositionToCheck, null));
        }
    }
}
