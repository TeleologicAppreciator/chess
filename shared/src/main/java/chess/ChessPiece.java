package chess;

import java.util.ArrayList;
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
            for(int row = myPosition.getRow() - 1; row <= myPosition.getRow() + 1; row++) {
                for(int col = myPosition.getColumn() - 1; col <= myPosition.getColumn() + 1; col++) {
                    ChessPosition positionToCheck = new ChessPosition(row, col);

                    if(board.isInBounds(positionToCheck) &&
                            (board.getPiece(positionToCheck) == null || board.getPiece(positionToCheck).getTeamColor() != myTeamColor)) {

                        validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    }
                }
            }
        } else if(myPieceType.equals(ChessPiece.PieceType.QUEEN)) {
            for(int row = myPosition.getRow() + 1, col = myPosition.getColumn() + 1; row <= 8 && col <= 8; row++, col++) {
                ChessPosition positionToCheck = new ChessPosition(row, col);

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

            for(int row = myPosition.getRow() + 1, col = myPosition.getColumn() - 1; row <= 8 && col > 0; row++, col--) {
                ChessPosition positionToCheck = new ChessPosition(row, col);

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

            for(int row = myPosition.getRow() - 1, col = myPosition.getColumn() + 1; row > 0 && col <= 8; row--, col++) {
                ChessPosition positionToCheck = new ChessPosition(row, col);

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

            for(int row = myPosition.getRow() - 1, col = myPosition.getColumn() - 1; row > 0 && col > 0; row--, col--) {
                ChessPosition positionToCheck = new ChessPosition(row, col);

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

            for(int row = myPosition.getRow() + 1; row <= 8; row++) {
                ChessPosition positionToCheck = new ChessPosition(row, myPosition.getColumn());

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

            for(int row = myPosition.getRow() - 1; row > 0; row--) {
                ChessPosition positionToCheck = new ChessPosition(row, myPosition.getColumn());

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

            for(int col = myPosition.getColumn() + 1; col <= 8; col++) {
                ChessPosition positionToCheck = new ChessPosition(myPosition.getRow(), col);

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

            for(int col = myPosition.getColumn() - 1; col > 0; col--) {
                ChessPosition positionToCheck = new ChessPosition(myPosition.getRow(), col);

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

        } else if(myPieceType.equals(ChessPiece.PieceType.BISHOP)) {
            for(int row = myPosition.getRow() + 1, col = myPosition.getColumn() + 1; row <= 8 && col <= 8; row++, col++) {
                ChessPosition positionToCheck = new ChessPosition(row, col);

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

            for(int row = myPosition.getRow() + 1, col = myPosition.getColumn() - 1; row <= 8 && col > 0; row++, col--) {
                ChessPosition positionToCheck = new ChessPosition(row, col);

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

            for(int row = myPosition.getRow() - 1, col = myPosition.getColumn() + 1; row > 0 && col <= 8; row--, col++) {
                ChessPosition positionToCheck = new ChessPosition(row, col);

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

            for(int row = myPosition.getRow() - 1, col = myPosition.getColumn() - 1; row > 0 && col > 0; row--, col--) {
                ChessPosition positionToCheck = new ChessPosition(row, col);

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

        } else if(myPieceType.equals(ChessPiece.PieceType.KNIGHT)) {
            ChessPosition upLeft = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
            if(board.isInBounds(upLeft) && (board.getPiece(upLeft) == null || board.getPiece(upLeft).getTeamColor() != myTeamColor)) {
                validMoves.add(new ChessMove(myPosition, upLeft, null));
            }

            ChessPosition upRight = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
            if(board.isInBounds(upRight) && (board.getPiece(upRight) == null || board.getPiece(upLeft).getTeamColor() != myTeamColor)) {
                validMoves.add(new ChessMove(myPosition, upRight, null));
            }

            ChessPosition rightUp = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
            if(board.isInBounds(rightUp) && (board.getPiece(rightUp) == null || board.getPiece(upLeft).getTeamColor() != myTeamColor)) {
                validMoves.add(new ChessMove(myPosition, rightUp, null));
            }

            ChessPosition rightDown = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
            if(board.isInBounds(rightDown) && (board.getPiece(rightDown) == null || board.getPiece(upLeft).getTeamColor() != myTeamColor)) {
                validMoves.add(new ChessMove(myPosition, rightDown, null));
            }

            ChessPosition downRight = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
            if(board.isInBounds(downRight) && (board.getPiece(downRight) == null || board.getPiece(downRight).getTeamColor() != myTeamColor)) {
                validMoves.add(new ChessMove(myPosition, downRight, null));
            }

            ChessPosition downLeft = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
            if(board.isInBounds(downLeft) && (board.getPiece(downLeft) == null || board.getPiece(downLeft).getTeamColor() != myTeamColor)) {
                validMoves.add(new ChessMove(myPosition, downLeft, null));
            }

            ChessPosition leftDown = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
            if(board.isInBounds(leftDown) && (board.getPiece(leftDown) == null || board.getPiece(leftDown).getTeamColor() != myTeamColor)) {
                validMoves.add(new ChessMove(myPosition, leftDown, null));
            }

            ChessPosition leftUp = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
            if(board.isInBounds(leftUp) && (board.getPiece(leftUp) == null || board.getPiece(leftUp).getTeamColor() != myTeamColor)) {
                validMoves.add(new ChessMove(myPosition, leftUp, null));
            }
        } else if(myPieceType.equals(ChessPiece.PieceType.ROOK)) {
            for(int row = myPosition.getRow() + 1; row <= 8; row++) {
                ChessPosition positionToCheck = new ChessPosition(row, myPosition.getColumn());

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

            for(int row = myPosition.getRow() - 1; row > 0; row--) {
                ChessPosition positionToCheck = new ChessPosition(row, myPosition.getColumn());

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

            for(int col = myPosition.getColumn() + 1; col <= 8; col++) {
                ChessPosition positionToCheck = new ChessPosition(myPosition.getRow(), col);

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

            for(int col = myPosition.getColumn() - 1; col > 0; col--) {
                ChessPosition positionToCheck = new ChessPosition(myPosition.getRow(), col);

                if(!board.isInBounds(positionToCheck)){
                    break;
                }
                if(board.getPiece(positionToCheck) == null) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                } else if(board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    break;
                } else {
                    //the only other option is if a friendly unit is occupying the space
                    break;
                }
            }

        } else {
            if(myTeamColor.equals(ChessGame.TeamColor.WHITE)){
                ChessPosition positionToCheck = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                boolean isNextPositionPromotion = myPosition.getRow() + 1 == 8;

                //pawn moves
                if(board.getPiece(positionToCheck) == null) {
                    if(isNextPositionPromotion){
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.KNIGHT));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.ROOK));
                    } else {
                        validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    }

                    if(myPosition.getRow() == 2) {
                        ChessPosition doublePawnMove = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                        if(board.getPiece(doublePawnMove) == null) {
                            validMoves.add(new ChessMove(myPosition, doublePawnMove, null));
                        }
                    }
                }

                //diagonal right case
                positionToCheck = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                if(board.getPiece(positionToCheck) != null && board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    if(isNextPositionPromotion){
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.KNIGHT));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.ROOK));
                    } else {
                        validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    }
                }

                //diagonal left case
                positionToCheck = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                if(board.getPiece(positionToCheck) != null && board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    if(isNextPositionPromotion){
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.KNIGHT));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.ROOK));
                    } else {
                        validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    }
                }
            } else {
                ChessPosition positionToCheck = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                boolean isNextPositionPromotion = myPosition.getRow() - 1 == 1;

                if(board.getPiece(positionToCheck) == null) {
                    if(isNextPositionPromotion){
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.KNIGHT));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.ROOK));
                    } else {
                        validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    }

                    if(myPosition.getRow() == 7) {
                        ChessPosition doublePawnMove = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                        if(board.getPiece(doublePawnMove) == null) {
                            validMoves.add(new ChessMove(myPosition, doublePawnMove, null));
                        }
                    }
                }

                positionToCheck = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                if(board.getPiece(positionToCheck) != null && board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    if(isNextPositionPromotion){
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.KNIGHT));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.ROOK));
                    } else {
                        validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    }
                }

                positionToCheck = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                if(board.getPiece(positionToCheck) != null && board.getPiece(positionToCheck).getTeamColor() != myTeamColor) {
                    if(isNextPositionPromotion){
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.QUEEN));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.BISHOP));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.KNIGHT));
                        validMoves.add(new ChessMove(myPosition, positionToCheck, PieceType.ROOK));
                    } else {
                        validMoves.add(new ChessMove(myPosition, positionToCheck, null));
                    }
                }
            }
        }

        return validMoves;
    }
}
