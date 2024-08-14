package ast;

import ast.records.ASTNodeType;
import token.Position;

public class Identifier implements Expression {
    private final String name;
    private final Position start;
    private final Position end;

    public Identifier(String name, Position start, Position end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    @Override
    public ASTNodeType getType() {
        return ASTNodeType.IDENTIFIER;
    }
}
