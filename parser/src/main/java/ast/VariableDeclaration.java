package ast;

public record VariableDeclaration(Identifier identifier, Expression expression) implements Statement {
    @Override
    public String toString() {
        return "VariableDeclaration{" +
                "identifier=" + identifier +
                ", expression=" + expression +
                '}';
    }
}
