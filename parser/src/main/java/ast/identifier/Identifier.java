package ast.identifier;

import ast.root.ASTNodeType;
import ast.expressions.Expression;
import token.Position;
import visitors.ASTVisitor;

public record Identifier(String name, Position start, Position end) implements Expression {

    @Override
    public ASTNodeType getType() {
        return ASTNodeType.IDENTIFIER;
    }

    @Override
    public ASTVisitor accept(ASTVisitor visitor) {
        return visitor.visitIdentifier(this);
    }
}
