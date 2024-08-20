package ast.expressions;

import ast.root.ASTNode;
import ast.root.ASTNodeType;
import token.Position;
import visitors.ASTVisitor;

public record BinaryExpression(Expression left, Expression right, String operator, Position start, Position end) implements Expression {
    public BinaryExpression(Expression left, Expression right, String operator) {
        this(left, right, operator, left.start(), right.end());
    }
    @Override
    public ASTNodeType getType() {
        return ASTNodeType.BINARY_EXPRESSION;
    }

    @Override
    public ASTVisitor accept(ASTVisitor visitor) {
        return visitor;
    }
}
