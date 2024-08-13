package ast;

import token.Position;

public class VariableDeclaration implements Statement {
    private final Identifier identifier;
    private final Literal literal;
    private final Position start;
    private final Position end;


    public VariableDeclaration(Identifier identifier, Literal literal, Position start, Position end) {
        this.identifier = identifier;
        this.literal = literal;
        this.start = start;
        this.end = end;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Literal getLiteral() {
        return literal;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "VariableDeclaration{" +
                "identifier=" + identifier +
                ", literal=" + literal +
                '}';
    }
}
