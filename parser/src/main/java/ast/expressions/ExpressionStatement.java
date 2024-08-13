package ast.expressions;

import ast.Statement;

public class ExpressionStatement implements Statement {
    private final ExpressionType expressionType;

    public ExpressionStatement(ExpressionType expressionType) {
        this.expressionType = expressionType;
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }
}
