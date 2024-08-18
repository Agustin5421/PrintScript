package ast.statements;

import ast.root.ASTNode;
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
    public ASTVisitor visit(ASTVisitor visitor) {
        return visitor.visitAssignmentExpression(left, operator, right);
    }
}
