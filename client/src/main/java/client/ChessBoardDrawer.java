package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public class ChessBoardDrawer {
    private ChessGame game;
    private boolean isBlackPerspective;

    public ChessBoardDrawer(ChessGame theChessGame, boolean isBlackPerspective) {
        game = theChessGame;
    }

    public void updateGame(ChessGame theChessGame) {
        game = theChessGame;
    }

    public void drawChessBoard() {
        System.out.print(EscapeSequences.SET_TEXT_BOLD);

        //line 1
        drawChessTop(isBlackPerspective);
        drawChessBoardBody(isBlackPerspective, null);
        drawChessTop(isBlackPerspective);

        resetConsole();
    }

    public void drawValidMoves(ChessPosition thePositionOfPieceToDrawValidMoves) {
        System.out.print(EscapeSequences.SET_TEXT_BOLD);

        drawChessTop(isBlackPerspective);
        drawChessBoardBody(isBlackPerspective, thePositionOfPieceToDrawValidMoves);
        drawChessTop(isBlackPerspective);

        resetConsole();
    }

    private void drawChessBoardBody(boolean isBlackPerspective, ChessPosition thePositionOfPieceToDrawValidMoves) {
        Collection<ChessMove> validMoves = null;
        if(thePositionOfPieceToDrawValidMoves != null) {
            validMoves = game.validMoves(thePositionOfPieceToDrawValidMoves);
        }

        if (isBlackPerspective) {
            drawChessBodyBlackPerspective(validMoves);
        } else {
            drawChessBodyWhitePerspective(validMoves);
        }
    }

    private void drawChessBodyBlackPerspective(Collection<ChessMove> validMoves) {
        for (int row = 1; row <= 8; ++row) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            drawChessSide(row);

            for (int column = 7; column >= 0; column--) {
                boolean isValidMoveSquare = isValidMovePosition(row, column+1, validMoves);

                correctTileOrder(row, column, isValidMoveSquare);

                printChessPiece(row, column);
            }

            finishChessBoardLine(row);
        }
    }

    private void drawChessBodyWhitePerspective(Collection<ChessMove> validMoves) {
        for (int row = 8; row > 0; --row) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            drawChessSide(row);

            for (int column = 0; column < 8; column++) {
                boolean isValidMoveSquare = isValidMovePosition(row, column+1, validMoves);

                correctTileOrder(row, column, isValidMoveSquare);

                printChessPiece(row, column);
            }

            finishChessBoardLine(row);
        }
    }

    private boolean isValidMovePosition(int row, int column, Collection<ChessMove> validMoves) {
        if(validMoves == null) {
            return false;
        }

        ChessPosition positionToCheck = new ChessPosition(row, column);

        boolean positionToCheckIsValidMove = false;

        for (ChessMove move : validMoves) {
            if(move.getEndPosition().equals(positionToCheck)) {
                positionToCheckIsValidMove = true;
            }
        }

        return positionToCheckIsValidMove;
    }

    private void correctTileOrder(int row, int column, boolean isValidMoveSquare) {
        if (row % 2 == 0) {
            whiteTilesFirst(column, isValidMoveSquare);
        } else {
            blackTilesFirst(column, isValidMoveSquare);
        }
    }

    private void whiteTilesFirst(int column, boolean isValidMoveSquare) {
        if (column % 2 == 0) {
            if (isValidMoveSquare) {
                System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
            } else {
                System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
            }
        } else {
            if (isValidMoveSquare) {
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
            } else {
                System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
            }
        }
    }

    private void blackTilesFirst(int column, boolean isValidMoveSquare) {
        if (column % 2 == 0) {
            if (isValidMoveSquare) {
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
            } else {
                System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
            }
        } else {
            if (isValidMoveSquare) {
                System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
            } else {
                System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
            }
        }
    }

    private void finishChessBoardLine(int row) {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        drawChessSide(row);

        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.println();
    }

    private void printChessPiece(int row, int column) {
        ChessPiece currentPiece = game.getBoard().getPiece(new ChessPosition(row, column + 1));
        if (currentPiece != null) {
            if (currentPiece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            } else {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
            }

            System.out.print(" ");
            System.out.print(currentPiece.toString());
            System.out.print(" ");
        } else {
            System.out.print(EscapeSequences.EMPTY);
        }
    }

    private void drawChessTop(boolean isBlackPerspective) {
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
        System.out.print(EscapeSequences.EMPTY);

        if (!isBlackPerspective) {
            for (char c = 'a'; c <= 'h'; ++c) {
                System.out.print(" ");
                System.out.print(c);
                System.out.print(" ");
            }
        } else {
            for (char c = 'h'; c >= 'a'; --c) {
                System.out.print(" ");
                System.out.print(c);
                System.out.print(" ");
            }
        }

        System.out.print(EscapeSequences.EMPTY);
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.println();
    }

    private void drawChessSide(int theLineNumber) {
        System.out.print(" ");
        System.out.print(theLineNumber);
        System.out.print(" ");
    }

    private void resetConsole() {
        System.out.println();
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
    }
}
