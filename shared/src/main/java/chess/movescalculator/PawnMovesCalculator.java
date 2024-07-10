package chess.movescalculator;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition theStartPosition) {
        Collection<ChessMove> possibleMoves = new HashSet<ChessMove>();

        if(thePiece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            addBlackPawnMoves(thePiece, theBoard, theStartPosition, possibleMoves);
        } else {
            addWhitePawnMoves(thePiece, theBoard, theStartPosition, possibleMoves);
        }

        return possibleMoves;
    }

    private void addBlackPawnMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition thePosition,
                                   Collection<ChessMove> theValidMoves) {
        //straight ahead case
        ChessPosition positionToCheck = new ChessPosition(thePosition.getRow() - 1, thePosition.getColumn());
        addPossibleMoves(thePiece, theBoard, thePosition, positionToCheck, theValidMoves);

        //diagonal right case
        positionToCheck = new ChessPosition(thePosition.getRow() - 1, thePosition.getColumn() + 1);
        addPossibleMoves(thePiece, theBoard, thePosition, positionToCheck, theValidMoves);

        //diagonal left case
        positionToCheck = new ChessPosition(thePosition.getRow() - 1, thePosition.getColumn() - 1);
        addPossibleMoves(thePiece, theBoard, thePosition, positionToCheck, theValidMoves);
    }

    private void addWhitePawnMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition thePosition,
                                   Collection<ChessMove> thePossibleMoves) {
        //straight ahead case
        ChessPosition positionToCheck = new ChessPosition(thePosition.getRow() + 1, thePosition.getColumn());
        addPossibleMoves(thePiece, theBoard, thePosition, positionToCheck, thePossibleMoves);

        //diagonal right case
        positionToCheck = new ChessPosition(thePosition.getRow() + 1, thePosition.getColumn() + 1);
        addPossibleMoves(thePiece, theBoard, thePosition, positionToCheck, thePossibleMoves);

        //diagonal left case
        positionToCheck = new ChessPosition(thePosition.getRow() + 1, thePosition.getColumn() - 1);
        addPossibleMoves(thePiece, theBoard, thePosition, positionToCheck, thePossibleMoves);
    }

    private void addPossibleMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition theStartPosition,
                               ChessPosition theEndPositionToCheck, Collection<ChessMove> thePossibleMoves) {
        if(isNotDiagonalMove(thePiece, theStartPosition, theEndPositionToCheck)) {
            if (theBoard.isInBounds(theEndPositionToCheck) && theBoard.getPiece(theEndPositionToCheck) == null) {
                if (isNextMovePromotion(thePiece, theStartPosition)) {
                    thePossibleMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.QUEEN));
                    thePossibleMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.BISHOP));
                    thePossibleMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.KNIGHT));
                    thePossibleMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.ROOK));
                } else {
                    thePossibleMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, null));
                }

                if (isPawnOnStartPosition(thePiece, theStartPosition)) {

                    ChessPosition doublePawnMove;

                    if (thePiece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
                        doublePawnMove = new ChessPosition(theStartPosition.getRow() - 2, theStartPosition.getColumn());
                    } else {
                        doublePawnMove = new ChessPosition(theStartPosition.getRow() + 2, theStartPosition.getColumn());
                    }

                    if (theBoard.getPiece(doublePawnMove) == null) {
                        thePossibleMoves.add(new ChessMove(theStartPosition, doublePawnMove, null));
                    }

                }
            }
        } else {
            if (theBoard.isInBounds(theEndPositionToCheck) && !(theBoard.getPiece(theEndPositionToCheck) == null)
                    && (!theBoard.getPiece(theEndPositionToCheck).getTeamColor().equals(thePiece.getTeamColor()))) {
                if (isNextMovePromotion(thePiece, theStartPosition)) {
                    thePossibleMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.QUEEN));
                    thePossibleMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.BISHOP));
                    thePossibleMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.KNIGHT));
                    thePossibleMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, ChessPiece.PieceType.ROOK));
                } else {
                    thePossibleMoves.add(new ChessMove(theStartPosition, theEndPositionToCheck, null));
                }
            }
        }
    }

    private boolean isNotDiagonalMove(ChessPiece thePiece, ChessPosition theStartPosition,
                                      ChessPosition theEndPositionToCheck) {

        return theStartPosition.getColumn() == theEndPositionToCheck.getColumn();
    }

    private boolean isPawnOnStartPosition(ChessPiece thePiece, ChessPosition theStartPosition) {
        if(thePiece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            return theStartPosition.getRow() == 7;
        } else {
            return theStartPosition.getRow() == 2;
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
