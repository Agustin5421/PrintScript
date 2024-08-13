package ast.literal;

import token.Position;

public class StringLiteral implements Literal<String> {
    private final String value;
    private final Position start;
    private final Position end;
    public StringLiteral(String value, Position start, Position end) {
        this.value = value;
        this.start = start;
        this.end = end;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "LiteralString{" +
                "value='" + value + '\'' +
                '}';
    }



    @Override
    public String getValue() {
        return value;
    }
}
