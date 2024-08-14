package ast;

import ast.literal.Literal;
import ast.records.ASTNodeType;
import token.Position;

import java.util.List;

public record CallExpression(Identifier callee, List<Literal> arguments, Position start, Position end) implements Expression {
    @Override
    public ASTNodeType getType() {
        return ASTNodeType.CALL_EXPRESSION;
    }
}
