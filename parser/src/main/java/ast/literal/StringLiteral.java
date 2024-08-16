package ast.literal;

import ast.root.ASTNodeType;
import token.Position;

public record StringLiteral(String value, Position start, Position end) implements Literal<String> {

    @Override
    public String toString() {
        return "LiteralString{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override
    public ASTNodeType getType() {
        return ASTNodeType.STRING_LITERAL;
    }
}
