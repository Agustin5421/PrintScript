package ast.literal;

import token.Position;

public record StringLiteral(String value, Position start, Position end) implements Literal<String> {

    @Override
    public String toString() {
        return "LiteralString{" +
                "value='" + value + '\'' +
                '}';
    }
}
