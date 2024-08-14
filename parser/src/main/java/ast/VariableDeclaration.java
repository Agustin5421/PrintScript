package ast;

import ast.records.ASTNodeType;
import token.Position;

public record VariableDeclaration(Identifier identifier, Expression expression, Position start, Position end) implements Statement {

    @Override
    public String toString() {
        return "VariableDeclaration{" +
                "identifier=" + identifier +
                ", expression=" + expression +
                '}';
    }

    @Override
    public ASTNodeType getType() {
        return ASTNodeType.VARIABLE_DECLARATION;
    }
    // el record tiene de por si un toString pero es con [] y no {} por eso lo overridee
}
