package ast;

import ast.literal.Literal;
import ast.records.ASTNodeType;
import token.Position;

public record VariableDeclaration(Identifier identifier, Literal literal, Position start, Position end) implements Statement {

    @Override
    public String toString() {
        return "VariableDeclaration{" +
                "identifier=" + identifier +
                ", literal=" + literal +
                '}';
    }

    @Override
    public ASTNodeType getType() {
        return ASTNodeType.VARIABLE_DECLARATION;
    }
    // el record tiene de por si un toString pero es con [] y no {} por eso lo overridee
}
