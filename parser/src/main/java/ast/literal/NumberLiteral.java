package ast.literal;

import ast.root.ASTNodeType;
import token.Position;

public record NumberLiteral(Number value, Position start, Position end) implements Literal<Number> {


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
