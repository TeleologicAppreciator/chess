package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private final ChessBoard myChessBoard;
    private TeamColor myCurrentTeamTurn;

    public ChessGame() {
        myChessBoard = new ChessBoard();
        myCurrentTeamTurn = TeamColor.BLACK;

        myChessBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return myCurrentTeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        myCurrentTeamTurn = team;
    }

    private void setNextTeamTurn() {
        if (getTeamTurn().equals(TeamColor.BLACK)) {
            setTeamTurn(TeamColor.WHITE);
        } else {
            setTeamTurn(TeamColor.BLACK);
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(this.getBoard().getPiece(startPosition) == null){
            return null;
        }

        HashSet<ChessMove> validMoves = new HashSet<>();

        ChessPiece pieceToMove = this.getBoard().getPiece(startPosition);
        HashSet<ChessMove> possibleMoves = (HashSet<ChessMove>) pieceToMove.pieceMoves(this.getBoard(), startPosition);

        for (ChessMove currentMove : possibleMoves) {
            ChessGame ifThisMoveGoesThrough = new ChessGame();
            ifThisMoveGoesThrough.setTeamTurn(getTeamTurn());
            ifThisMoveGoesThrough.getBoard().addPiece(currentMove.getEndPosition(), pieceToMove);

            if(!isInCheck(getTeamTurn())) {
                validMoves.add(currentMove);
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(this.getBoard().getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("It is not your turn");
        }

        if(!this.getBoard().getPiece(move.getStartPosition()).pieceMoves(this.getBoard(), move.getStartPosition()).contains(move.getEndPosition())){
            throw new InvalidMoveException("This piece cannot make that type of move");
        }
    }

    private Collection<ChessMove> allMovesPossibleOnBoard(){
        HashSet<ChessMove> movesPossible = new HashSet<ChessMove>();

        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition positionToCheck = new ChessPosition(i, j);
                ChessPiece pieceToCheck = this.getBoard().getPiece(positionToCheck);

                if(pieceToCheck != null){
                    movesPossible.addAll(pieceToCheck.pieceMoves(this.getBoard(), positionToCheck));
                }
            }
        }

        return movesPossible;
    }

    private ChessPosition opponentKingPosition() {
        boolean isWhiteTeam = getTeamTurn().equals(TeamColor.WHITE);

        for(int i = 1; i < 8; i++) {
            for(int j = 1; j < 8; j++) {
                ChessPosition positionToCheck = new ChessPosition(i, j);
                ChessPiece pieceToCheck = this.getBoard().getPiece(positionToCheck);

                if(pieceToCheck != null && pieceToCheck.getPieceType().equals(ChessPiece.PieceType.KING)){
                    if(isWhiteTeam && !pieceToCheck.getTeamColor().equals(TeamColor.WHITE)){
                        return positionToCheck;
                    } else if (!isWhiteTeam && !pieceToCheck.getTeamColor().equals(TeamColor.BLACK)) {
                        return positionToCheck;
                    }
                }
            }
        }

        return null;
    }

    private ChessPosition currentPlayerKingPosition() {
        boolean isWhiteTeam = getTeamTurn().equals(TeamColor.WHITE);

        for(int i = 1; i < 8; i++) {
            for(int j = 1; j < 8; j++) {
                ChessPosition positionToCheck = new ChessPosition(i, j);
                ChessPiece pieceToCheck = this.getBoard().getPiece(positionToCheck);

                if(pieceToCheck != null && pieceToCheck.getPieceType().equals(ChessPiece.PieceType.KING)){
                    if(isWhiteTeam && pieceToCheck.getTeamColor().equals(TeamColor.WHITE)){
                        return positionToCheck;
                    } else if (!isWhiteTeam && pieceToCheck.getTeamColor().equals(TeamColor.BLACK)) {
                        return positionToCheck;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        if(allMovesPossibleOnBoard().contains(currentPlayerKingPosition())){
            return true;
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPosition curPosition = new ChessPosition(i, j);

                myChessBoard.addPiece(curPosition, board.getPiece(curPosition));
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return myChessBoard;
    }
}
