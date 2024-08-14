package ast;

public record BinaryExpression(Expression left, Expression right, String operator) implements Expression {
}
