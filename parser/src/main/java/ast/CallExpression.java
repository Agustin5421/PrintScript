package ast;

import ast.records.ASTNodeType;
import token.Position;

import java.util.List;

public record CallExpression(Identifier methodIdentifier, List<Expression> arguments,
                             boolean optionalParameters, Position start, Position end) implements Expression {
    @Override
    public ASTNodeType getType() {
        return ASTNodeType.CALL_EXPRESSION;
    }
}
