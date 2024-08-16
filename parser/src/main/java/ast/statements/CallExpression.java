package ast.statements;

import ast.root.ASTNode;
import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.ASTNodeType;
import token.Position;

import java.util.List;

public record CallExpression(Identifier methodIdentifier, List<ASTNode> arguments,
                             boolean optionalParameters, Position start, Position end) implements Expression {
    @Override
    public ASTNodeType getType() {
        return ASTNodeType.CALL_EXPRESSION;
    }
}
