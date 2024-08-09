package token;

public class TokenPosition {
    private final int row;
    private final int col;

    public TokenPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String toString() {
        return String.format("(row: %d, col: %d)", row, col);
    }
}
