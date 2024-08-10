package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class ChessBoardDrawer {
    private ChessGame game;

    public ChessBoardDrawer(ChessGame theChessGame) {
        game = theChessGame;
    }

    public void updateGame(ChessGame theChessGame) {
        game = theChessGame;
    }

    public void drawChessBoard(boolean isBlackPerspective) {
        System.out.print(EscapeSequences.SET_TEXT_BOLD);

        //line 1
        drawChessTop(isBlackPerspective);
        drawChessBoardBody(isBlackPerspective);
        drawChessTop(isBlackPerspective);

        System.out.println();
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print(EscapeSequences.RESET_TEXT_BOLD_FAINT);
    }

    private void drawChessBoardBody(boolean isBlackPerspective) {
        if (isBlackPerspective) {
            drawChessBodyBlackPerspective();
        } else {
            drawChessBodyWhitePerspective();
        }
    }

    private void drawChessBodyBlackPerspective() {
        for (int row = 1; row <= 8; ++row) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            drawChessSide(row);

            for (int column = 7; column >= 0; column--) {
                correctTileOrder(row, column);

                printChessPiece(row, column);
            }

            finishChessBoardLine(row);
        }
    }

    private void drawChessBodyWhitePerspective() {
        for (int row = 8; row > 0; --row) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            drawChessSide(row);

            for (int column = 0; column < 8; column++) {
                correctTileOrder(row, column);

                printChessPiece(row, column);
            }

            finishChessBoardLine(row);
        }
    }

    private void correctTileOrder(int row, int column) {
        if (row % 2 == 0) {
            whiteTilesFirst(column);
        } else {
            blackTilesFirst(column);
        }
    }

    private void whiteTilesFirst(int column) {
        if (column % 2 == 0) {
            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        } else {
            System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        }
    }

    private void blackTilesFirst(int column) {
        if (column % 2 == 0) {
            System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        } else {
            System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
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
}
