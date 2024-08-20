package ast.statements;

import ast.expressions.Expression;
import ast.identifier.Identifier;
import ast.root.ASTNodeType;
import token.Position;
import visitors.ASTVisitor;

public record VariableDeclaration(Identifier identifier, Expression expression, Position start, Position end) implements Statement {
    private static final ASTNodeType type = ASTNodeType.VARIABLE_DECLARATION;

    public VariableDeclaration(Identifier identifier, Expression expression) {
        this(identifier, expression, identifier.start(), expression.end());
    }

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

    @Override
    public ASTVisitor accept(ASTVisitor visitor) {
        visitor = visitor.visitVarDec(this);
        visitor = identifier().accept(visitor);
        visitor = expression().accept(visitor);
        return visitor;
    }
    // el record tiene de por si un toString pero es con [] y no {} por eso lo overridee
}
