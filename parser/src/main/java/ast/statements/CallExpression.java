package ast.statements;

import ast.root.ASTNode;
import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.ASTNodeType;
import token.Position;
import visitors.ASTVisitor;

import java.util.List;

public record CallExpression(Identifier methodIdentifier, List<ASTNode> arguments,
                             boolean optionalParameters, Position start, Position end) implements Expression {

    public CallExpression(Identifier methodIdentifier, List<ASTNode> arguments, boolean optionalParameters) {
        this(methodIdentifier, arguments, optionalParameters, methodIdentifier.start(),
                arguments.isEmpty() ? methodIdentifier.end() : arguments.get(arguments.size() - 1).end());
    }
    @Override
    public ASTNodeType getType() {
        return ASTNodeType.CALL_EXPRESSION;
    }

    @Override
    public ASTVisitor visit(ASTVisitor visitor) {
        return visitor.visitCallExpression(methodIdentifier, arguments);
    }
}
