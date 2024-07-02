package chess.movescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public abstract class DirectionalScanCalculator implements PieceMovesCalculator {
    public abstract Collection<ChessMove> pieceMoves(ChessPiece thePiece, ChessBoard theBoard,
                                                     ChessPosition theStartPosition);

    protected void addDiagonalMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition theStartPosition,
                                    int theRowScanDirection, int theColumnScanDirection,
                                    Collection<ChessMove> theMoves) {

        boolean isRowScanPositiveDirection = isPositiveScanDirection(theRowScanDirection);
        boolean isColumnScanPositiveDirection = isPositiveScanDirection(theColumnScanDirection);
        int rowBounds = getScanBounds(theRowScanDirection);
        int columnBounds = getScanBounds(theColumnScanDirection);
        int rowScanTracker = getScanStartingLocation(theRowScanDirection, theStartPosition.getRow());
        int columnScanTracker = getScanStartingLocation(theColumnScanDirection, theStartPosition.getColumn());

        while(((isRowScanPositiveDirection && rowScanTracker < rowBounds)
                || (!isRowScanPositiveDirection && rowScanTracker > rowBounds))
                && ((isColumnScanPositiveDirection && columnScanTracker < columnBounds)
                || (!isColumnScanPositiveDirection && columnScanTracker > columnBounds))) {

            ChessPosition positionToCheck = new ChessPosition(rowScanTracker, columnScanTracker);

            if(!theBoard.isInBounds(positionToCheck)) {
                break;
            }
            if(theBoard.getPiece(positionToCheck) == null) {
                theMoves.add(new ChessMove(theStartPosition, positionToCheck, null));
            } else if(theBoard.getPiece(positionToCheck).getTeamColor() != thePiece.getTeamColor()) {
                theMoves.add(new ChessMove(theStartPosition, positionToCheck, null));
                break;
            } else {
                //the only other option is if a friendly unit is occupying the space
                break;
            }

            rowScanTracker += theRowScanDirection;
            columnScanTracker += theColumnScanDirection;
        }
    }

    protected void addStraightMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition theStartPosition,
                                  boolean isRow, int oneDimensionalScanDirection, Collection<ChessMove> theMoves) {

        boolean isOneDimensionScanPositive = isPositiveScanDirection(oneDimensionalScanDirection);
        int oneDimensionBounds = getScanBounds(oneDimensionalScanDirection);
        int oneDimensionalTracker;

        if(isRow){
            oneDimensionalTracker = theStartPosition.getRow() + oneDimensionalScanDirection;
        } else {
            oneDimensionalTracker = theStartPosition.getColumn() + oneDimensionalScanDirection;
        }

        while ((isOneDimensionScanPositive && oneDimensionalTracker < oneDimensionBounds)
                || (!isOneDimensionScanPositive && oneDimensionalTracker > oneDimensionBounds)){
            ChessPosition positionToCheck;

            if(isRow) {
                positionToCheck = new ChessPosition(oneDimensionalTracker, theStartPosition.getColumn());
            } else {
                positionToCheck = new ChessPosition(theStartPosition.getRow(), oneDimensionalTracker);
            }

            if (!theBoard.isInBounds(positionToCheck)) {
                break;
            }
            if (theBoard.getPiece(positionToCheck) == null) {
                theMoves.add(new ChessMove(theStartPosition, positionToCheck, null));
            } else if (theBoard.getPiece(positionToCheck).getTeamColor() != thePiece.getTeamColor()) {
                theMoves.add(new ChessMove(theStartPosition, positionToCheck, null));
                break;
            } else {
                //the only other option is if a friendly unit is occupying the space
                break;
            }

            oneDimensionalTracker += oneDimensionalScanDirection;
        }
    }

    private boolean isPositiveScanDirection(int theScanDirection) {
        return theScanDirection > 0;
    }

    protected int getScanStartingLocation(int theScanDirection, int theOneDimensionalCoordinate) {
        if(isPositiveScanDirection(theScanDirection)) {
            return theOneDimensionalCoordinate + 1;
        }

        return theOneDimensionalCoordinate - 1;
    }

    //theDirection should either be +1 or -1
    protected int getScanBounds(int theScanDirection){
        if(isPositiveScanDirection(theScanDirection)){
            return 9;
        }

        return 0;
    }
}
