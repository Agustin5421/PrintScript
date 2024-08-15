package ast;

import ast.records.ASTNodeType;
import token.Position;

public record AssignmentExpression(Identifier left, ASTNode right, String operator, Position start,
                                   Position end) implements Expression {


    @Override
    public ASTNodeType getType() {
        return ASTNodeType.ASSIGNMENT_EXPRESSION;
    }
}
