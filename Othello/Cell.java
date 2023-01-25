package Othello;

public class Cell {

    private int x; //column
    private int y; //row

    /**
     * Constructor
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return column index
     */
    public int getX() {
        return x;
    }

    /**
     * @return row index
     */
    public int getY() {
        return y;
    }

    /**
     * Comparing the cell object to other cell object
     * Cells located at the same row and column are considered as the same cell.
     *
     * @param o Object to compare.
     * @return True if both the column index and row index are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Cell)) {
            return false;
        }
        Cell that = (Cell) o;
        return x == that.x && y == that.y;
    }

}