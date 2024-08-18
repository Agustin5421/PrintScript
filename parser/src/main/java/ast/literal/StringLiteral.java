package ast.literal;

import ast.root.ASTNodeType;
import token.Position;
import visitors.ASTVisitor;

public record StringLiteral(String value, Position start, Position end) implements Literal<String> {

    @Override
    public String toString() {
        return "LiteralString{" +
                "expression='" + value + '\'' +
                '}';
    }

    @Override
    public ASTNodeType getType() {
        return ASTNodeType.STRING_LITERAL;
    }

    @Override
    public ASTVisitor visit(ASTVisitor visitor) {
        return visitor;
    }
}
