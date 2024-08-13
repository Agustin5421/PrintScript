package ast.expressions;

import ast.Statement;

public record ExpressionStatement(ExpressionType expressionType) implements Statement {
}
