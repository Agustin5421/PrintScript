package ast.expressions;

import ast.Statement;
import ast.records.ASTNodeType;

public record ExpressionStatement(ExpressionType expressionType) implements Statement {
    @Override
    public ASTNodeType getType() {
        return ASTNodeType.EXPRESSION_STATEMENT;
    }
}
