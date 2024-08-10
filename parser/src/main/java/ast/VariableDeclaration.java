package ast;

public class VariableDeclaration implements Statement {
    private final Identifier identifier;
    private final Literal literal;


    public VariableDeclaration(Identifier identifier, Literal literal) {
        this.identifier = identifier;
        this.literal = literal;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Literal getLiteral() {
        return literal;
    }

    @Override
    public String toString() {
        return "VariableDeclaration{" +
                "identifier=" + identifier +
                ", literal=" + literal +
                '}';
    }
}
