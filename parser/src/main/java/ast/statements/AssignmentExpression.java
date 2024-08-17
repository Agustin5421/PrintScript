package ast.statements;

import ast.root.ASTNode;
import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.ASTNodeType;
import token.Position;

public record AssignmentExpression(Identifier left, Expression right, String operator, Position start,
                                   Position end) implements Expression {


    @Override
    public ASTNodeType getType() {
        return ASTNodeType.ASSIGNMENT_EXPRESSION;
    }
}
