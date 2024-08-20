package ast.statements;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.ASTNodeType;
import token.Position;
import visitors.ASTVisitor;

public record AssignmentExpression(Identifier left, Expression right, String operator, Position start,
                                   Position end) implements Expression {

    public AssignmentExpression(Identifier left, Expression right, String operator) {
        this(left, right, operator, left.start(), right.end());
    }

    @Override
    public ASTNodeType getType() {
        return ASTNodeType.ASSIGNMENT_EXPRESSION;
    }

    @Override
    public ASTVisitor accept(ASTVisitor visitor) {
        visitor = visitor.visitAssignmentExpression(this);
        visitor = left().accept(visitor);
        visitor = right().accept(visitor);

        return visitor;
    }
}
