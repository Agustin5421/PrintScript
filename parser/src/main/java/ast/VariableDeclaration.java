package ast;

import ast.literal.Literal;

public record VariableDeclaration(Identifier identifier, Literal literal) implements Statement {
    @Override
    public String toString() {
        return "VariableDeclaration{" +
                "identifier=" + identifier +
                ", literal=" + literal +
                '}';
    }
    // el record tiene de por si un toString pero es con [] y no {} por eso lo overridee
}
