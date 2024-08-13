package token;

public record Position(int row, int col) {

    public String toString() {
        return String.format("(row: %d, col: %d)", row, col);
    }
}
