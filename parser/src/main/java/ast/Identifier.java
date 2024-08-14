package ast;

import ast.records.ASTNodeType;
import token.Position;

public record Identifier(String name, Position start, Position end) implements Expression {

    @Override
    public ASTNodeType getType() {
        return ASTNodeType.IDENTIFIER;
    }
}
