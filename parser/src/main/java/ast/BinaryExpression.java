package ast;

import ast.records.ASTNodeType;
import token.Position;

public record BinaryExpression(ASTNode left, ASTNode right, String operator, Position start, Position end) implements Expression {
    public BinaryExpression(ASTNode left, ASTNode right, String operator) {
        this(left, right, operator, left.start(), right.end());
    }
    @Override
    public ASTNodeType getType() {
        return ASTNodeType.BINARY_EXPRESSION;
    }
}
