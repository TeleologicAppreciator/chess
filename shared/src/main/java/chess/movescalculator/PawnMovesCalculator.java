package chess.movescalculator;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition theStartPosition) {
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();

        if(thePiece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            addBlackPawnMoves(thePiece, theBoard, theStartPosition, validMoves);
        } else {
            addWhitePawnMoves(thePiece, theBoard, theStartPosition, validMoves);
        }

        return validMoves;
    }

    private void addBlackPawnMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition thePosition,
                                   Collection<ChessMove> theValidMoves) {
        //straight ahead case
        ChessPosition positionToCheck = new ChessPosition(thePosition.getRow() - 1, thePosition.getColumn());
        addValidMoves(thePiece, theBoard, thePosition, positionToCheck, theValidMoves);

        //diagonal right case
        positionToCheck = new ChessPosition(thePosition.getRow() - 1, thePosition.getColumn() + 1);
        addValidMoves(thePiece, theBoard, thePosition, positionToCheck, theValidMoves);

        //diagonal left case
        positionToCheck = new ChessPosition(thePosition.getRow() - 1, thePosition.getColumn() - 1);
        addValidMoves(thePiece, theBoard, thePosition, positionToCheck, theValidMoves);
    }

    private void addWhitePawnMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition thePosition,
                                   Collection<ChessMove> theValidMoves) {
        //straight ahead case
        ChessPosition positionToCheck = new ChessPosition(thePosition.getRow() + 1, thePosition.getColumn());
        addValidMoves(thePiece, theBoard, thePosition, positionToCheck, theValidMoves);

        //diagonal right case
        positionToCheck = new ChessPosition(thePosition.getRow() + 1, thePosition.getColumn() + 1);
        addValidMoves(thePiece, theBoard, thePosition, positionToCheck, theValidMoves);

        //diagonal left case
        positionToCheck = new ChessPosition(thePosition.getRow() + 1, thePosition.getColumn() - 1);
        addValidMoves(thePiece, theBoard, thePosition, positionToCheck, theValidMoves);
    }

    private void addValidMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition theStartPosition,
                               ChessPosition theEndPositionToCheck, Collection<ChessMove> theValidMoves) {
        if(isNotDiagonalMove(thePiece, theStartPosition, theEndPositionToCheck)) {
            if (theBoard.getPiece(theEndPositionToCheck) == null) {
                if (isNextMovePromotion(thePiece, theStartPosition)) {
                    theValidMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.QUEEN));
                    theValidMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.BISHOP));
                    theValidMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.KNIGHT));
                    theValidMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.ROOK));
                } else {
                    theValidMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, null));
                }

                if (isPawnOnStartPosition(thePiece, theStartPosition)) {

                    ChessPosition doublePawnMove;

                    if (thePiece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
                        doublePawnMove = new ChessPosition(theStartPosition.getRow() - 2, theStartPosition.getColumn());
                    } else {
                        doublePawnMove = new ChessPosition(theStartPosition.getRow() + 2, theStartPosition.getColumn());
                    }

                    if (theBoard.getPiece(doublePawnMove) == null) {
                        theValidMoves.add(new ChessMove(theStartPosition, doublePawnMove, null));
                    }

                }
            }
        } else {
            if (theBoard.isInBounds(theEndPositionToCheck) && !(theBoard.getPiece(theEndPositionToCheck) == null)
                    && (!theBoard.getPiece(theEndPositionToCheck).getTeamColor().equals(thePiece.getTeamColor()))) {
                if (isNextMovePromotion(thePiece, theStartPosition)) {
                    theValidMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.QUEEN));
                    theValidMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.BISHOP));
                    theValidMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.KNIGHT));
                    theValidMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.ROOK));
                } else {
                    theValidMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, null));
                }
            }
        }
    }

    private boolean isNotDiagonalMove(ChessPiece thePiece, ChessPosition theStartPosition,
                                      ChessPosition theEndPositionToCheck) {

        return theStartPosition.getColumn() == theEndPositionToCheck.getColumn();
    }

    private boolean isPawnOnStartPosition(ChessPiece thePiece, ChessPosition thePosition) {
        if(thePiece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            return thePosition.getRow() == 7;
        } else {
            return thePosition.getRow() == 2;
        }
    }

    private boolean isNextMovePromotion(ChessPiece thePiece, ChessPosition theStartPosition) {
        if(thePiece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            return (theStartPosition.getRow() - 1) == 1;
        } else {
            return (theStartPosition.getRow() + 1) == 8;
        }
    }
}
