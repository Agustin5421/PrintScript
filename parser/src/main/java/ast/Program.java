package ast;

import token.Position;

import java.util.List;

public class Program {
    private final List<ASTNode> statements;
    private final Position start;
    private final Position end;

    public Program(List<ASTNode> statements, Position start, Position end) {
        this.statements = statements;
        this.start = start;
        this.end = end;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }


    public List<ASTNode> getStatements() {
        return statements;
    }
}
