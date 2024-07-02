package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int myRow;
    private final int myColumn;

    public ChessPosition(int row, int col) {
        myRow = row;
        myColumn = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return myRow;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return myColumn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return myRow == that.myRow && myColumn == that.myColumn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(myRow, myColumn);
    }

    @Override
    public String toString() {
        return "(" + myRow + ", " + myColumn + ")";
    }
}
