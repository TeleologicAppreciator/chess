package chess.movescalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public interface PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessPiece thePiece, ChessBoard theBoard, ChessPosition theStartPosition);
}
