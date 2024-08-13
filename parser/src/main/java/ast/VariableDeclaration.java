package ast;

import token.Position;

public record VariableDeclaration(Identifier identifier, Literal literal, Position start, Position end) implements Statement {

    @Override
    public String toString() {
        return "VariableDeclaration{" +
                "identifier=" + identifier +
                ", literal=" + literal +
                '}';
    }
}