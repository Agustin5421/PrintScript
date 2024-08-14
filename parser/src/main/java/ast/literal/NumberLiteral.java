package ast.literal;

import ast.records.ASTNodeType;
import token.Position;

public class NumberLiteral implements Literal<Number> {
    private final Number value;
    private final Position start;
    private final Position end;
    public NumberLiteral(Number value, Position start, Position end) {
        this.value = value;
        this.start = start;
        this.end = end;
    }

    public Number value() {
        return value;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }


    @Override
    public String toString() {
        return "LiteralNumber{" +
                "value=" + value +
                '}';
    }

    @Override
    public ASTNodeType getType() {
        return ASTNodeType.NUMBER_LITERAL;
    }
}
