package ast;

import ast.records.ASTNodeType;
import token.Position;

public record VariableDeclaration(Identifier identifier, ASTNode value, Position start, Position end) implements Statement {
    private static final ASTNodeType type = ASTNodeType.VARIABLE_DECLARATION;

    public VariableDeclaration(Identifier identifier, ASTNode expression) {
        this(identifier, expression, identifier.start(), expression.end());
    }

    @Override
    public String toString() {
        return "VariableDeclaration{" +
                "identifier=" + identifier +
                ", value=" + value +
                '}';
    }

    @Override
    public ASTNodeType getType() {
        return ASTNodeType.VARIABLE_DECLARATION;
    }
    // el record tiene de por si un toString pero es con [] y no {} por eso lo overridee
}
