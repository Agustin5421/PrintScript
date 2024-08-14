package ast;

import ast.records.ASTNodeType;

public record BinaryExpression(Expression left, Expression right, String operator) implements Expression {
    @Override
    public ASTNodeType getType() {
        return ASTNodeType.BINARY_EXPRESSION;
    }
}
